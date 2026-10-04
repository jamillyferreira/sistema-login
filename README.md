# Sistema de Login

API REST de autenticação construída com **Java 21** e **Spring Boot**, com JWT de curta duração, refresh token com rotação e recuperação de senha por e-mail.

O projeto foi desenvolvido em duas fases: primeiro sem Spring Security, para entender o fluxo de autenticação "na mão", e depois com Spring Security e JWT.

## Funcionalidades

- Cadastro e login com senha criptografada (BCrypt)
- Access token JWT de curta duração (10 minutos)
- Refresh token opaco, guardado como hash, com **rotação** a cada uso e revogação
- Logout que revoga o refresh token (idempotente)
- Recuperação de senha por e-mail, com código de 6 dígitos de uso único
- Revogação de todas as sessões ativas ao redefinir a senha
- Tratamento global de erros, com resposta padronizada

## Tecnologias

| Área | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot (Web MVC, Data JPA, Validation, Mail) |
| Segurança | Spring Security, JWT (JJWT), BCrypt |
| Banco de dados | PostgreSQL 18 |
| Migrations | Flyway |
| Infraestrutura | Docker e Docker Compose, Maven |

## Como executar

### Pré-requisitos

- Java 21
- Docker e Docker Compose
- Uma conta Gmail com **senha de app** (exige verificação em duas etapas), usada para enviar os e-mails de recuperação de senha

### Passo a passo

1. Clone o repositório e entre na pasta do projeto.

2. Crie o arquivo `.env` na raiz, a partir do exemplo, e preencha os valores:

   ```bash
   cp .env.example .env
   ```

3. Suba o banco de dados:

   ```bash
   docker compose up -d
   ```

4. Rode a aplicação com o profile `dev`:

   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

   (ou `mvn` no lugar de `./mvnw`, se você não usa o wrapper). Na IDE, basta definir o profile ativo como `dev`.

A API sobe em `http://localhost:8080`, e as migrations do Flyway são aplicadas automaticamente na inicialização.

### Variáveis de ambiente

| Variável | Descrição |
|---|---|
| `POSTGRES_DB` | Nome do banco de dados |
| `POSTGRES_USER` | Usuário do banco |
| `POSTGRES_PASSWORD` | Senha do banco |
| `JWT_SECRET` | Chave de assinatura do JWT, em Base64 |
| `MAIL_USERNAME` | Conta Gmail que envia os e-mails |
| `MAIL_PASSWORD` | Senha de app do Gmail |

Para gerar um `JWT_SECRET`:

```bash
openssl rand -base64 64
```

O arquivo `.env` está no `.gitignore` e nunca deve ser versionado.

## Endpoints

| Método | Rota | Acesso | Descrição |
|---|---|---|---|
| `POST` | `/auth/register` | público | Cria um usuário |
| `POST` | `/auth/login` | público | Autentica e devolve access token e refresh token |
| `POST` | `/auth/refresh` | público | Troca o refresh token por um novo par de tokens (o antigo é revogado) |
| `POST` | `/auth/logout` | público | Revoga o refresh token enviado |
| `POST` | `/auth/forgot-password` | público | Envia o código de redefinição por e-mail |
| `POST` | `/auth/reset-password` | público | Redefine a senha usando o código recebido |
| `GET` | `/users/me` | autenticado | Retorna os dados do usuário autenticado |

As rotas autenticadas exigem o cabeçalho `Authorization: Bearer <access_token>`.

### Exemplo: login

```json
{
  "email": "usuario@exemplo.com",
  "password": "minhaSenha123"
}
```

Resposta:

```json
{
  "access_token": "eyJhbGciOi...",
  "refresh_token": "3f1c9a52-...",
  "token_type": "Bearer",
  "expires_in": 600
}
```

### Exemplo: erro

Todos os erros seguem o mesmo formato:

```json
{
  "type": "about:blank",
  "title": "Unauthorized",
  "status": 401,
  "detail": "Refresh token inválido ou expirado",
  "path": "/auth/refresh",
  "timestamp": "2026-10-03T16:09:26.706282885Z"
}
```

Erros de validação trazem também a lista de campos inválidos.

## Fluxo de recuperação de senha

1. O cliente envia o e-mail para `POST /auth/forgot-password`:

   ```json
   { "email": "usuario@exemplo.com" }
   ```

2. A API responde sempre da mesma forma, exista o e-mail ou não. Se ele existir, um código de 6 dígitos é gerado e enviado por e-mail em segundo plano.

3. O cliente envia o código e a nova senha para `POST /auth/reset-password`:

   ```json
   {
     "code": "482913",
     "new_password": "novaSenha123"
   }
   ```

4. Se o código for válido, a senha é trocada, o código é marcado como usado e todos os refresh tokens do usuário são revogados. Código inexistente, expirado ou já usado recebe a mesma resposta (`400`), sem revelar qual foi o caso.

## Decisões de segurança

**Autenticação**
- Sessões stateless e CSRF desabilitado, já que a autenticação é feita por token no cabeçalho.
- Senhas guardadas com BCrypt.
- Access token com 10 minutos de validade, o que limita a janela de uso de um token vazado.

**Refresh token**
- Valor aleatório e opaco. No banco fica só o hash (SHA-256), e o valor original é entregue uma única vez ao cliente.
- Rotação: cada uso revoga o token anterior, então um token reutilizado é recusado.
- Logout idempotente: repetir o logout, ou usar um token já inválido, responde `204` sem erro.

**Recuperação de senha**
- Resposta idêntica para e-mail existente e inexistente, para não revelar quais e-mails estão cadastrados (user enumeration).
- E-mail enviado de forma assíncrona (`@Async`), para o tempo de resposta também não revelar se o e-mail existe.
- Código gerado com `SecureRandom`, válido por 15 minutos e de uso único.
- Um código ativo por usuário: ao pedir um novo, o anterior é apagado.
- A mensagem de erro do reset é a mesma para código inexistente, expirado ou já usado.
- O `INSERT` do código é enviado ao banco antes do envio do e-mail.
- Os logs do fluxo de reset não registram o código nem o e-mail informado.

## Banco de dados

O schema é versionado com Flyway (`src/main/resources/db/migration`), e o Hibernate roda com `ddl-auto: validate`, apenas conferindo se as entidades batem com as tabelas.

| Tabela | Conteúdo |
|---|---|
| `users` | Usuários (chaves primárias UUID) |
| `refresh_tokens` | Hash do refresh token, expiração, flag de revogação e usuário |
| `password_reset_tokens` | Código de redefinição, expiração, flag de uso e usuário (`ON DELETE CASCADE`) |


## Limitações conhecidas e próximos passos

**Limitações conhecidas**

- Depois do logout ou da redefinição de senha, o **access token continua válido até expirar** (no máximo 10 minutos), porque o JWT é validado sem consultar o banco.
- O código de redefinição é guardado **em texto puro**. É uma escolha de estudo para comparar com a versão que usa hash.
- A unicidade global do código de 6 dígitos tem uma chance pequena de colisão, que cresce com o número de registros na tabela.
- Os endpoints de recuperação de senha **não têm rate limit**.
- Códigos expirados só são removidos quando o mesmo usuário pede um novo.

**Próximos passos**

- Guardar o código de redefinição como hash
- Rate limit nos endpoints de recuperação de senha
- Job agendado para limpar códigos expirados
- Entregar o refresh token em cookie HttpOnly
- Login com Google (OAuth2)
- Containerizar a aplicação com um Dockerfile
- Testes automatizados

## Autora

Jamilly Ferreira de Souza
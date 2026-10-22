package com.ferreiradev.sistema_login.docs;

import com.ferreiradev.sistema_login.dtos.request.*;
import com.ferreiradev.sistema_login.dtos.response.MessageResponse;
import com.ferreiradev.sistema_login.dtos.response.AuthResponse;
import com.ferreiradev.sistema_login.dtos.response.UserResponse;
import com.ferreiradev.sistema_login.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Autenticação - Sistema de login",
        description = "Endpoints de registro, login, logout e recuperação de senha")
public interface AuthControllerDoc {

   // ENDPOINT REGISTER
   @Operation(
           summary = "Registrar novo usuário",
           description = "Cria um novo usuário no sistema com e-mail e senha",
           security = {}  // endpoint público
   )
   @ApiResponses({
           @ApiResponse(responseCode = "201", description = "Usuário criado e logado com sucesso",
                   content = @Content(schema = @Schema(implementation = AuthResponse.class)),
                   headers = @Header(name = "Set-Cookie", description = "Refresh token em cookie HttpOnly",
                           schema = @Schema(
                                   type = "string",
                                   example = "refresh_token=eyJ...; Path=/api/auth; HttpOnly; Secure; SameSite=Strict")
                   )),
           @ApiResponse(responseCode = "400", description = "Dados inválidos no corpo da requisição",
                   content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
           @ApiResponse(responseCode = "409", description = "E-mail já cadastrado no sistema",
                   content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
   })
   ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request);

   // ENDPOINT LOGIN
   @Operation(
           summary = "Fazer login",
           description = """
                   Autentica o usuário e retorna:
                   - **access token** no corpo da resposta (curta duração);
                   - **refresh token** em cookie `HttpOnly`, `Secure`, `SameSite=Strict`, `Path=/api/auth`
                   """,
           security = {}
   )
   @ApiResponses({
           @ApiResponse(
                   responseCode = "200", description = "Login realizado com sucesso",
                   content = @Content(schema = @Schema(implementation = AuthResponse.class)),
                   headers = @Header(name = "Set-Cookie", description = "Refresh token em cookie HttpOnly",
                           schema = @Schema(type = "string", example = "refresh_token=eyJ...; Path=/api/auth; HttpOnly; Secure; SameSite=Strict")
                   )),
           @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                   content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
   })
   ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request);


   // ENDPOINT REFRESH TOKEN
   @Operation(
           summary = "Renovar token de acesso",
           description = """
                   Gera um novo access token e rotaciona o refresh token a partir do
                   cookie `refresh_token` enviado automaticamente pelo navegador.
                   **Não requer corpo de requisição.**
                   """,
           security = {},
           parameters = {
                   @Parameter(name = "refresh_token", in = ParameterIn.COOKIE, required = false,
                           description = "Refresh token previamente emitido no login/refresh.",
                           schema = @Schema(type = "string")
                   )
           }
   )
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "Token renovado",
                   content = @Content(schema = @Schema(implementation = AuthResponse.class))),
           @ApiResponse(responseCode = "400", description = "Dados inválidos no corpo da requisição",
                   content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
           @ApiResponse(responseCode = "401", description = "Refresh token inválido ou expirado",
                   content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
   })
   ResponseEntity<AuthResponse> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken);


   // ENDPOINT LOGOUT
   @Operation(summary = "Realizar logout",
           description = """
                   Invalida o refresh token do usuário (se presente) e limpa o cookie
                   `refresh_token` enviando `Set-Cookie` com `Max-Age=0`.
                   Idempotente: pode ser chamado mesmo sem cookie.
                   """)
   @ApiResponses({
           @ApiResponse(responseCode = "204", description = "Logout realizado, cookie limpo",
                   headers = @Header(name = "Set-Cookie", description = "Limpeza do cookie de refresh",
                           schema = @Schema(type = "string", example = "refresh_token=; Path=/api/auth; Max-Age=0; HttpOnly; Secure; SameSite=Strict")
                   )),
           @ApiResponse(responseCode = "401", description = "Não autenticado",
                   content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
   })
   ResponseEntity<Void> logout(@CookieValue(name = "refresh_token", required = false) String refreshToken);


   // ENDPOINT FORGOT PASSWORD
   @Operation(
           summary = "Solicitar redefinição de senha",
           description = "Envia um código de redefinição para o e-mail, se existir",
           security = {}
   )
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "Solicitação processada",
                   content = @Content(schema = @Schema(implementation = MessageResponse.class)))
   })
   ResponseEntity<MessageResponse> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request);

   // ENDPOINT RESET PASSWORDl
   @Operation(
           summary = "Redefinir senha",
           description = "Redefine a senha usando o código recebido por e-mail",
           security = {}
   )
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "Senha redefinida",
                   content = @Content(schema = @Schema(implementation = MessageResponse.class))),
           @ApiResponse(responseCode = "400", description = "Código inválido ou expirado",
                   content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
   })
   ResponseEntity<MessageResponse> resetPassword(@RequestBody @Valid ResetPasswordRequest request);

}

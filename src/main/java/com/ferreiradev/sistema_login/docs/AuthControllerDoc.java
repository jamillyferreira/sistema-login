package com.ferreiradev.sistema_login.docs;

import com.ferreiradev.sistema_login.dtos.request.*;
import com.ferreiradev.sistema_login.dtos.response.MessageResponse;
import com.ferreiradev.sistema_login.dtos.response.TokenResponse;
import com.ferreiradev.sistema_login.dtos.response.UserResponse;
import com.ferreiradev.sistema_login.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Autenticação", description = "Endpoints de registro, login, logout e recuperação de senha")
public interface AuthControllerDoc {

    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria um novo usuário no sistema com e-mail e senha",
            security = {}  // endpoint público
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos no corpo da requisição",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado no sistema",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<UserResponse> register(@RequestBody @Valid RegisterRequest request);

    @Operation(
            summary = "Fazer login",
            description = "Retorna o access token e refresh token do usuário",
            security = {}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request);

    @Operation(
            summary = "Renovar token de acesso",
            description = "Gera um novo access token a partir de um refresh token válido",
            security = {}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token renovado",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos no corpo da requisição",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Refresh token inválido ou expirado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<TokenResponse> refresh(@RequestBody @Valid RefreshRequest request);


    @Operation(summary = "Realizar logout", description = "Invalida o refresh token do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Logout realizado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<Void> logout(@RequestBody @Valid RefreshRequest request);


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

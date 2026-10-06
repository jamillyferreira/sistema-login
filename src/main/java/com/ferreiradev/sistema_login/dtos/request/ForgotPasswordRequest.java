package com.ferreiradev.sistema_login.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Dados para solicitar redefinição de senha")
public record ForgotPasswordRequest(
        @Schema(description = "E-mail cadastrado do usuário")
        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail não pode ser vazio")
        String email
) {
}

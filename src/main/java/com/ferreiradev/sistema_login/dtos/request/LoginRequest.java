package com.ferreiradev.sistema_login.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para login do usuário")
public record LoginRequest(
        @Schema(description = "E-mail do usuário", example = "test@email.com")
        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail não pode ser vazio")
        String email,

        @Schema(description = "Senha do usuário")
        @NotBlank(message = "Senha é obrigatória")
        String password
) {
}

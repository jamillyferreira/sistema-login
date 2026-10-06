package com.ferreiradev.sistema_login.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para registro de novo usuário")
public record RegisterRequest(
        @Schema(description = "Nome completo", example = "Jamilly Ferreira")
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Schema(description = "E-mail")
        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail não pode ser vazio")
        String email,

        @Schema(description = "Senha (mín. 6 caracteres)")
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no minímo 6 caracteres")
        String password

) {

}



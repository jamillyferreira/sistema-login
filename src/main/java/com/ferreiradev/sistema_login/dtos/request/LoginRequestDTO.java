package com.ferreiradev.sistema_login.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail não pode ser vazio")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String password
) {
}

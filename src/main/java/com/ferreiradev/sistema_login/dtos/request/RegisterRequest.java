package com.ferreiradev.sistema_login.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail não pode ser vazio")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no minímo 6 caracteres")
        String password

) {

}



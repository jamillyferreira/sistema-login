package com.ferreiradev.sistema_login.dtos.request;

import com.ferreiradev.sistema_login.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail não pode ser vazio")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no minímo 6 caracteres")
        String password

) {

        public User toEntity() {
                User user = new User();
                user.setName(this.name());
                user.setEmail(this.email());
                user.setPassword(this.password());
                return user;
        }

}



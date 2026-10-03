package com.ferreiradev.sistema_login.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ResetPasswordRequest(
        @NotBlank(message = "É obrigatório informar o código recebido no e-mail")
        @Size(min = 6, max = 6, message = "O código deve ter 6 caracteres")
        String code,

        @JsonProperty("new_password")
        @NotBlank(message = "Nova senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no minímo 6 caracteres")
        String newPassword) {
}

package com.ferreiradev.sistema_login.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "Dados para redefinir a senha com o código recebido por e-mail")
public record ResetPasswordRequest(
        @Schema(description = "Código de 6 dígitos recebido por e-mail")
        @NotBlank(message = "É obrigatório informar o código recebido no e-mail")
        @Size(min = 6, max = 6, message = "O código deve ter 6 caracteres")
        String code,

        @Schema(description = "Nova senha do usuário (mín. 6 caracteres)")
        @JsonProperty("new_password")
        @NotBlank(message = "Nova senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no minímo 6 caracteres")
        String newPassword) {
}

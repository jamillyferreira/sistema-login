package com.ferreiradev.sistema_login.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para renovação do token de acesso")
public record RefreshRequest(
        @Schema(description = "Refresh token obtido no login")
        @NotBlank(message = "Refresh token é obrigatório")
        @JsonProperty("refresh_token")
        String refreshToken
) {
}

package com.ferreiradev.sistema_login.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank(message = "Refresh token é obrigatório")
        @JsonProperty("refresh_token")
        String refreshToken
) {
}

package com.ferreiradev.sistema_login.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record LoginResponseDTO(
        @JsonProperty("access_token")
        String accessToken,

        String name,
        String email,

        @JsonProperty("expires_in")
        long expiresIn

) {
}

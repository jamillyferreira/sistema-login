package com.ferreiradev.sistema_login.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Resposta com tokens de autenticação")
public record AuthResponse(

        @JsonProperty("access_token")
        String accessToken,

        @JsonIgnore()
        String refreshToken,

        @JsonProperty("token_type")
        String tokenType,

        @JsonProperty("expires_in")
        long expiresIn

) {
}

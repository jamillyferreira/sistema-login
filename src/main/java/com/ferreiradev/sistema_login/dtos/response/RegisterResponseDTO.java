package com.ferreiradev.sistema_login.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record RegisterResponseDTO(
        UUID id,
        String name,
        String email,

        @JsonProperty("created_at")
        Instant createdAt
) {

}

package com.ferreiradev.sistema_login.dtos.response;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record RegisterResponseDTO(
        UUID id,
        String name,
        String email,
        Instant createdAt
) {

}

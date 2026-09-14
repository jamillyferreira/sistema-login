package com.ferreiradev.sistema_login.dtos.response;

import lombok.Builder;

@Builder
public record LoginResponseDTO(
        String token,
        String name,
        String email,
        long expiresIn

) {
}

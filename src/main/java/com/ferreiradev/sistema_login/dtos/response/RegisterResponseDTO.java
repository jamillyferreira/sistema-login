package com.ferreiradev.sistema_login.dtos.response;

import com.ferreiradev.sistema_login.model.User;

import java.util.UUID;

public record RegisterResponseDTO(UUID id, String name, String email) {

    public static RegisterResponseDTO toDTO(User user) {
        return new RegisterResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }


}

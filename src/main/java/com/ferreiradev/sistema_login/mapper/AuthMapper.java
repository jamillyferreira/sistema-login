package com.ferreiradev.sistema_login.mapper;

import com.ferreiradev.sistema_login.dtos.request.RegisterRequestDTO;
import com.ferreiradev.sistema_login.dtos.response.LoginResponseDTO;
import com.ferreiradev.sistema_login.dtos.response.RegisterResponseDTO;
import com.ferreiradev.sistema_login.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User toEntity(RegisterRequestDTO request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();
    }

    public RegisterResponseDTO toRegisterResponseDTO(User user) {
        return RegisterResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public LoginResponseDTO toLoginResponseDTO(String token, User user, long expiresIn) {
        return LoginResponseDTO.builder()
                .accessToken(token)
                .name(user.getName())
                .email(user.getEmail())
                .expiresIn(expiresIn)
                .build();
    }
}

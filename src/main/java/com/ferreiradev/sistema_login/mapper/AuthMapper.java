package com.ferreiradev.sistema_login.mapper;

import com.ferreiradev.sistema_login.dtos.request.RegisterRequest;
import com.ferreiradev.sistema_login.dtos.response.UserResponse;
import com.ferreiradev.sistema_login.dtos.response.TokenResponse;
import com.ferreiradev.sistema_login.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User toEntity(RegisterRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();
    }

    public UserResponse toRegisterResponseDTO(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public TokenResponse toTokenResponse(String accessToken, String refreshToken, long expiresIn) {
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .build();

    }
}

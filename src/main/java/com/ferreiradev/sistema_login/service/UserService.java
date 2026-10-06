package com.ferreiradev.sistema_login.service;

import com.ferreiradev.sistema_login.dtos.response.UserResponse;
import com.ferreiradev.sistema_login.mapper.AuthMapper;
import com.ferreiradev.sistema_login.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthMapper authMapper;

    public UserResponse getAuthenticatedUser(User user) {
        log.info("Consulta de usuário autenticado: id={}", user.getId());
        return authMapper.toUserResponseDTO(user);
    }
}

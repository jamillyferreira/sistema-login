package com.ferreiradev.sistema_login.service;

import com.ferreiradev.sistema_login.dtos.request.LoginRequestDTO;
import com.ferreiradev.sistema_login.dtos.request.RegisterRequestDTO;
import com.ferreiradev.sistema_login.dtos.response.RegisterResponseDTO;
import com.ferreiradev.sistema_login.exception.EmailAlreadyExistsException;
import com.ferreiradev.sistema_login.exception.InvalidCredentialException;
import com.ferreiradev.sistema_login.model.User;
import com.ferreiradev.sistema_login.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponseDTO registerUser(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            log.warn("Usuário com email {} já cadastrado", request.email());
            throw new EmailAlreadyExistsException("E-mail já cadastrado no sistema");
        }

        User user = request.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        return RegisterResponseDTO.toDTO(savedUser);
    }

    public RegisterResponseDTO loginUser(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Tentativa de login com e-mail não cadastrado: {}", request.email());
                    return new InvalidCredentialException("E-mail ou senha inválidos");
                });

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Tentativa de login com senha incorreta para o e-mail: {}", request.email());
            throw new InvalidCredentialException("E-mail ou senha inválidos");
        }

        return RegisterResponseDTO.toDTO(user);
    }
}

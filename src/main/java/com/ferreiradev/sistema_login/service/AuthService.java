package com.ferreiradev.sistema_login.service;

import com.ferreiradev.sistema_login.dtos.request.LoginRequest;
import com.ferreiradev.sistema_login.dtos.request.RefreshRequest;
import com.ferreiradev.sistema_login.dtos.request.RegisterRequest;
import com.ferreiradev.sistema_login.dtos.response.UserResponse;
import com.ferreiradev.sistema_login.dtos.response.TokenResponse;
import com.ferreiradev.sistema_login.exception.EmailAlreadyExistsException;
import com.ferreiradev.sistema_login.mapper.AuthMapper;
import com.ferreiradev.sistema_login.model.RefreshToken;
import com.ferreiradev.sistema_login.model.User;
import com.ferreiradev.sistema_login.repository.UserRepository;
import com.ferreiradev.sistema_login.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final RefreshTokenService refreshTokenService;

    public UserResponse register(RegisterRequest request) {
        log.info("Iniciando tentativa de registro: email={}", request.email());
        if (userRepository.existsByEmail(request.email())) {
            log.warn("Tentativa de registro com email já cadastrado: {}", request.email());
            throw new EmailAlreadyExistsException("E-mail já cadastrado no sistema");
        }

        User user = authMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        log.info("Usuário registrado com sucesso. id={}, email={}", savedUser.getId(), savedUser.getEmail());

        return authMapper.toRegisterResponseDTO(savedUser);
    }

    public TokenResponse login(LoginRequest request) {
        log.info("Tentativa de login: email={}", request.email());

       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(request.email(), request.password())
       );

        User user = userRepository.findByEmail(authentication.getName())
               .orElseThrow(() -> {
                   log.error("Usuário autenticado mas não encontrado no banco: {}", authentication.getName());
                   return new UsernameNotFoundException("Usuário não encontrado no banco");
               });

       String token = jwtService.generateToken(user);
       String refreshToken = refreshTokenService.generateRefreshToken(user);
       long expiresIn = jwtService.getExpirationInSeconds();

       log.info("Login bem-sucedido: id={}, username={}", user.getId(), user.getEmail());
       return authMapper.toTokenResponse(token, refreshToken, expiresIn);
    }


    public TokenResponse refresh(RefreshRequest request) {
        log.info("Tentativa de refresh token");

        // busca refresh token antigo
        RefreshToken oldRefreshToken = refreshTokenService.validateRefreshToken(request.refreshToken());

        // pega usuario do refresh token
        User user = oldRefreshToken.getUser();

        // torna o refresh token revogado (nao sera possivel usar de novo)
        refreshTokenService.revokeRefreshToken(oldRefreshToken);

        // gera novo acess token e refresh token com novo expiracao
        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = refreshTokenService.generateRefreshToken(user);
        long expiresIn = jwtService.getExpirationInSeconds();

        log.info("Refresh token bem-sucedido: id={}, username={}", user.getId(), user.getEmail());
        return authMapper.toTokenResponse(newAccessToken, newRefreshToken, expiresIn);
    }


    public void logout (RefreshRequest request) {
        log.info("Tentativa de logout");

        RefreshToken token = refreshTokenService.validateRefreshToken(request.refreshToken());
        refreshTokenService.revokeRefreshToken(token);
        log.info("Logout bem-sucedido: username={}", token.getUser().getName());
    }


}

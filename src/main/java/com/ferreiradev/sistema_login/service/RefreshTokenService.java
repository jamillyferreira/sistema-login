package com.ferreiradev.sistema_login.service;

import com.ferreiradev.sistema_login.exception.InvalidRefreshTokenException;
import com.ferreiradev.sistema_login.model.RefreshToken;
import com.ferreiradev.sistema_login.model.User;
import com.ferreiradev.sistema_login.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    // gerar refresh token
    public String generateRefreshToken(User user) {
        String rawToken = generateRandomToken(); // token puro
        String hashedToken = hashToken(rawToken); // token hasheia, nao puro que vai pro banco

        RefreshToken refreshToken = RefreshToken.builder() // cria objeto refresh token
                .tokenHash(hashedToken)
                .user(user)
                .expiresAt(Instant.now().plusMillis(refreshTokenExpiration))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    // validar refresh token
    public RefreshToken validateRefreshToken(String rawToken) {
        String hashedToken = hashToken(rawToken);

        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(hashedToken)
                .orElseThrow(() -> {
                    log.error("Refresh token inválido");
                    return new InvalidRefreshTokenException("Refresh token inválido");
                });

        if (refreshToken.isRevoked()) throw new InvalidRefreshTokenException("Refresh token revogado");

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidRefreshTokenException("Refresh token expirado");
        }

        return refreshToken;
    }

    // revogar refresh token
    public void revokeRefreshToken(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    // criar token aleatorio
    private String generateRandomToken() {
        return UUID.randomUUID().toString();
    }

    // gerar hash do token
    private String hashToken(String token) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo de hash não encontrado", e);
        }
    }


}

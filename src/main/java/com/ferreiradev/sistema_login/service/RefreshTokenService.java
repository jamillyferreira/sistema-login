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
import java.util.Optional;
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
        return findValidRefreshToken(rawToken).orElseThrow(() -> {
            log.warn("Refresh token inválido ou expirado");
            return new InvalidRefreshTokenException("Refresh token inválido ou expirado");
        });
    }

    // revogar refresh token
    public void revokeRefreshToken(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    public int revokeAllForUser(User user) {
        int revoked = refreshTokenRepository.revokeAllByUserId(user.getId());
        log.info("Regovados {} refresh tokens do usuário {}", revoked, user.getId());
        return revoked;
    }

    public Optional<RefreshToken> findValidRefreshToken(String rawToken) {
        String hashedToken = hashToken(rawToken);
        return refreshTokenRepository.findByTokenHash(hashedToken)
                .filter(t -> !t.isRevoked())
                .filter(t -> t.getExpiresAt().isAfter(Instant.now()));
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

package com.ferreiradev.sistema_login.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CookieService {
    public static final String REFRESH_TOKEN_NAME = "refresh_token"; // é o nome do cookie, que vai no Set-Cookie
    private static final String COOKIE_PATH = "/api/auth"; // o cookie só viaja nas rotas de auth

    private final long refreshExpirationMs; // Expiracao do refresh em millisegundos
    private final boolean secure;

    public CookieService(
            @Value("${jwt.refresh-expiration}") long refreshExpirationMs,
            @Value("${app.cookie.secure}") boolean secure) {
        this.refreshExpirationMs = refreshExpirationMs;
        this.secure = secure;
    }

    // cria o refresh token, passando o valor do refresh token e a duracao
    public ResponseCookie createRefreshCookie(String refreshToken) {
        return buildCookie(refreshToken, Duration.ofMillis(refreshExpirationMs));
    }

    public ResponseCookie clearRefreshCookie() {
        return buildCookie("", Duration.ZERO);
    }

    private ResponseCookie buildCookie(String value, Duration maxAge) {
        return ResponseCookie.from(REFRESH_TOKEN_NAME, value)
                .httpOnly(true)
                .secure(secure)
                .sameSite("Strict")
                .path(COOKIE_PATH)
                .maxAge(maxAge)
                .build();

    }


}

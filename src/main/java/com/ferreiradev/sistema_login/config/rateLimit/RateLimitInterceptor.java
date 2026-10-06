package com.ferreiradev.sistema_login.config.rateLimit;

import com.ferreiradev.sistema_login.exception.RateLimitExceededException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;

@Component
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {
    // Configuração de limite por rota.
    // capacity = número máximo de requisições na janela
    // window = período em que as requisições são contadas
    private record RateLimit(int capacity, Duration window) {}
    private static final RateLimit DEFAULT_LIMIT = new RateLimit(30, Duration.ofMinutes(1));

    private static final Map<String, RateLimit> LIMITS = Map.of(
            "/api/auth/login", new RateLimit(10, Duration.ofMinutes(5)),
            "/api/auth/forgot-password", new RateLimit(3, Duration.ofMinutes(15)),
            "/api/auth/reset-password", new RateLimit(5, Duration.ofMinutes(15))
    );


    // Buckets expiram após 1h sem acesso, isso evita memory leak
    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofHours(1))
            .maximumSize(10_000)
            .build();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        String ip = request.getRemoteAddr();
        String key = ip + ":" + path;

        Bucket bucket = buckets.get(key, k -> newBucketFor(path));

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) return true;

        double seconds = probe.getNanosToWaitForRefill() / 1_000_000_000.0;
        long waitSeconds = Math.max(1, (long) Math.ceil(seconds));

        log.warn("Rate limit excedido: ip={} path={} retryAfter={}s",
                request.getRemoteAddr(), request.getRequestURI(), waitSeconds);

        throw new RateLimitExceededException(waitSeconds);
    }

    private Bucket newBucketFor(String path) {
        RateLimit rule = LIMITS.getOrDefault(path, DEFAULT_LIMIT);
        Bandwidth limit = Bandwidth.builder()
                .capacity(rule.capacity())
                .refillGreedy(rule.capacity(), rule.window())
                .build();
        return Bucket.builder().addLimit(limit).build();
    }


}

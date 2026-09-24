package com.pharma.consultoria_pharma.security;

import com.pharma.consultoria_pharma.exceptions.RateLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LoginRateLimiter {

    private final Map<String, AttemptWindow> attempts = new HashMap<>();

    @Value("${security.login.max-attempts:5}")
    private int maxAttempts;

    @Value("${security.login.window-ms:900000}")
    private long windowMs;

    public synchronized void check(String key) {
        AttemptWindow window = currentWindow(key);
        if (window.failures >= maxAttempts) {
            throw new RateLimitExceededException("Demasiados intentos de inicio de sesión. Intenta nuevamente más tarde.");
        }
    }

    public synchronized void recordFailure(String key) {
        AttemptWindow window = currentWindow(key);
        window.failures++;
    }

    public synchronized void reset(String key) {
        attempts.remove(key);
    }

    private AttemptWindow currentWindow(String key) {
        long now = System.currentTimeMillis();
        if (attempts.size() > 10000) {
            attempts.entrySet().removeIf(entry -> now - entry.getValue().startedAt >= windowMs);
        }
        AttemptWindow window = attempts.get(key);
        if (window == null || now - window.startedAt >= windowMs) {
            window = new AttemptWindow(now);
            attempts.put(key, window);
        }
        return window;
    }

    private static final class AttemptWindow {
        private final long startedAt;
        private int failures;

        private AttemptWindow(long startedAt) {
            this.startedAt = startedAt;
        }
    }
}

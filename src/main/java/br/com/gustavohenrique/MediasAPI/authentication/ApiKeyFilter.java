package br.com.gustavohenrique.MediasAPI.authentication;

import br.com.gustavohenrique.MediasAPI.exception.StandardError;
import br.com.gustavohenrique.MediasAPI.model.Application;
import br.com.gustavohenrique.MediasAPI.repository.ApplicationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ApiKeyFilter extends OncePerRequestFilter {

    public static final String APP_ATTRIBUTE = "authenticatedApp";
    private static final String API_KEY_HEADER = "X-Api-Key";

    private final ApplicationRepository applicationRepository;
    private final ApiKeyService apiKeyService;
    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<Long, Bucket> buckets = new ConcurrentHashMap<>();

    public ApiKeyFilter(ApplicationRepository applicationRepository,
                        ApiKeyService apiKeyService,
                        ObjectMapper objectMapper) {
        this.applicationRepository = applicationRepository;
        this.apiKeyService = apiKeyService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        return "OPTIONS".equalsIgnoreCase(method)
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/actuator")
                || path.startsWith("/authenticate")
                || (path.equals("/api/v1/users") && "POST".equalsIgnoreCase(method))
                || (path.equals("/api/v1/applications") && "POST".equalsIgnoreCase(method));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String rawKey = request.getHeader(API_KEY_HEADER);
        if (rawKey == null || rawKey.isBlank()) {
            sendError(response, request, HttpStatus.UNAUTHORIZED, "Missing X-Api-Key header");
            return;
        }

        Optional<Application> appOpt = applicationRepository.findByApiKeyHashAndActiveTrue(apiKeyService.hash(rawKey));
        if (appOpt.isEmpty()) {
            sendError(response, request, HttpStatus.UNAUTHORIZED, "Invalid or revoked API key");
            return;
        }

        Application app = appOpt.get();
        Bucket bucket = buckets.computeIfAbsent(app.getId(), id -> buildBucket(app.getRateLimitPerMinute()));

        long remaining = bucket.getAvailableTokens();
        response.setHeader("X-RateLimit-Limit", String.valueOf(app.getRateLimitPerMinute()));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(Math.max(remaining - 1, 0)));

        if (!bucket.tryConsume(1)) {
            response.setHeader("X-RateLimit-Remaining", "0");
            sendError(response, request, HttpStatus.TOO_MANY_REQUESTS,
                    "Rate limit exceeded — max " + app.getRateLimitPerMinute() + " req/min");
            return;
        }

        request.setAttribute(APP_ATTRIBUTE, app);
        filterChain.doFilter(request, response);
    }

    private Bucket buildBucket(int limitPerMinute) {
        return Bucket.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(limitPerMinute)
                        .refillGreedy(limitPerMinute, Duration.ofMinutes(1))
                        .build())
                .build();
    }

    private void sendError(HttpServletResponse response, HttpServletRequest request,
                           HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(),
                new StandardError(status.value(), message, request.getRequestURI()));
    }
}

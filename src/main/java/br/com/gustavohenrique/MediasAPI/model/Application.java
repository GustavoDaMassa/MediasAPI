package br.com.gustavohenrique.MediasAPI.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "api_key_hash", nullable = false, unique = true, length = 64)
    private String apiKeyHash;

    @Column(name = "api_key_prefix", nullable = false, length = 13)
    private String apiKeyPrefix;

    @Setter
    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "rate_limit_per_minute", nullable = false)
    private int rateLimitPerMinute = 60;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public Application(String name, String description, String apiKeyHash, String apiKeyPrefix) {
        this.name = name;
        this.description = description;
        this.apiKeyHash = apiKeyHash;
        this.apiKeyPrefix = apiKeyPrefix;
        this.rateLimitPerMinute = 60;
        this.createdAt = Instant.now();
        this.active = true;
    }

    public void rotateKey(String newHash, String newPrefix) {
        this.apiKeyHash = newHash;
        this.apiKeyPrefix = newPrefix;
    }
}

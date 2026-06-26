CREATE TABLE applications (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                  VARCHAR(100)  NOT NULL,
    description           VARCHAR(500),
    api_key_hash          VARCHAR(64)   NOT NULL UNIQUE,
    api_key_prefix        VARCHAR(13)   NOT NULL,
    active                BOOLEAN       NOT NULL DEFAULT TRUE,
    rate_limit_per_minute INT           NOT NULL DEFAULT 60,
    created_at            DATETIME      NOT NULL
);

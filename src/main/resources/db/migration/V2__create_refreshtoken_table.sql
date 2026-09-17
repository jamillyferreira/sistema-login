CREATE TABLE refresh_tokens (
    id UUID primary key ,
    token_hash VARCHAR(255) NOT NULL UNIQUE ,
    user_id UUID NOT NULL REFERENCES users(id),
    expires_at TIMESTAMP NOT NULL ,
    revoked BOOLEAN  NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
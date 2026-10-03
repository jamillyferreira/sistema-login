-- V4__recreate_password_reset_tokens.sql
DROP TABLE password_reset_token;

-- NOVA ESTILO DE TABELA
CREATE TABLE password_reset_tokens (
    id UUID primary key ,
    token VARCHAR(6) NOT NULL UNIQUE ,
    user_id UUID NOT NULL ,
    expires_at TIMESTAMP NOT NULL ,
    used BOOLEAN  NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_password_reset_tokens_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_password_reset_tokens_user_id ON password_reset_tokens(user_id);
CREATE INDEX idx_password_reset_tokens_expires ON password_reset_tokens(expires_at);
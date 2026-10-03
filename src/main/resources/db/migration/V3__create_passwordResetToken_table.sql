CREATE TABLE password_reset_token (
  id UUID primary key ,
  token_hash VARCHAR(255) NOT NULL UNIQUE ,
  user_id UUID NOT NULL REFERENCES users(id),
  expires_at TIMESTAMP NOT NULL ,
  used BOOLEAN  NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- Breaking change (Module 4): accounts no longer stores an email column.
CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(40) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_accounts_username UNIQUE (username)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_0900_ai_ci;

-- Upgrade step for local databases created before Module 4: drop the removed email
-- column so inserts stop failing with "Field 'email' doesn't have a default value".
-- MySQL has no ALTER TABLE ... DROP COLUMN IF EXISTS, so the statement is chosen
-- from information_schema and only runs when the stale column still exists.
SET @drop_stale_email_column := (
    SELECT IF(COUNT(*) = 0, 'SELECT 1', 'ALTER TABLE accounts DROP COLUMN email')
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = 'accounts'
      AND column_name = 'email'
);
PREPARE drop_stale_email FROM @drop_stale_email_column;
EXECUTE drop_stale_email;
DEALLOCATE PREPARE drop_stale_email;

CREATE TABLE IF NOT EXISTS games (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    genre VARCHAR(120) NOT NULL,
    platform VARCHAR(120) NOT NULL,
    maturity_rating VARCHAR(40) NOT NULL,
    description TEXT NOT NULL,
    thumbnail_image_path VARCHAR(255) NOT NULL,
    availability_status VARCHAR(30) NOT NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_0900_ai_ci;
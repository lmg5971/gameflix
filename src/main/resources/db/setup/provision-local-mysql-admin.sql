CREATE DATABASE IF NOT EXISTS gameflix
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

CREATE USER IF NOT EXISTS 'gameflix'@'localhost'
    IDENTIFIED BY 'gameflix123';

ALTER USER 'gameflix'@'localhost'
    IDENTIFIED BY 'gameflix123';

GRANT ALL PRIVILEGES ON gameflix.*
    TO 'gameflix'@'localhost';
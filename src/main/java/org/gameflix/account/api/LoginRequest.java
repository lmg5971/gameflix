package org.gameflix.account.api;

import jakarta.validation.constraints.NotBlank;

/**
 * JSON body for {@code POST /api/auth/login}
 */
public record LoginRequest(

        @NotBlank(message = "Enter your username.")
        String username,

        @NotBlank(message = "Enter your passphrase.")
        String password) {
}

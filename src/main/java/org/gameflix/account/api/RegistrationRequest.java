package org.gameflix.account.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.gameflix.shared.validation.MaxUtf8Bytes;

/**
 * JSON body for {@code POST /api/auth/register}
 */
public record RegistrationRequest(

        @NotBlank(message = "Choose a username.")
        @Size(min = 3, max = 40, message = "Username must be 3 to 40 characters.")
        @Pattern(regexp = "^[\\p{L}\\p{N}._-]+$", message = "Username may use letters, numbers, periods, underscores, or hyphens.")
        String username,

        @NotBlank(message = "Create a passphrase.")
        @Size(min = 11, max = 72, message = "Passphrase must be 11 to 72 characters.")
        @MaxUtf8Bytes(value = 72, message = "Passphrase is too long.")
        String password) {
}

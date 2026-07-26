package org.gameflix.account.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.gameflix.shared.validation.MaxUtf8Bytes;

public class RegistrationForm {

    @NotBlank(message = "Choose a username")
    @Size(min = 3, max = 40, message = "Username must be 3 to 40 characters")
    @Pattern(regexp = "^[\\p{L}\\p{N}._-]+$", message = "Username may use letters, numbers, periods, underscores, or hyphens")
    private String username = "";

    @NotBlank(message = "Create a passphrase")
    @Size(min = 11, max = 72, message = "Passphrase must be 11 to 72 characters")
    @MaxUtf8Bytes(value = 72, message = "Passphrase is too long")
    private String password = "";

    @NotBlank(message = "Confirm your passphrase")
    private String passwordConfirmation = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
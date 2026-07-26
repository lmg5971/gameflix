package org.gameflix.account.service;

import org.gameflix.account.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

/**
 * Verifies username and passphrase credentials for API login
 */
@Service
public class LoginService {

    private static final int MAX_PASSWORD_BYTES = 72;

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public boolean passwordMatches(String username, String password) {
        if (username == null || username.isBlank() || password == null) {
            return false;
        }
        if (password.getBytes(StandardCharsets.UTF_8).length > MAX_PASSWORD_BYTES) {
            return false;
        }
        return accountRepository.findByUsernameIgnoreCase(username.strip())
                .map(account -> passwordEncoder.matches(password, account.getPasswordHash()))
                .orElse(false);
    }
}

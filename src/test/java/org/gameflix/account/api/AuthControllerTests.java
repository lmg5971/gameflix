package org.gameflix.account.api;

import org.gameflix.account.model.Account;
import org.gameflix.account.model.AccountRole;
import org.gameflix.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void deleteAccounts() {
        accountRepository.deleteAll();
    }

    @Test
    void registerCreatesUserAccountWithHashedPassphraseAndReturns201() throws Exception {
        postJson("/api/auth/register", authJson("johndoe", "super secret passphrase"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"));

        var account = accountRepository.findByUsernameIgnoreCase("johndoe").orElseThrow();
        assertThat(account.getRole()).isEqualTo(AccountRole.USER);
        assertThat(account.getPasswordHash()).isNotEqualTo("super secret passphrase");
        assertThat(passwordEncoder.matches("super secret passphrase", account.getPasswordHash())).isTrue();
    }

    @Test
    void registerRejectsDuplicateUsernameIgnoringCaseWith409() throws Exception {
        saveAccount("johndoe", "super secret passphrase");

        postJson("/api/auth/register", authJson("JOHNDOE", "another secret passphrase"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Username already exists"));

        assertThat(accountRepository.count()).isEqualTo(1);
    }

    @Test
    void registerRejectsTooShortUsernameWith400() throws Exception {
        postJson("/api/auth/register", authJson("ab", "super secret passphrase"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username must be 3 to 40 characters."));

        assertThat(accountRepository.count()).isZero();
    }

    @Test
    void registerRejectsTooShortPassphraseWith400() throws Exception {
        postJson("/api/auth/register", authJson("johndoe", "tooshort"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Passphrase must be 11 to 72 characters."));

        assertThat(accountRepository.count()).isZero();
    }

    @Test
    void registerRejectsMalformedJsonWith400() throws Exception {
        postJson("/api/auth/register", "not valid json")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request body must be valid JSON."));
    }

    @Test
    void loginReturns200ForCorrectCredentials() throws Exception {
        saveAccount("johndoe", "super secret passphrase");

        postJson("/api/auth/login", authJson("johndoe", "super secret passphrase"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void loginMatchesUsernameCaseInsensitively() throws Exception {
        saveAccount("johndoe", "super secret passphrase");

        postJson("/api/auth/login", authJson("JOHNDOE", "super secret passphrase"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void loginRejectsWrongPassphraseWith401() throws Exception {
        saveAccount("johndoe", "super secret passphrase");

        postJson("/api/auth/login", authJson("johndoe", "wrong passphrase"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    void loginRejectsUnknownUsernameWith401() throws Exception {
        postJson("/api/auth/login", authJson("ghost", "super secret passphrase"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    void loginRejectsBlankUsernameWith400() throws Exception {
        postJson("/api/auth/login", authJson("", "super secret passphrase"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Enter your username."));
    }

    private ResultActions postJson(String path, String body) throws Exception {
        return mockMvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    private void saveAccount(String username, String password) {
        accountRepository.save(Account.registeredUser(username, passwordEncoder.encode(password)));
    }

    private static String authJson(String username, String password) {
        return """
                {"username": "%s", "password": "%s"}
                """.formatted(username, password);
    }
}

package org.gameflix.security;

import org.gameflix.account.model.Account;
import org.gameflix.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class SecurityConfigurationTests {

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
    void passwordEncoderIsBcryptAndVerifiesOnlyTheCorrectPassphrase() {
        var encodedPassword = passwordEncoder.encode("correct horse battery staple");

        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
        assertThat(encodedPassword).startsWith("$2");
        assertThat(passwordEncoder.matches("correct horse battery staple", encodedPassword)).isTrue();
        assertThat(passwordEncoder.matches("wrong passphrase", encodedPassword)).isFalse();
    }

    @Test
    void configuredPublicRoutesAllowAnonymousAccess() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(unauthenticated());

        mockMvc.perform(get("/catalog"))
                .andExpect(status().isOk())
                .andExpect(unauthenticated());
    }

    @Test
    void protectedRoutesRedirectAnonymousUsersToCustomLoginPage() throws Exception {
        mockMvc.perform(get("/account"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"))
                .andExpect(unauthenticated());
    }

    @Test
    void loginUsesUsernameAndPasswordParametersAndRedirectsAuthenticatedUsersToAccountPage() throws Exception {
        accountRepository.save(Account.registeredUser(
                "playerone",
                passwordEncoder.encode("correct horse battery staple")));

        mockMvc.perform(post("/login")
                        .with(csrf())
                        .param("username", "PLAYERONE")
                        .param("password", "correct horse battery staple"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account"))
                .andExpect(authenticated().withUsername("playerone"));
    }

    @Test
    void anonymousApiRequestsOutsideAuthEndpointsReturn401WithoutLoginRedirect() throws Exception {
        mockMvc.perform(get("/api/games"))
                .andExpect(status().isUnauthorized())
                .andExpect(unauthenticated());

        mockMvc.perform(post("/api/subscriptions"))
                .andExpect(status().isUnauthorized())
                .andExpect(unauthenticated());
    }

    @Test
    void apiAuthEndpointsAreReachableWithoutCsrfTokenOrSession() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username": "ghost", "password": "correct horse battery staple"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid username or password"))
                .andExpect(unauthenticated());
    }

    @Test
    void loginRejectsMissingCsrfTokenBeforeAuthentication() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "playerone")
                        .param("password", "correct horse battery staple"))
                .andExpect(status().isForbidden())
                .andExpect(unauthenticated());
    }

    @Test
    void logoutClearsAuthenticationAndRedirectsToLoginLogoutFlag() throws Exception {
        mockMvc.perform(post("/logout")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"))
                .andExpect(unauthenticated());
    }
}
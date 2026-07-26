package org.gameflix.security;

import org.gameflix.account.model.Account;
import org.gameflix.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersistedAccountUserDetailsServiceTests {

    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final PersistedAccountUserDetailsService userDetailsService = new PersistedAccountUserDetailsService(accountRepository);

    @Test
    void loadUserByUsernameStripsInputAndMapsPersistedAccountToSpringSecurityUser() {
        var account = Account.registeredUser("playerone", "{noop}passphrase");
        when(accountRepository.findByUsernameIgnoreCase("PlayerOne")).thenReturn(Optional.of(account));

        var userDetails = userDetailsService.loadUserByUsername("  PlayerOne  ");

        assertThat(userDetails.getUsername()).isEqualTo("playerone");
        assertThat(userDetails.getPassword()).isEqualTo("{noop}passphrase");
        assertThat(userDetails.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_USER");
        verify(accountRepository).findByUsernameIgnoreCase("PlayerOne");
    }

    @Test
    void loadUserByUsernameUsesSafeGenericFailureForMissingAccounts() {
        when(accountRepository.findByUsernameIgnoreCase("missing")).thenReturn(Optional.empty());

        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> userDetailsService.loadUserByUsername(" missing "))
                .withMessage("Invalid username or password.");
        verify(accountRepository).findByUsernameIgnoreCase("missing");
    }
}
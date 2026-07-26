package org.gameflix.security;

import org.gameflix.account.model.Account;
import org.gameflix.account.repository.AccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class PersistedAccountUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    PersistedAccountUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        return accountRepository.findByUsernameIgnoreCase(username.strip())
                .map(PersistedAccountUserDetailsService::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));
    }

    private static UserDetails toUserDetails(Account account) {
        return User.withUsername(account.getUsername())
                .password(account.getPasswordHash())
                .roles(account.getRole().name())
                .build();
    }
}
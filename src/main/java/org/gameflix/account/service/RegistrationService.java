package org.gameflix.account.service;

import org.gameflix.account.model.Account;
import org.gameflix.account.repository.AccountRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Account register(AccountRegistration registration) {
        String passwordHash = passwordEncoder.encode(registration.password());
        Account account = Account.registeredUser(registration.username(), passwordHash);
        try {
            return accountRepository.saveAndFlush(account);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateUsernameException(ex);
        }
    }
}
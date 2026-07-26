package org.gameflix.account.repository;

import org.gameflix.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByUsernameIgnoreCase(String username);

    Optional<Account> findByUsernameIgnoreCase(String username);
}
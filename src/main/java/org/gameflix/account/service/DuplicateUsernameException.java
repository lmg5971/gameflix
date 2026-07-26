package org.gameflix.account.service;

public class DuplicateUsernameException extends RuntimeException {

    DuplicateUsernameException(Throwable cause) {
        super("Username already exists", cause);
    }
}

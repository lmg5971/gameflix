package org.gameflix.account.service;

/**
 * Command describing a validated registration request
 */
public record AccountRegistration(String username, String password) {
}

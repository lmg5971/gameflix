package org.gameflix.account.api;

import jakarta.validation.Valid;
import org.gameflix.account.service.AccountRegistration;
import org.gameflix.account.service.LoginService;
import org.gameflix.account.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for account registration and login
 */
@RestController
@RequestMapping("/api/auth")
class AuthController {

    private final RegistrationService registrationService;
    private final LoginService loginService;

    AuthController(RegistrationService registrationService, LoginService loginService) {
        this.registrationService = registrationService;
        this.loginService = loginService;
    }

    @PostMapping("/register")
    ResponseEntity<MessageResponse> register(@Valid @RequestBody RegistrationRequest request) {
        registrationService.register(new AccountRegistration(request.username(), request.password()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("User registered successfully"));
    }

    @PostMapping("/login")
    ResponseEntity<MessageResponse> login(@Valid @RequestBody LoginRequest request) {
        if (loginService.passwordMatches(request.username(), request.password())) {
            return ResponseEntity.ok(new MessageResponse("Login successful"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid username or password"));
    }
}

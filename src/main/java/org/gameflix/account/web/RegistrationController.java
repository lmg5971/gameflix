package org.gameflix.account.web;

import jakarta.validation.Valid;
import org.gameflix.account.service.AccountRegistration;
import org.gameflix.account.service.DuplicateUsernameException;
import org.gameflix.account.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class RegistrationController {

    private final RegistrationService registrationService;

    RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    String showRegistrationForm(Model model) {
        if (!model.containsAttribute("registrationForm")) {
            model.addAttribute("registrationForm", new RegistrationForm());
        }
        return "account/register";
    }

    @PostMapping("/register")
    String register(@Valid @ModelAttribute("registrationForm") RegistrationForm form, BindingResult bindingResult) {
        validatePasswordConfirmation(form, bindingResult);

        if (bindingResult.hasErrors()) {
            return "account/register";
        }

        try {
            registrationService.register(new AccountRegistration(form.getUsername(), form.getPassword()));
        } catch (DuplicateUsernameException ex) {
            rejectDuplicateUsername(bindingResult);
            return "account/register";
        }
        return "redirect:/register?success";
    }

    private static void rejectDuplicateUsername(BindingResult bindingResult) {
        bindingResult.rejectValue("username", "username.duplicate", "That username is already taken.");
    }

    private static void validatePasswordConfirmation(RegistrationForm form, BindingResult bindingResult) {
        if (form.getPassword() != null && form.getPasswordConfirmation() != null
                && !form.getPassword().equals(form.getPasswordConfirmation())) {
            bindingResult.rejectValue("passwordConfirmation", "passwordConfirmation.mismatch", "Passphrases must match.");
        }
    }
}
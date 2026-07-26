package org.gameflix.account.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class AccountController {

    @GetMapping("/account")
    String showAccountHome(Authentication authentication, Model model) {
        model.addAttribute("accountUsername", authentication.getName());
        return "account/home";
    }
}
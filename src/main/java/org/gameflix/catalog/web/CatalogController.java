package org.gameflix.catalog.web;

import jakarta.servlet.http.HttpServletResponse;
import org.gameflix.catalog.service.CatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/catalog")
class CatalogController {

    private final CatalogService catalogService;

    CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping
    String list(Model model) {
        model.addAttribute("games", catalogService.listPublicGames());
        return "catalog/list";
    }

    @GetMapping("/{id}")
    String detail(@PathVariable Long id, Model model, HttpServletResponse response) {
        return catalogService.findPublicGame(id)
                .map(game -> {
                    model.addAttribute("game", game);
                    return "catalog/detail";
                })
                .orElseGet(() -> {
                    response.setStatus(HttpStatus.NOT_FOUND.value());
                    return "catalog/not-found";
                });
    }
}
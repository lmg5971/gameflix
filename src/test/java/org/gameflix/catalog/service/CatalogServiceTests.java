package org.gameflix.catalog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.gameflix.catalog.model.Game;
import org.gameflix.catalog.model.GameAvailabilityStatus;
import org.gameflix.catalog.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CatalogServiceTests {

    private final CatalogService catalogService;
    private final GameRepository gameRepository;

    @Autowired
    CatalogServiceTests(CatalogService catalogService, GameRepository gameRepository) {
        this.catalogService = catalogService;
        this.gameRepository = gameRepository;
    }

    @BeforeEach
    void deleteGames() {
        gameRepository.deleteAll();
    }

    @Test
    void listPublicGamesReturnsEmptyListWhenCatalogIsEmpty() {
        assertThat(catalogService.listPublicGames()).isEmpty();
    }

    @Test
    void listPublicGamesReturnsOnlyActiveGamesSortedByTitle() {
        gameRepository.save(new Game("Zelda Builder", "Adventure", "Switch", "E10+",
                "A creative adventure available to public visitors.", "/images/catalog/default.jpg",
                GameAvailabilityStatus.AVAILABLE, true));
        gameRepository.save(new Game("Arcade Preview", "Arcade", "PC", "E",
                "A public coming-soon title.", "/images/catalog/default.jpg",
                GameAvailabilityStatus.COMING_SOON, true));
        gameRepository.save(new Game("Hidden Admin Draft", "Strategy", "PC", "T",
                "Inactive records must not appear in the public catalog.", "/images/catalog/default.jpg",
                GameAvailabilityStatus.UNAVAILABLE, false));

        var publicGames = catalogService.listPublicGames();

        assertThat(publicGames)
                .extracting(Game::getTitle)
                .containsExactly("Arcade Preview", "Zelda Builder");
        assertThat(publicGames)
                .extracting(Game::getAvailabilityStatus)
                .containsExactly(GameAvailabilityStatus.COMING_SOON, GameAvailabilityStatus.AVAILABLE);
    }

    @Test
    void findPublicGameReturnsActiveGameAndTreatsInactiveOrMissingGamesAsNotFound() {
        var activeGame = gameRepository.save(new Game("Public Quest", "Role-playing", "PC", "T",
                "A visible public catalog title.", "/images/catalog/default.jpg",
                GameAvailabilityStatus.UNAVAILABLE, true));
        var inactiveGame = gameRepository.save(new Game("Retired Quest", "Role-playing", "PC", "T",
                "A hidden retired catalog title.", "/images/catalog/default.jpg",
                GameAvailabilityStatus.UNAVAILABLE, false));

        assertThat(catalogService.findPublicGame(activeGame.getId()))
                .hasValueSatisfying(game -> assertThat(game.getTitle()).isEqualTo("Public Quest"));
        assertThat(catalogService.findPublicGame(inactiveGame.getId())).isEmpty();
        assertThat(catalogService.findPublicGame(999_999L)).isEmpty();
    }
}
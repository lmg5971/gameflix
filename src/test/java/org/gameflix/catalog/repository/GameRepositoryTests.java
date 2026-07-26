package org.gameflix.catalog.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.gameflix.catalog.model.Game;
import org.gameflix.catalog.model.GameAvailabilityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GameRepositoryTests {

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    void deleteGames() {
        gameRepository.deleteAll();
    }

    @Test
    void activePublicCatalogQueryIncludesAllPublicStatusesAndExcludesInactiveGames() {
        gameRepository.save(new Game("Available Quest", "Adventure", "PC", "E10+",
                "An available public catalog record.", "/images/catalog/default.jpg",
                GameAvailabilityStatus.AVAILABLE, true));
        gameRepository.save(new Game("Unavailable Quest", "Adventure", "PC", "E10+",
                "An unavailable public catalog record.", "/images/catalog/default.jpg",
                GameAvailabilityStatus.UNAVAILABLE, true));
        gameRepository.save(new Game("Coming Soon Quest", "Adventure", "PC", "E10+",
                "A coming-soon public catalog record.", "/images/catalog/default.jpg",
                GameAvailabilityStatus.COMING_SOON, true));
        gameRepository.save(new Game("Inactive Quest", "Adventure", "PC", "E10+",
                "An inactive catalog record.", "/images/catalog/default.jpg",
                GameAvailabilityStatus.AVAILABLE, false));

        var publicGames = gameRepository.findByActiveTrueOrderByTitleAsc();

        assertThat(publicGames)
                .extracting(Game::getTitle)
                .containsExactly("Available Quest", "Coming Soon Quest", "Unavailable Quest");
        assertThat(publicGames)
                .extracting(Game::getAvailabilityStatus)
                .containsExactly(
                        GameAvailabilityStatus.AVAILABLE,
                        GameAvailabilityStatus.COMING_SOON,
                        GameAvailabilityStatus.UNAVAILABLE);
    }

    @Test
    void activeDetailQueryDoesNotReturnInactiveGame() {
        var publicGame = gameRepository.save(new Game("Public Detail", "Puzzle", "Switch", "E",
                "A public detail record.", "/images/catalog/default.jpg",
                GameAvailabilityStatus.AVAILABLE, true));
        var inactiveGame = gameRepository.save(new Game("Inactive Detail", "Puzzle", "Switch", "E",
                "An inactive detail record.", "/images/catalog/default.jpg",
                GameAvailabilityStatus.AVAILABLE, false));

        assertThat(gameRepository.findByIdAndActiveTrue(publicGame.getId()))
                .hasValueSatisfying(game -> assertThat(game.getTitle()).isEqualTo("Public Detail"));
        assertThat(gameRepository.findByIdAndActiveTrue(inactiveGame.getId())).isEmpty();
    }
}
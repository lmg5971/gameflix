package org.gameflix.catalog.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.gameflix.catalog.model.Game;
import org.gameflix.catalog.model.GameAvailabilityStatus;
import org.gameflix.catalog.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@SpringBootTest
class CatalogControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    void deleteGames() {
        gameRepository.deleteAll();
    }

    @Test
    void catalogListIsPublicAndShowsEmptyState() throws Exception {
        mockMvc.perform(get("/catalog"))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/list"))
                .andExpect(content().string(containsString("Stream the games you love")))
                .andExpect(content().string(containsString("Browse titles")))
                .andExpect(content().string(containsString("No public catalog titles are available yet.")));
    }

    @Test
    void catalogListShowsActiveGamesAndStatusLabelsForUnavailableAndComingSoonEdgeCases() throws Exception {
        gameRepository.save(new Game("Offline Arena", "Fighting", "PC", "T",
                "This public catalog title is temporarily unavailable.", "/images/catalog/counter_strike_2.jpg",
                GameAvailabilityStatus.UNAVAILABLE, true));
        gameRepository.save(new Game("Future Racer", "Racing", "Xbox", "E",
                "This public catalog title is coming soon.", "/images/catalog/default.jpg",
                GameAvailabilityStatus.COMING_SOON, true));
        gameRepository.save(new Game("Private Prototype", "Puzzle", "PC", "E",
                "Inactive records are excluded from public browsing.", "/images/catalog/default.jpg",
                GameAvailabilityStatus.AVAILABLE, false));

        mockMvc.perform(get("/catalog"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Offline Arena")))
                .andExpect(content().string(containsString("/images/catalog/counter_strike_2.jpg")))
                .andExpect(content().string(containsString("Offline Arena catalog thumbnail")))
                .andExpect(content().string(containsString("height=\"87\"")))
                .andExpect(content().string(containsString("loading=\"lazy\"")))
                .andExpect(content().string(containsString("decoding=\"async\"")))
                .andExpect(content().string(not(containsString("fetchpriority=\"high\""))))
                .andExpect(content().string(containsString("Unavailable")))
                .andExpect(content().string(containsString("Future Racer")))
                .andExpect(content().string(containsString("Coming soon")))
                .andExpect(content().string(not(containsString("Private Prototype"))));
    }

    @Test
    void catalogDetailIsPublicAndShowsGameMetadata() throws Exception {
        var game = gameRepository.save(new Game("Sky Library", "Adventure", "PC", "E10+",
                "A public detail page should show the full catalog description.", "/images/catalog/marvel_rivals.jpg",
                GameAvailabilityStatus.AVAILABLE, true));

        mockMvc.perform(get("/catalog/{id}", game.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("catalog/detail"))
                .andExpect(content().string(containsString("Sky Library")))
                .andExpect(content().string(containsString("/images/catalog/marvel_rivals.jpg")))
                .andExpect(content().string(containsString("Sky Library catalog thumbnail")))
                .andExpect(content().string(containsString("height=\"87\"")))
                .andExpect(content().string(containsString("loading=\"eager\"")))
                .andExpect(content().string(containsString("fetchpriority=\"high\"")))
                .andExpect(content().string(not(containsString("loading=\"lazy\""))))
                .andExpect(content().string(containsString("Adventure")))
                .andExpect(content().string(containsString("PC")))
                .andExpect(content().string(containsString("E10+")))
                .andExpect(content().string(containsString("Available")))
                .andExpect(content().string(containsString("A public detail page should show the full catalog description.")));
    }

    @Test
    void catalogThumbnailStaticResourceIsServedAsJpeg() throws Exception {
        mockMvc.perform(get("/images/catalog/counter_strike_2.jpg"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("image/jpeg")));
    }

    @Test
    void inactiveOrMissingCatalogDetailUsesSafeNotFoundPageWithoutStackTrace() throws Exception {
        var inactiveGame = gameRepository.save(new Game("Hidden Detail", "Stealth", "PC", "T",
                "This title is inactive and should be hidden.", "/images/catalog/default.jpg",
                GameAvailabilityStatus.UNAVAILABLE, false));

        mockMvc.perform(get("/catalog/{id}", inactiveGame.getId()))
                .andExpect(status().isNotFound())
                .andExpect(view().name("catalog/not-found"))
                .andExpect(content().string(containsString("Catalog title not found")))
                .andExpect(content().string(not(containsString("Exception"))))
                .andExpect(content().string(not(containsString("Stacktrace"))));

        mockMvc.perform(get("/catalog/{id}", 999_999L))
                .andExpect(status().isNotFound())
                .andExpect(view().name("catalog/not-found"))
                .andExpect(content().string(containsString("Catalog title not found")));
    }
}
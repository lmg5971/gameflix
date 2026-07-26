package org.gameflix.catalog.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class GameTests {

    private static Game activeGame() {
        return new Game("  Apex Legends  ", "  Action  ", "  PC  ", "  Teen  ",
                "  Squad-based battle royale.  ", "  /images/catalog/apex_legends.jpg  ",
                GameAvailabilityStatus.AVAILABLE, true);
    }

    @Test
    void constructorStripsTextFields() {
        var game = activeGame();

        assertThat(game.getTitle()).isEqualTo("Apex Legends");
        assertThat(game.getGenre()).isEqualTo("Action");
        assertThat(game.getPlatform()).isEqualTo("PC");
        assertThat(game.getMaturityRating()).isEqualTo("Teen");
        assertThat(game.getDescription()).isEqualTo("Squad-based battle royale.");
        assertThat(game.getThumbnailImagePath()).isEqualTo("/images/catalog/apex_legends.jpg");
        assertThat(game.getAvailabilityStatus()).isEqualTo(GameAvailabilityStatus.AVAILABLE);
        assertThat(game.isActive()).isTrue();
        assertThat(game.getId()).isNull();
        assertThat(game.getCreatedAt()).isNull();
        assertThat(game.getUpdatedAt()).isNull();
    }

    @Test
    void inactiveGameKeepsInactiveFlagForAdminOnlyRecords() {
        var game = new Game("Prototype", "Puzzle", "Switch", "E", "Internal-only prototype.",
                "/images/catalog/default.jpg", GameAvailabilityStatus.COMING_SOON, false);

        assertThat(game.isActive()).isFalse();
        assertThat(game.getAvailabilityStatus()).isEqualTo(GameAvailabilityStatus.COMING_SOON);
    }

    @Test
    void availabilityStatusesProvideLabelsAndBootstrapBadgeClassesForEveryStatus() {
        assertThat(GameAvailabilityStatus.AVAILABLE.getLabel()).isEqualTo("Available");
        assertThat(GameAvailabilityStatus.AVAILABLE.getBadgeClass()).isEqualTo("text-bg-success");
        assertThat(GameAvailabilityStatus.UNAVAILABLE.getLabel()).isEqualTo("Unavailable");
        assertThat(GameAvailabilityStatus.UNAVAILABLE.getBadgeClass()).isEqualTo("text-bg-secondary");
        assertThat(GameAvailabilityStatus.COMING_SOON.getLabel()).isEqualTo("Coming soon");
        assertThat(GameAvailabilityStatus.COMING_SOON.getBadgeClass()).isEqualTo("text-bg-info");
    }

    @Test
    void recordCreationTimeStampsCreatedAtAndUpdatedAtWhenUnset() {
        var game = activeGame();

        var before = Instant.now();
        game.recordCreationTime();
        var after = Instant.now();

        assertThat(game.getCreatedAt()).isBetween(before, after);
        assertThat(game.getUpdatedAt()).isBetween(before, after);
    }

    @Test
    void recordCreationTimeDoesNotOverwriteExistingTimestamps() {
        var game = activeGame();

        game.recordCreationTime();
        var createdAt = game.getCreatedAt();
        var updatedAt = game.getUpdatedAt();
        game.recordCreationTime();

        assertThat(game.getCreatedAt()).isEqualTo(createdAt);
        assertThat(game.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void recordUpdateTimeRefreshesUpdatedAtWithoutChangingCreatedAt() {
        var game = activeGame();
        game.recordCreationTime();
        var createdAt = game.getCreatedAt();
        var previousUpdatedAt = game.getUpdatedAt();

        game.recordUpdateTime();

        assertThat(game.getCreatedAt()).isEqualTo(createdAt);
        assertThat(game.getUpdatedAt()).isAfterOrEqualTo(previousUpdatedAt);
    }

    @Test
    void protectedNoArgConstructorIsPresentForJpa() {
        var game = new Game();

        assertThat(game.getTitle()).isNull();
        assertThat(game.getAvailabilityStatus()).isNull();
        assertThat(game.isActive()).isFalse();
    }
}

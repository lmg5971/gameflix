package org.gameflix.catalog.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 120)
    private String genre;

    @Column(nullable = false, length = 120)
    private String platform;

    @Column(name = "maturity_rating", nullable = false, length = 40)
    private String maturityRating;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "thumbnail_image_path", nullable = false, length = 255)
    private String thumbnailImagePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability_status", nullable = false, length = 30)
    private GameAvailabilityStatus availabilityStatus;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Game() {
    }

    public Game(String title, String genre, String platform, String maturityRating, String description,
                String thumbnailImagePath, GameAvailabilityStatus availabilityStatus, boolean active) {
        this.title = title.strip();
        this.genre = genre.strip();
        this.platform = platform.strip();
        this.maturityRating = maturityRating.strip();
        this.description = description.strip();
        this.thumbnailImagePath = thumbnailImagePath.strip();
        this.availabilityStatus = availabilityStatus;
        this.active = active;
    }

    @PrePersist
    void recordCreationTime() {
        var now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    void recordUpdateTime() {
        updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getPlatform() {
        return platform;
    }

    public String getMaturityRating() {
        return maturityRating;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailImagePath() {
        return thumbnailImagePath;
    }

    public GameAvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
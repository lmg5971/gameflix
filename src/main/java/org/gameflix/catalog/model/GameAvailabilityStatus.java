package org.gameflix.catalog.model;

public enum GameAvailabilityStatus {
    AVAILABLE("Available", "text-bg-success"),
    UNAVAILABLE("Unavailable", "text-bg-secondary"),
    COMING_SOON("Coming soon", "text-bg-info");

    private final String label;
    private final String badgeClass;

    GameAvailabilityStatus(String label, String badgeClass) {
        this.label = label;
        this.badgeClass = badgeClass;
    }

    public String getLabel() {
        return label;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}

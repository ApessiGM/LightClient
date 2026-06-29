package dev.lightclient.module;

/** Top level grouping for modules shown as panels in the ClickGUI. */
public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    HUD("HUD"),
    UTILITY("Utility"),
    PLAYER("Player"),
    MISC("Misc"),
    COSMETICS("Cosmetics"),
    PERFORMANCE("Performance");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

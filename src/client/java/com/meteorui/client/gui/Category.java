package com.meteorui.client.gui;

import com.meteorui.client.util.ColorPalette;

public enum Category {
    COMBAT("Combat", ColorPalette.CAT_COMBAT),
    MOVEMENT("Movement", ColorPalette.CAT_MOVEMENT),
    RENDER("Render", ColorPalette.CAT_RENDER),
    PLAYER("Player", ColorPalette.CAT_PLAYER),
    WORLD("World", ColorPalette.CAT_WORLD),
    MISC("Misc", ColorPalette.CAT_MISC);

    private final String displayName;
    private final int color;

    Category(String displayName, int color) {
        this.displayName = displayName;
        this.color = color;
    }

    public String getDisplayName() { return displayName; }
    public int getColor() { return color; }
}

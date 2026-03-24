package com.meteorui.client.module;

import com.meteorui.client.gui.Category;

public abstract class Module {
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
    }

    public void toggle() {
        if (enabled) disable(); else enable();
    }

    public void enable() { this.enabled = true; onEnable(); }
    public void disable() { this.enabled = false; onDisable(); }

    protected void onEnable() {}
    protected void onDisable() {}

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
}

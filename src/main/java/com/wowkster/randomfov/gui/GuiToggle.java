package com.wowkster.randomfov.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiToggle extends GuiButton {
    private boolean state;
    private final GuiDisplaySupplier displaySupplier;
    private final ToggleValueCallback updateCallback;
    private final String title;

    interface GuiDisplaySupplier {
        String getDisplayString(boolean state);
    }

    interface ToggleValueCallback {
        void callback(boolean sliderValue);
    }

    public GuiToggle(int id, String title, int xPos, int yPos, boolean defaultValue, GuiDisplaySupplier displaySupplier, ToggleValueCallback updateCallback) {
        super(id, xPos, yPos, 150, 20, title);

        this.state = defaultValue;

        this.displaySupplier = displaySupplier;
        this.updateCallback = updateCallback;

        this.title = title;
        this.displayString = getDisplayString();
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (!super.mousePressed(mc, mouseX, mouseY)) return false;

        state = !state;
        updateCallback.callback(state);
        this.displayString = getDisplayString();

        return true;
    }

    private String getDisplayString() {
        return title + ": " + this.displaySupplier.getDisplayString(state);
    }
}

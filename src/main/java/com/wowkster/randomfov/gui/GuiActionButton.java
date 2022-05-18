package com.wowkster.randomfov.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiActionButton extends GuiButton {
    interface ButtonAction {
        void execute();
    }

    ButtonAction action;

    public GuiActionButton(int id, String displayString, int xPos, int yPos, ButtonAction action) {
        super(id, xPos, yPos, 150, 20, displayString);

        this.action = action;
        this.displayString = displayString;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (!super.mousePressed(mc, mouseX, mouseY)) return false;

        action.execute();

        return true;
    }
}

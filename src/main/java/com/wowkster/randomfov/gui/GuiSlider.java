package com.wowkster.randomfov.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public class GuiSlider extends GuiButton {
    private float sliderValue;
    public boolean dragging;

    private final float maxValue;
    private final float minValue;
    private final float step;
    String title;
    private final GuiDisplaySupplier displaySupplier;
    private final SliderValueCallback updateCallback;

    public interface GuiDisplaySupplier {
        String getDisplayString(float denormalizedValue);
    }

    interface SliderValueCallback {
        void callback(float sliderValue);
    }

    public GuiSlider(int id, String title, int xPos, int yPos, float defaultValue, float minValue, float maxValue, float step, GuiDisplaySupplier displaySupplier, SliderValueCallback updateCallback) {
        super(id, xPos, yPos, 150, 20, "");
        this.title = title;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.step = step;
        this.displaySupplier = displaySupplier;

        this.sliderValue = normalizeValue(defaultValue);
        this.updateCallback = updateCallback;
        this.displayString = getDisplayString();
    }

    public void setValue(float value) {
        this.sliderValue = normalizeValue(value);
        this.displayString = getDisplayString();
    }

    public String getDisplayString() {
        return title + ": " + displaySupplier.getDisplayString(denormalizeValue(this.sliderValue));
    }

    protected int getHoverState(boolean p_getHoverState_1_) {
        return 0;
    }

    protected void mouseDragged(Minecraft mc, int dx, int unknown) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (float) (dx - (this.xPosition + 4)) / (float) (this.width - 8);
                this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
                this.updateCallback.callback(this.denormalizeValue(this.sliderValue));
                this.displayString = getDisplayString();
            }

            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
            this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
            this.updateCallback.callback(this.denormalizeValue(this.sliderValue));
            this.displayString = getDisplayString();
            this.dragging = true;
            return true;
        } else {
            return false;
        }
    }

    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }

    private float normalizeValue(float value) {
        return MathHelper.clamp_float((this.snapToStepClamp(value) - this.minValue) / (this.maxValue - this.minValue), 0.0F, 1.0F);
    }

    private float denormalizeValue(float value) {
        return this.snapToStepClamp(this.minValue + (this.maxValue - this.minValue) * MathHelper.clamp_float(value, 0.0F, 1.0F));
    }

    private float snapToStepClamp(float value) {
        value = this.snapToStep(value);
        return MathHelper.clamp_float(value, this.minValue, this.maxValue);
    }

    private float snapToStep(float value) {
        if (this.step > 0.0F) {
            value = this.step * (float) Math.round(value / this.step);
        }

        return value;
    }
}


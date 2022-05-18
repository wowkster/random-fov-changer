package com.wowkster.randomfov.gui;

import com.wowkster.randomfov.RandomFOVChanger;
import net.minecraft.client.gui.GuiScreen;

public class GuiModOptions extends GuiScreen {
    public static final GuiSlider.GuiDisplaySupplier timeSupplier = (value) -> {
        int rawSeconds = (int) value;

        int seconds = rawSeconds % 60;
        int minutes = rawSeconds / 60;

        String res = "";

        if (minutes > 0) {
            res += String.format("%dm", minutes);
        }

        if (seconds > 0) {
            res += String.format("%s%ds", minutes > 0 ? " " : "", seconds);
        }

        return res;
    };

    public GuiModOptions() {

    }

    @Override
    public void initGui() {
        super.initGui();

        this.buttonList.add(new GuiSlider(0, "Time Interval", this.width / 2 - 155, this.height / 6, RandomFOVChanger.timeInterval, 15, 60 * 5, 15,
                timeSupplier, sliderValue -> RandomFOVChanger.getInstance().setTimeInterval((int)sliderValue)));

        this.buttonList.add(new GuiToggle(1, "Enable Timer", this.width / 2 + 5, this.height / 6, RandomFOVChanger.getInstance().getEnabled(),
                (state) -> state ? "§aON" : "§cOFF", (state) -> RandomFOVChanger.getInstance().setEnabled(state)));

        this.buttonList.add(new GuiActionButton(2, "Randomize FOV", this.width / 2 - 155, this.height / 6 + (24), () -> {
           RandomFOVChanger.getInstance().randomizeResourcePack();
        }));

        this.buttonList.add(new GuiToggle(1, "Show Debug Info", this.width / 2 - 155, this.height / 6 + (24 * 3), RandomFOVChanger.showDebugHud,
                (state) -> state ? "§aON" : "§cOFF", (state) -> RandomFOVChanger.showDebugHud = state));

        this.buttonList.add(new GuiToggle(1, "Show Debug Messages", this.width / 2 + 5, this.height / 6 + (24 * 3), RandomFOVChanger.showDebugMessages,
                (state) -> state ? "§aON" : "§cOFF", (state) -> RandomFOVChanger.showDebugMessages = state));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float _unknown) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Random FOV Changer Settings", this.width / 2, 24, 16777215);
        this.drawString(this.fontRendererObj, "§7Made by Wowkster#0001", this.width - this.fontRendererObj.getStringWidth("§7Made by Wowkster#0001") - 10, this.height - 15, 16777215);
//        this.drawString(this.fontRendererObj, "§c[WARNING] §eThis mod has not been paid for!", 10, this.height - 25, 16777215);
//        this.drawString(this.fontRendererObj, "§7To remove this message contact §bWowkster#0001 §7for a licensed copy.", 10, this.height - 15, 16777215);
        super.drawScreen(mouseX, mouseY, _unknown);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

//        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
//                String.format("§bMaximum CPS Set\n§7Left: %s\n§7Right: %s", displaySupplier.getDisplayString(RandomTextureChanger.leftCpsMax), displaySupplier.getDisplayString(RandomTextureChanger.rightCpsMax))
//        ));
//        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
//                "§c[WARNING] §eThis mod has not been paid for! §7To remove this message contact §bWowkster#0001 §7for a licensed copy."
//        ));
    }
}

package com.wowkster.randomfov;


import com.wowkster.randomfov.gui.GuiModOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = RandomFOVChanger.MODID, name = RandomFOVChanger.MOD_NAME, version = RandomFOVChanger.VERSION, acceptedMinecraftVersions = "[1.8.9]", clientSideOnly = true)
public class RandomFOVChanger {
    public static final String MODID = "random_fov";
    public static final String MOD_NAME = "RandomFOVChanger";
    public static final String VERSION = "0.0.2";

    public static List<KeyBinding> keyBindings;

    public static boolean showDebugHud = false;
    public static boolean showDebugMessages = true;

    public static RandomFOVChanger instance;
    public static int timeInterval = 60; // Seconds
    private boolean enabled = false;
    private boolean paused = false;
    private boolean showedMessage = false;

    public static int minFOV = 30;
    public static int maxFOV = 110;

    private final Timer timer;

    public RandomFOVChanger() {
        instance = this;
        timer = new Timer();
        timer.reset();
    }

    public static RandomFOVChanger getInstance() {
        return RandomFOVChanger.instance;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);

        keyBindings = new ArrayList<>();

        keyBindings.add(new KeyBinding("key.menu.desc", Keyboard.KEY_RSHIFT, "key.random_fov.category"));

        for (KeyBinding key : keyBindings)
            ClientRegistry.registerKeyBinding(key);


    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        timer.reset();

        if (enabled) {
            timer.start();
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onTick(TickEvent.ClientTickEvent event) {
        if (enabled && !paused && timer.getSeconds() > timeInterval) {
            randomizeResourcePack();
            timer.reset();
            timer.start();
        } else if (enabled && !paused && timer.getSeconds() > timeInterval - 1 && !showedMessage) {
            showRandomizingMessage();
            showedMessage = true;
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onTick(GuiOpenEvent event) {
        if (event.gui == null && Minecraft.getMinecraft().currentScreen != null) {
            if (paused) {
                paused = false;
                timer.start();
                return;
            }
        }

        if (event.gui != null) {
            if (!paused) {
                paused = true;
                timer.stop();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onTick(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;

        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;

        if (Minecraft.getMinecraft().gameSettings.showDebugInfo) return;

        if (!showDebugHud) return;
        font.drawStringWithShadow("§bRandom FOV Changer", 10, 10, 0xffffff);
        font.drawStringWithShadow("§eCurrent FOV: §7" + Minecraft.getMinecraft().gameSettings.fovSetting, 10, 30, 0xffffff);
        font.drawStringWithShadow("§eTime Left: §7" + String.format("%.2fs", timeInterval - timer.getSeconds()), 10, 40, 0xffffff);
        font.drawStringWithShadow("§eCurrent Time: §7" + String.format("%.2fs", timer.getSeconds()), 10, 50, 0xffffff);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(InputEvent.KeyInputEvent event) {
        if (keyBindings.get(0).isPressed())
            Minecraft.getMinecraft().displayGuiScreen(new GuiModOptions());
    }

    public void setTimeInterval(int timeInterval) {
        RandomFOVChanger.timeInterval = timeInterval;
        timer.reset();
    }

    public void showRandomizingMessage() {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§cR§6a§en§ad§bo§dm§ci§6z§ei§an§bg §fFOV!"));
    }

    public void randomizeResourcePack() {
        float random = (int) (Math.random() * (maxFOV - minFOV)) + minFOV;

        Minecraft.getMinecraft().gameSettings.fovSetting = random;

        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§eNew FOV is: §7" + random));
    }
}
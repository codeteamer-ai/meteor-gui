package com.meteorui.client.sound;

import com.meteorui.MeteorGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import org.slf4j.LoggerFactory;

public class ModSounds {
    public static void register() {
        LoggerFactory.getLogger("MeteorGUI").info("[MeteorGUI] Sounds registered");
    }

    public static void playOpen() {
        play(SoundEvents.UI_BUTTON_CLICK.value(), 1.2f, 0.4f);
    }

    public static void playClose() {
        play(SoundEvents.UI_BUTTON_CLICK.value(), 0.8f, 0.3f);
    }

    public static void playTabSwitch() {
        play(SoundEvents.UI_BUTTON_CLICK.value(), 1.5f, 0.25f);
    }

    public static void playToggleOn() {
        play(SoundEvents.UI_BUTTON_CLICK.value(), 1.4f, 0.35f);
    }

    public static void playToggleOff() {
        play(SoundEvents.UI_BUTTON_CLICK.value(), 0.9f, 0.3f);
    }

    private static void play(net.minecraft.sound.SoundEvent event, float pitch, float volume) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.getSoundManager().play(PositionedSoundInstance.ui(event, pitch, volume));
        }
    }
}

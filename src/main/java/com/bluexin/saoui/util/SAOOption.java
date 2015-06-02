package com.bluexin.saoui.util;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum SAOOption {

    DEFAULT_UI(StatCollector.translateToLocal("optionDefaultUI"), false),
    DEFAULT_INVENTORY(StatCollector.translateToLocal("optionDefaultInv"), false),
    DEFAULT_DEATH_SCREEN(StatCollector.translateToLocal("optionDefaultDeath"), false),
    CROSS_HAIR(StatCollector.translateToLocal("optionCrossHair"), false),
    HEALTH_BARS(StatCollector.translateToLocal("optionHealthBars"), true),
    SMOOTH_HEALTH(StatCollector.translateToLocal("optionSmoothHealth"), true),
    COLOR_CURSOR(StatCollector.translateToLocal("optionColorCursor"), true),
    PARTICLES(StatCollector.translateToLocal("optionParticles"), true),
    CURSOR_MOVEMENT(StatCollector.translateToLocal("optionCursorMov"), true),
    CLIENT_CHAT_PACKETS(StatCollector.translateToLocal("optionCliChatPacks"), true),
    SOUND_EFFECTS(StatCollector.translateToLocal("optionSounds"), true),
    LOGOUT(StatCollector.translateToLocal("optionLogout"), false),
    ORIGINAL_UI(StatCollector.translateToLocal("optionOrigUI"), true),
    LESS_VISUALS(StatCollector.translateToLocal("optionLessVis"), false);

    public final String name;
    public boolean value;

    SAOOption(String optionName, boolean defaultValue) {
        name = optionName;
        value = defaultValue;
    }

    public final String toString() {
        return name;
    }

}

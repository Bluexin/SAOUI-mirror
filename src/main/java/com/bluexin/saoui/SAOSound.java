package com.bluexin.saoui;

import com.bluexin.saoui.util.SAOOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import java.util.ConcurrentModificationException;

public class SAOSound {

    public static final String CONFIRM = "sao.confirm";
    public static final String DIALOG_CLOSE = "sao.dialog.close";
    public static final String MENU_POPUP = "sao.menu.popup";
    public static final String MESSAGE = "sao.message";
    public static final String ORB_DROPDOWN = "sao.orb.dropdown";
    public static final String PARTICLES_DEATH = "sao.particles.death";

    private static ResourceLocation getResource(String name) {
        return new ResourceLocation(SAOMod.MODID, name);
    }

    public static void playFromEntity(Entity entity, String name) {
        if ((entity != null) && (!entity.isSilent())) {
            try {
                playAtEntity(entity, name);
            } catch (ConcurrentModificationException ignored) {
                System.out.println("CME thrown!");
            }
        }
    }

    public static void playAtEntity(Entity entity, String name) {
        final Minecraft mc = Minecraft.getMinecraft();

        if (mc != null) {
            try {
                play(mc.getSoundHandler(), name, (float) entity.posX, (float) entity.posY, (float) entity.posZ);
            } catch (ConcurrentModificationException ignored) {
                System.out.println("CME thrown!");}
        }
    }

    public static void play(Minecraft mc, String name) {
        if (mc != null) {
            try {
                play(mc.getSoundHandler(), name);
            } catch (ConcurrentModificationException ignored) {
                System.out.println("CME thrown!");}
        }
    }

    public static void play(SoundHandler handler, String name) {
        if ((SAOOption.SOUND_EFFECTS.value) && (handler != null)) {
            try {
                handler.playSound(PositionedSoundRecord.create(getResource(name)));
            } catch (ConcurrentModificationException ignored) {
                System.out.println("CME thrown!");}
        }
    }

    private static void play(SoundHandler handler, String name, float x, float y, float z) {
        if ((SAOOption.SOUND_EFFECTS.value) && (handler != null)) {
            try {
                handler.playSound(PositionedSoundRecord.create(getResource(name), x, y, z));
            } catch (ConcurrentModificationException ignored) {
                System.out.println("CME thrown!");}
        }
    }

}

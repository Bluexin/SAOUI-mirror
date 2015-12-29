package com.bluexin.saoui.util;

import com.bluexin.saoui.SAOMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum SAOColorState {

    INNOCENT(0x93F43EFF),
    VIOLENT(0xF49B00FF),
    KILLER(0xBD0000FF),

    CREATIVE(0x4cedc5FF),
    OP(0xFFFFFFFF),
    INVALID(0x8B8B8BFF),
    GAMEMASTER(0x79139EFF);

    private final int color;

    SAOColorState(int argb) {
        color = argb;
    }

    public static SAOColorState getColorState(Minecraft mc, Entity entity, float time) {
        if (entity instanceof EntityPlayer) return getPlayerColorState(mc, (EntityPlayer) entity, time);
        else if (entity instanceof EntityLiving) {
            if (((EntityLiving) entity).getAttackTarget() instanceof EntityPlayer) return KILLER;
            else return getState(mc, (EntityLiving) entity, time);
        } else return INVALID;
    }

    private static SAOColorState getState(Minecraft mc, EntityLiving entity, float time) {
        if (entity instanceof EntityWolf && ((EntityWolf) entity).isAngry()) return KILLER;
        else if (entity instanceof EntityTameable && ((EntityTameable) entity).isTamed()) {
            if (((EntityTameable) entity).getOwner() != mc.thePlayer)
                return SAOColorState.getColorState(mc, ((EntityTameable) entity).getOwner(), time);
            else return INNOCENT;
        } else if (entity instanceof IMob) return KILLER;
        else if (entity instanceof IAnimals) return INNOCENT;
        else if (entity instanceof IEntityOwnable) return VIOLENT;
        else return INVALID;
    }

    private static SAOColorState getPlayerColorState(Minecraft mc, EntityPlayer player, float time) {
        if (isDev(SAOMod.getName(player))) {
			return GAMEMASTER;
        /*} else if (FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().canSendCommands(((EntityPlayerMP)player).getGameProfile())){
            return OP;*/
        } else if (SAOMod.isCreative((AbstractClientPlayer) player)) {
            return CREATIVE;
		} else {
            return SAOMod.getColorState(player);
        }
    }

    private static boolean isDev(String pl) {
        String[] devs = new String[]{"_Bluexin_", "Blaez", "Felphor", "LordCruaver", "Tencao"};
        for (String dev: devs) {
            if (dev.equals(pl)) return true;
        }

        return false;
    }

    public final void glColor() {
        SAOGL.glColorRGBA(color);
    }

}

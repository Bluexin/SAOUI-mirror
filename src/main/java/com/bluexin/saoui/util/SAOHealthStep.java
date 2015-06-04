package com.bluexin.saoui.util;

import com.bluexin.saoui.SAOMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum SAOHealthStep {

    VERY_LOW(0.1F, 0xBD0000FF),
    LOW(0.2F, 0xF40000FF),
    VERY_DAMAGED(0.3F, 0xF47800FF),
    DAMAGED(0.4F, 0xF4BD00FF),
    OKAY(0.5F, 0xEDEB38FF),
    GOOD(1.0F, 0x93F43EFF),
    CREATIVE(-1.0F, 0xB32DE3FF);

    private final float healthLimit;
    private final int color;

    SAOHealthStep(float limit, int argb) {
        healthLimit = limit;
        color = argb;
    }

    private float getLimit() {
        return healthLimit;
    }

    public final void glColor() {
        SAOGL.glColorRGBA(color);
    }

    public final void glColor(EntityLivingBase entity) {
        /*if (entity instanceof EntityMob) {
			final int red = (color >> 24) & 0xFF;
			final int green = (color >> 24) & 0xFF;
			final int blue = (color >> 24) & 0xFF;
			
			final float value = Math.min((red + green + blue) / 3, 0xFF);
			
			SAOGL.glColor(value / 0xFF, 0, 0, (float) (color & 0xFF) / 0xFF);
		} else {*/
        glColor();
        //}
    }

    public static SAOHealthStep getStep(Minecraft mc, EntityLivingBase entity, float time) {
        if (entity instanceof EntityPlayer && (((EntityPlayer) entity).capabilities.isCreativeMode || ((EntityPlayer) entity).isSpectator())) return CREATIVE;
        final float value = SAOMod.getHealth(mc, entity, time) / SAOMod.getMaxHealth(entity);
        SAOHealthStep step = first();

        while ((value > step.getLimit()) && (step.ordinal() + 1 < values().length)) {
            step = next(step);
        }

        return step;
    }

    private static SAOHealthStep first() {
        return values()[0];
    }

    private static SAOHealthStep next(SAOHealthStep step) {
        return values()[step.ordinal() + 1];
    }

}

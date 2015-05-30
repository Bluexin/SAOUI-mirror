package com.thejackimonster.saoui.util;

import com.thejackimonster.saoui.SAOMod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum SAOColorState {

	INNOCENT(0x93F43EFF),
	VIOLENT(0xF4BD00FF),
	KILLER(0xBD0000FF),

	GAMEMASTER(0x222222FF);

	private final int color;

	private SAOColorState(int argb) {
		color = argb;
	}

	public final void glColor() {
		SAOGL.glColorRGBA(color);
	}

	public static final SAOColorState getColorState(Minecraft mc, Entity entity, float time) {
		if (entity instanceof EntityPlayer) {
			return getPlayerColorState(mc, (EntityPlayer) entity, time);
		} else
		if (entity instanceof EntityCreature) {
			return getEntityColorState((EntityCreature) entity);
		} else {
			return INNOCENT;
		}
	}

	private static SAOColorState getEntityColorState(EntityCreature creature) {
		if ((creature instanceof EntityTameable) && (((EntityTameable) creature).isTamed())) {
			return VIOLENT;
		} else
		if (((creature instanceof EntityWolf) && (((EntityWolf) creature).isAngry())) ||
			(creature.getAttackTarget() instanceof EntityPlayer) ||
			((creature.getRevengeTimer() > 0) && (creature.getAITarget() instanceof EntityPlayer)) ||
			((creature instanceof EntityMob) && (!(creature instanceof EntityPigZombie)))) {
			return KILLER;
		} else {
			return INNOCENT;
		}
	}

	private static final SAOColorState getPlayerColorState(Minecraft mc, EntityPlayer player, float time) {
		/*if (SAOMod.getName(player).equals(SAOMod.AUTHOR_AND_DEVELOPER)) {
			return GAMEMASTER;
		} else {*/
			return SAOMod.getColorState(player);
		//}
	}

}

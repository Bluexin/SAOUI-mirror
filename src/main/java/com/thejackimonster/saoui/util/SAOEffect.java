package com.thejackimonster.saoui.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public enum SAOEffect {

	PARALYZED,
	POISONED,
	STARVING,
	HUNGRY,
	ROTTEN,
	ILL,
	WEAK,
	CURSED,
	BLIND,
	WET,
	DROWNING,
	BURNING;

	private static final int SRC_X = 0;
	private static final int SRC_Y = 135;
	private static final int SRC_WIDTH = 15;
	private static final int SRC_HEIGHT = 10;

	private int getSrcX() {
		return SRC_X + ordinal() * SRC_WIDTH;
	}

	private int getSrcY() {
		return SRC_Y;
	}

	public final void glDraw(int x, int y, float z) {
		SAOGL.glBindTexture(SAOResources.effects);
		SAOGL.glTexturedRect(x, y, z, getSrcX(), getSrcY(), SRC_WIDTH, SRC_HEIGHT);
	}

	public final void glDraw(int x, int y) {
		SAOGL.glBindTexture(SAOResources.effects);
		SAOGL.glTexturedRect(x, y, getSrcX(), getSrcY(), SRC_WIDTH, SRC_HEIGHT);
	}

	public static List<SAOEffect> getEffects(EntityPlayer player) {
		final List<SAOEffect> effects = new ArrayList<>();

		player.getActivePotionEffects().stream().filter(potionEffect0 -> potionEffect0 instanceof PotionEffect).forEach(potionEffect0 -> {
			final PotionEffect potionEffect = (PotionEffect) potionEffect0;

			if ((potionEffect.getPotionID() == Potion.moveSlowdown.getId()) && (potionEffect.getAmplifier() > 5)) {
				effects.add(PARALYZED);
			} else if (potionEffect.getPotionID() == Potion.poison.getId()) {
				effects.add(POISONED);
			} else if (potionEffect.getPotionID() == Potion.hunger.getId()) {
				effects.add(ROTTEN);
			} else if (potionEffect.getPotionID() == Potion.confusion.getId()) {
				effects.add(ILL);
			} else if (potionEffect.getPotionID() == Potion.weakness.getId()) {
				effects.add(WEAK);
			} else if (potionEffect.getPotionID() == Potion.wither.getId()) {
				effects.add(CURSED);
			} else if (potionEffect.getPotionID() == Potion.blindness.getId()) {
				effects.add(BLIND);
			}
		});
		
		if (player.getFoodStats().getFoodLevel() <= 6) {
			effects.add(STARVING);
		} else
		if (player.getFoodStats().getFoodLevel() <= 18) {
			effects.add(HUNGRY);
		}
		
		if (player.isInWater()) {
			if (player.getAir() <= 0) {
				effects.add(DROWNING);
			} else
			if (player.getAir() < 300) {
				effects.add(WET);
			}
		}
		
		if (player.isBurning()) {
			effects.add(BURNING);
		}
		
		return effects;
	}

}

package com.thejackimonster.saoui.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum SAOOption {

	DEFAULT_UI("Default UI", false),
	DEFAULT_INVENTORY("Default Inventory", false),
	DEFAULT_DEATH_SCREEN("Default Death", false),
	CROSS_HAIR("Cross Hair", false),
	HEALTH_BARS("Health Bars", true),
	SMOOTH_HEALTH("Smooth Health", true),
	COLOR_CURSOR("Guild Icon", true),
	PARTICLES("Particles", true),
	CURSOR_MOVEMENT("Cursor Movement", true),
	CLIENT_CHAT_PACKETS("Client Chat Packets", true),
	SOUND_EFFECTS("Sound Effects", true),
	LOGOUT("Can Logout?", false);

	public final String name;
	public boolean value;

	private SAOOption(String optionName, boolean defaultValue) {
		name = optionName;
		value = defaultValue;
	}

	public final String toString() {
		return name;
	}

}

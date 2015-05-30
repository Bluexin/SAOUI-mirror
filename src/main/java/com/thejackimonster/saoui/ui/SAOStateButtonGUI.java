package com.thejackimonster.saoui.ui;

import net.minecraft.client.Minecraft;

import com.thejackimonster.saoui.util.SAOID;
import com.thejackimonster.saoui.util.SAOIcon;
import com.thejackimonster.saoui.util.SAOParentGUI;
import com.thejackimonster.saoui.util.SAOStateHandler;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOStateButtonGUI extends SAOButtonGUI {

	private final SAOStateHandler state;

	public SAOStateButtonGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, int w, int h, String string, SAOIcon saoIcon, SAOStateHandler handler) {
		super(gui, saoID, xPos, yPos, w, h, string, saoIcon);
		state = handler;
	}

	public SAOStateButtonGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, int w, String string, SAOIcon saoIcon, SAOStateHandler handler) {
		this(gui, saoID, xPos, yPos, w, 20, string, saoIcon, handler);
	}

	public SAOStateButtonGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, String string, SAOIcon saoIcon, SAOStateHandler handler) {
		this(gui, saoID, xPos, yPos, 100, string, saoIcon, handler);
	}

	public void update(Minecraft mc) {
		if (state != null) {
			enabled = state.isStateEnabled(mc, this);
		}
		
		super.update(mc);
	}

}

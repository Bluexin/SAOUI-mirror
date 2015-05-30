package com.thejackimonster.saoui.ui;

import net.minecraft.client.Minecraft;

import com.thejackimonster.saoui.SAOMod;
import com.thejackimonster.saoui.util.SAOID;
import com.thejackimonster.saoui.util.SAOIcon;
import com.thejackimonster.saoui.util.SAOParentGUI;
import com.thejackimonster.saoui.util.SAOStateHandler;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOPartyGUI extends SAOStateButtonGUI {

	public SAOPartyGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, int w, int h, String string, SAOIcon saoIcon, boolean partyFlag) {
		super(gui, saoID, xPos, yPos, w, h, string, saoIcon, new SAOPartyStateHandler(partyFlag));
	}

	public SAOPartyGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, int w, String string, SAOIcon saoIcon, boolean partyFlag) {
		this(gui, saoID, xPos, yPos, w, 20, string, saoIcon, partyFlag);
	}

	public SAOPartyGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, String string, SAOIcon saoIcon, boolean partyFlag) {
		this(gui, saoID, xPos, yPos, 100, string, saoIcon, partyFlag);
	}

	private static final class SAOPartyStateHandler implements SAOStateHandler {

		private final boolean flag;

		private SAOPartyStateHandler(boolean partyFlag) {
			flag = partyFlag;
		}

		public boolean isStateEnabled(Minecraft mc, SAOStateButtonGUI button) {
			return (SAOMod.isPartyMember(mc.thePlayer.getName()) == flag);
		}

	}

}

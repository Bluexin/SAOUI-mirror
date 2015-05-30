package com.thejackimonster.saoui.ui;

import net.minecraft.client.Minecraft;

import com.thejackimonster.saoui.util.SAOColor;
import com.thejackimonster.saoui.util.SAOGL;
import com.thejackimonster.saoui.util.SAOParentGUI;
import com.thejackimonster.saoui.util.SAOResources;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOPanelGUI extends SAOMenuGUI {

	public int bgColor;

	public SAOPanelGUI(SAOParentGUI gui, int xPos, int yPos, int w, int h) {
		super(gui, xPos, yPos, w, h);
		bgColor = SAOColor.DEFAULT_COLOR;
	}

	public void draw(Minecraft mc, int cursorX, int cursorY) {
		if ((visibility > 0) && (height > 0)) {
			SAOGL.glBindTexture(SAOResources.gui);
			
			final int left = getX(false);
			final int top = getY(false);
			
			final int shadowSize = (x == 0? 0 : 5);
			
			SAOGL.glColorRGBA(SAOColor.multiplyAlpha(bgColor, visibility));
			
			if (shadowSize > 0) {
				SAOGL.glTexturedRect(left - shadowSize, top - shadowSize, 5 - shadowSize, 120 - shadowSize,  shadowSize, shadowSize);
				SAOGL.glTexturedRect(left + width, top - shadowSize, 15, 120 - shadowSize,  shadowSize, shadowSize);
				SAOGL.glTexturedRect(left - shadowSize, top + height, 5 - shadowSize, 130,  shadowSize, shadowSize);
				SAOGL.glTexturedRect(left + width, top + height, 15, 130,  shadowSize, shadowSize);
				
				SAOGL.glTexturedRect(left, top - shadowSize, width, shadowSize, 5, 120 - shadowSize, 10, shadowSize);
				SAOGL.glTexturedRect(left - shadowSize, top, shadowSize, height, 5 - shadowSize, 120, shadowSize, 10);
				SAOGL.glTexturedRect(left + width, top, shadowSize, height, 15, 120, shadowSize, 10);
				SAOGL.glTexturedRect(left, top + height, width, shadowSize, 5, 130, 10, shadowSize);
			}
			
			SAOGL.glTexturedRect(left, top, width, height, 5, 120, 10, 10);
			
			if (x == 0) {
				SAOGL.glColorRGBA(SAOColor.multiplyAlpha(SAOColor.DEFAULT_COLOR, visibility));
				
				SAOGL.glTexturedRect(left + 5, top, 156, 25, 10, 10);
			}
		}
		
		super.draw(mc, cursorX, cursorY);
	}

}

package com.thejackimonster.saoui.ui;

import net.minecraft.client.Minecraft;

import com.thejackimonster.saoui.util.SAOColor;
import com.thejackimonster.saoui.util.SAOGL;
import com.thejackimonster.saoui.util.SAOID;
import com.thejackimonster.saoui.util.SAOIcon;
import com.thejackimonster.saoui.util.SAOParentGUI;
import com.thejackimonster.saoui.util.SAOResources;

public class SAOAlertGUI extends SAOElementGUI {

	public String caption;
	public int alertColor;

	public SAOAlertGUI(SAOParentGUI gui, int xPos, int yPos, int w, String string, int color) {
		super(gui, xPos, yPos, w, 32);
		caption = string;
		alertColor = color;
	}

	public SAOAlertGUI(SAOParentGUI gui, int xPos, int yPos, String string, int color) {
		this(gui, xPos, yPos, autoWidth(string), string, color);
	}

	private static final int autoWidth(String string) {
		final int defValue = SAOGL.glStringWidth(string);
		
		return Math.max(0, defValue - 20);
	}

	public void draw(Minecraft mc, int cursorX, int cursorY) {
		super.draw(mc, cursorX, cursorY);
		
		if (visibility > 0) {
			SAOGL.glBindTexture(SAOResources.gui);
			
			final int color = mouseOver(cursorX, cursorY)? SAOColor.mediumColor(alertColor, SAOColor.DEFAULT_FONT_COLOR) : alertColor;
			
			SAOGL.glColorRGBA(SAOColor.multiplyAlpha(color, visibility));
			
			final int left = getX(false);
			final int top = getY(false);
			
			SAOGL.glTexturedRect(left - 20, top, 0, 145, 20, height);
			SAOGL.glTexturedRect(left, top, width, height, 20, 145, 40, height);
			SAOGL.glTexturedRect(left + width, top, 60, 145, 20, height);
			
			SAOGL.glString(caption, left + (width - SAOGL.glStringWidth(caption)) / 2, top + (height - SAOGL.glStringHeight()) / 2, alertColor);
		}
	}

	public boolean mouseReleased(Minecraft mc, int cursorX, int cursorY, int button) {
		return (button == 0);
	}

	public int getX(boolean relative) {
		return super.getX(relative) - width / 2;
	}

	public int getY(boolean relative) {
		return super.getY(relative) - height / 2;
	}

	public SAOID ID() {
		return SAOID.ALERT;
	}

}

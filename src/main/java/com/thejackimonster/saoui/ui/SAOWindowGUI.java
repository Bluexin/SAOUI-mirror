package com.thejackimonster.saoui.ui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import com.thejackimonster.saoui.util.SAOAlign;
import com.thejackimonster.saoui.util.SAOColor;
import com.thejackimonster.saoui.util.SAOGL;
import com.thejackimonster.saoui.util.SAOParentGUI;
import com.thejackimonster.saoui.util.SAOResources;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOWindowGUI extends SAOMenuGUI {

	private final SAOLabelGUI titleLable;

	public SAOWindowGUI(SAOParentGUI gui, int xPos, int yPos, int w, int h, String title) {
		super(gui, xPos, yPos, w, h);
		elements.add(titleLable = new SAOLabelGUI(this, 0, 0, title, SAOAlign.CENTER));
		
		if (titleLable.width > width) {
			width = titleLable.width;
		}
	}

	public final void setTitle(String title) {
		titleLable.caption = title;
	}

	public final String getTitle() {
		return titleLable.caption;
	}

	protected int getSize() {
		return Math.max(super.getSize(), 20) + 20;
	}

	protected int getOffsetSize(SAOElementGUI element) {
		return element.visibility > 0? super.getOffsetSize(element) : 0;
	}

	public void draw(Minecraft mc, int cursorX, int cursorY) {
		if (visibility > 0) {
			SAOGL.glBindTexture(SAOResources.gui);
			SAOGL.glColorRGBA(SAOColor.multiplyAlpha(SAOColor.DEFAULT_COLOR, visibility));
			
			final int left = getX(false);
			final int top = getY(false);
			
			final int topBox = getBoxSize(false);
			final int bottomBox = getBoxSize(true);
			
			final int width2 = width / 2;
			
			final int size = height - (topBox + bottomBox);
			
			SAOGL.glTexturedRect(left, top, width2, topBox, 0, 65, width2, 20);
			SAOGL.glTexturedRect(left + width2, top, width2, topBox, 200 - width2, 65, width2, 20);
			
			if (size > 0) {
				final int borderSize = Math.min(size / 2, 10);
				
				SAOGL.glTexturedRect(left, top + topBox, 0, 85, width2, borderSize);
				SAOGL.glTexturedRect(left + width2, top + topBox, 200 - width2, 85, width2, borderSize);
				
				if ((size + 1) / 2 > 10) {
					SAOGL.glTexturedRect(left, top + topBox + borderSize, width, size - borderSize * 2, 0, 95, 200, 10);
				}
				
				SAOGL.glTexturedRect(left, top + topBox + size - borderSize, 0, 115 - borderSize, width2, borderSize);
				SAOGL.glTexturedRect(left + width2, top + topBox + size - borderSize, 200 - width2, 115 - borderSize, width2, borderSize);
			}
			
			SAOGL.glTexturedRect(left, top + size + topBox, width2, bottomBox, 0, 65, width2, 20);
			SAOGL.glTexturedRect(left + width2, top + size + topBox, width2, bottomBox, 200 - width2, 65, width2, 20);
		}
		
		super.draw(mc, cursorX, cursorY);
	}

	public int getY(boolean relative) {
		return super.getY(relative) + (relative? 0 : height / 2);
	}

	protected int getBoxSize(boolean bottom) {
		return 20;
	}

}

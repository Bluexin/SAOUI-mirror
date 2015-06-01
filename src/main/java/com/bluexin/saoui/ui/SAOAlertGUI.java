package com.bluexin.saoui.ui;

import com.bluexin.saoui.util.*;
import net.minecraft.client.Minecraft;

public class SAOAlertGUI extends SAOElementGUI {

    private String caption;
    private int alertColor;

    private SAOAlertGUI(SAOParentGUI gui, int xPos, int yPos, int w, String string, int color) {
        super(gui, xPos, yPos, w, 32);
        caption = string;
        alertColor = color;
    }

    public SAOAlertGUI(SAOParentGUI gui, int xPos, int yPos, String string, int color) {
        this(gui, xPos, yPos, autoWidth(string), string, color);
    }

    private static int autoWidth(String string) {
        final int defValue = SAOGL.glStringWidth(string);

        return Math.max(0, defValue - 20);
    }

    @Override
	public void draw(Minecraft mc, int cursorX, int cursorY) {
        super.draw(mc, cursorX, cursorY);

        if (visibility > 0) {
            SAOGL.glBindTexture(SAOOption.ORIGINAL_UI.value? SAOResources.gui: SAOResources.guiCustom);

            final int color = mouseOver(cursorX, cursorY) ? SAOColor.mediumColor(alertColor, SAOColor.DEFAULT_FONT_COLOR) : alertColor;

            SAOGL.glColorRGBA(SAOColor.multiplyAlpha(color, visibility));

            final int left = getX(false);
            final int top = getY(false);

            SAOGL.glTexturedRect(left - 20, top, 0, 145, 20, height);
            SAOGL.glTexturedRect(left, top, width, height, 20, 145, 40, height);
            SAOGL.glTexturedRect(left + width, top, 60, 145, 20, height);

            SAOGL.glString(caption, left + (width - SAOGL.glStringWidth(caption)) / 2, top + (height - SAOGL.glStringHeight()) / 2, alertColor);
        }
    }

    @Override
	public boolean mouseReleased(Minecraft mc, int cursorX, int cursorY, int button) {
        return (button == 0);
    }

    @Override
	public int getX(boolean relative) {
        return super.getX(relative) - width / 2;
    }

    @Override
	public int getY(boolean relative) {
        return super.getY(relative) - height / 2;
    }

    @Override
	public SAOID ID() {
        return SAOID.ALERT;
    }

}
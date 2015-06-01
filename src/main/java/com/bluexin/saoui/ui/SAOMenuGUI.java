package com.bluexin.saoui.ui;

import com.bluexin.saoui.util.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOMenuGUI extends SAOContainerGUI {

    boolean fullArrow;
    public boolean innerMenu;

    public SAOMenuGUI(SAOParentGUI gui, int xPos, int yPos, int w, int h) {
        super(gui, xPos, yPos, w, h);
        fullArrow = true;
        innerMenu = false;
    }

    int getOffset(int index) {
        int start = 0;
        int offset = 0;

        while ((start < elements.size()) && (start < index)) {
            offset += getOffsetSize(elements.get(start++));
        }

        return offset;
    }

    int getOffsetSize(SAOElementGUI element) {
        return element.height;
    }

    @Override
	public void update(Minecraft mc) {
        height = getSize();

        if (width <= 0) {
            elements.stream().filter(element -> element.width > width).forEach(element -> width = element.width);
        }

        super.update(mc);
    }

    int getSize() {
        return getOffset(elements.size());
    }

    @Override
	void update(Minecraft mc, int index, SAOElementGUI element) {
        element.y = getOffset(index);
        element.width = width - element.x;

        super.update(mc, index, element);
    }

    @Override
	public void draw(Minecraft mc, int cursorX, int cursorY) {
        if ((visibility > 0) && (parent != null) && (height > 0)) {
            if (x > 0) {
                SAOGL.glBindTexture(SAOOption.ORIGINAL_UI.value? SAOResources.gui: SAOResources.guiCustom);
                SAOGL.glColorRGBA(SAOColor.multiplyAlpha(SAOColor.DEFAULT_COLOR, visibility));

                final int left = getX(false);
                final int top = getY(false);

                final int arrowTop = super.getY(false) - height / 2;

                SAOGL.glTexturedRect(left - 2, top, 2, height, 40, 41, 2, 4);
                SAOGL.glTexturedRect(left - 10, arrowTop + (height - 10) / 2, 20, 25 + (fullArrow ? 10 : 0), 10, 10);
            } else if (x < 0) {
                SAOGL.glBindTexture(SAOOption.ORIGINAL_UI.value? SAOResources.gui: SAOResources.guiCustom);
                SAOGL.glColorRGBA(SAOColor.multiplyAlpha(SAOColor.DEFAULT_COLOR, visibility));

                final int left = getX(false);
                final int top = getY(false);

                final int arrowTop = super.getY(false) - height / 2;

                SAOGL.glTexturedRect(left + width, top, 2, height, 40, 41, 2, 4);
                SAOGL.glTexturedRect(left + width, arrowTop + (height - 10) / 2, 30, 25 + (fullArrow ? 10 : 0), 10, 10);
            }
        }

        super.draw(mc, cursorX, cursorY);
    }

    @Override
	public int getY(boolean relative) {
        return super.getY(relative) - (relative || innerMenu ? 0 : height / 2);
    }

}

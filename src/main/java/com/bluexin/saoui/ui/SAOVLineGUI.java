package com.bluexin.saoui.ui;

import com.bluexin.saoui.util.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOVLineGUI extends SAOElementGUI {

    private int lineWidth;

    public SAOVLineGUI(SAOParentGUI gui, int xPos, int yPos, int size) {
        super(gui, xPos, yPos, size, 2);
        lineWidth = size;
    }

    @Override
    public void draw(Minecraft mc, int cursorX, int cursorY) {
        super.draw(mc, cursorX, cursorY);

        if (visibility > 0) {
            SAOGL.glBindTexture(SAOOption.ORIGINAL_UI.value? SAOResources.gui: SAOResources.guiCustom);
            SAOGL.glColorRGBA(SAOColor.multiplyAlpha(SAOColor.DEFAULT_FONT_COLOR, visibility));

            final int left = getX(false) + (width - lineWidth) / 2;
            final int top = getY(false);

            SAOGL.glTexturedRect(left, top, lineWidth, 2, 42, 42, 4, 2);
        }
    }

}

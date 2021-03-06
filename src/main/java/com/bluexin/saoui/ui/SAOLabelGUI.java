package com.bluexin.saoui.ui;

import com.bluexin.saoui.util.SAOAlign;
import com.bluexin.saoui.util.SAOColor;
import com.bluexin.saoui.util.SAOGL;
import com.bluexin.saoui.util.SAOParentGUI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOLabelGUI extends SAOElementGUI {

    public String caption;
    public SAOColor fontColor;
    private SAOAlign align;

    public SAOLabelGUI(SAOParentGUI gui, int xPos, int yPos, int width, String string, SAOAlign saoAlign) {
        super(gui, xPos, yPos, width, 20);
        caption = string;
        fontColor = SAOColor.DEFAULT_FONT_COLOR;
        align = saoAlign;
    }

    public SAOLabelGUI(SAOParentGUI gui, int xPos, int yPos, String string, SAOAlign saoAlign) {
        this(gui, xPos, yPos, 200, string, saoAlign);
    }

    @Override
    public void draw(Minecraft mc, int cursorX, int cursorY) {
        super.draw(mc, cursorX, cursorY);

        if (visibility > 0) {
            final int left = align.getX(this, false, SAOGL.glStringWidth(caption)) + getOffsetX();
            final int top = getY(false);

            SAOGL.glString(caption, left, top + (height - SAOGL.glStringHeight()) / 2, fontColor.multiplyAlpha(visibility));
        }
    }

    private int getOffsetX() {
        if (align == SAOAlign.LEFT) {
            return 8;
        } else if (align == SAOAlign.RIGHT) {
            return -8;
        } else {
            return 0;
        }
    }

}

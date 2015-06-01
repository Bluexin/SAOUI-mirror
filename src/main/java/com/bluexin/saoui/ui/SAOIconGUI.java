package com.bluexin.saoui.ui;

import com.bluexin.saoui.SAOSound;
import com.bluexin.saoui.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOIconGUI extends SAOElementGUI {

    private final SAOID id;

    private SAOIcon icon;
    public boolean highlight;
    public int bgColor, disabledMask;

    public SAOIconGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, SAOIcon saoIcon) {
        super(gui, xPos, yPos, 20, 20);
        id = saoID;
        icon = saoIcon;
        highlight = false;
        bgColor = SAOColor.DEFAULT_COLOR;
        disabledMask = SAOColor.DISABLED_MASK;
    }

    @Override
    public void draw(Minecraft mc, int cursorX, int cursorY) {
        super.draw(mc, cursorX, cursorY);

        if (visibility > 0) {
            SAOGL.glBindTexture(SAOOption.ORIGINAL_UI.value? SAOResources.gui: SAOResources.guiCustom);

            final int hoverState = hoverState(cursorX, cursorY);

            final int color0 = getColor(hoverState, true);
            final int color1 = getColor(hoverState, false);

            SAOGL.glColorRGBA(SAOColor.multiplyAlpha(color0, visibility));

            final int left = getX(false);
            final int top = getY(false);

            SAOGL.glTexturedRect(left, top, 0, 25, 20, 20);

            final int iconOffset = 2;

            SAOGL.glColorRGBA(SAOColor.multiplyAlpha(color1, visibility));
            icon.glDraw(left + iconOffset, top + iconOffset);
        }
    }

    private int getColor(int hoverState, boolean bg) {
        if (icon == SAOIcon.CONFIRM) {
            if (bg) {
                return hoverState == 1 ? SAOColor.CONFIRM_COLOR : hoverState == 2 ? SAOColor.CONFIRM_COLOR_LIGHT : SAOColor.CONFIRM_COLOR & disabledMask;
            } else {
                return hoverState > 0 ? SAOColor.HOVER_FONT_COLOR : disabledMask;
            }
        } else if (icon == SAOIcon.CANCEL) {
            if (bg) {
                return hoverState == 1 ? SAOColor.CANCEL_COLOR : hoverState == 2 ? SAOColor.CANCEL_COLOR_LIGHT : SAOColor.CANCEL_COLOR & disabledMask;
            } else {
                return hoverState > 0 ? SAOColor.HOVER_FONT_COLOR : disabledMask;
            }
        } else {
            if (bg) {
                return hoverState == 1 ? bgColor : hoverState == 2 ? SAOColor.HOVER_COLOR : bgColor & disabledMask;
            } else {
                return hoverState == 1 ? SAOColor.DEFAULT_FONT_COLOR : hoverState == 2 ? SAOColor.HOVER_FONT_COLOR : SAOColor.DEFAULT_FONT_COLOR & disabledMask;
            }
        }
    }

    @Override
    public boolean mouseReleased(Minecraft mc, int cursorX, int cursorY, int button) {
        return (button == 0);
    }

    @Override
    public void click(SoundHandler handler, boolean flag) {
        if (icon == SAOIcon.CONFIRM) {
            SAOSound.play(handler, SAOSound.CONFIRM);
        } else {
            super.click(handler, flag);
        }
    }

    private int hoverState(int cursorX, int cursorY) {
        if ((highlight) || (mouseOver(cursorX, cursorY))) {
            return 2;
        } else if (enabled) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public SAOID ID() {
        return id;
    }

}

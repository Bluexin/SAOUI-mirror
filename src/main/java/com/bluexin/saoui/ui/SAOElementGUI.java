package com.bluexin.saoui.ui;

import com.bluexin.saoui.SAOSound;
import com.bluexin.saoui.util.SAOAction;
import com.bluexin.saoui.util.SAOID;
import com.bluexin.saoui.util.SAOParentGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class SAOElementGUI implements SAOParentGUI {

    public final SAOParentGUI parent;

    public int x, y;
    public int width;
    public int height;
    public boolean enabled;
    public float visibility;
    public boolean focus;

    private boolean removed;

    SAOElementGUI(SAOParentGUI gui, int xPos, int yPos, int w, int h) {
        parent = gui;
        x = xPos;
        y = yPos;
        width = w;
        height = h;
        enabled = true;
        visibility = 1;
        focus = false;

        removed = false;
    }

    public void update(Minecraft mc) {
    }

    public void draw(Minecraft mc, int cursorX, int cursorY) {
        if (mouseOver(cursorX, cursorY)) {
            mouseMoved(mc, cursorX, cursorY);
        }
    }

    public boolean keyTyped(Minecraft mc, char ch, int key) {
        return false;
    }

    public boolean mouseOver(int cursorX, int cursorY, int flag) {
        if ((visibility >= 1) && (enabled)) {
            final int left = getX(false);
            final int top = getY(false);

            return (
                    (cursorX >= left) &&
                            (cursorY >= top) &&
                            (cursorX <= left + width) &&
                            (cursorY <= top + height)
            );
        } else {
            return false;
        }
    }

    public final boolean mouseOver(int cursorX, int cursorY) {
        return mouseOver(cursorX, cursorY, -1);
    }

    public boolean mousePressed(Minecraft mc, int cursorX, int cursorY, int button) {
        return false;
    }

    void mouseMoved(Minecraft mc, int cursorX, int cursorY) {
    }

    public boolean mouseReleased(Minecraft mc, int cursorX, int cursorY, int button) {
        return false;
    }

    public boolean mouseWheel(Minecraft mc, int cursorX, int cursorY, int delta) {
        return false;
    }

    @Override
    public int getX(boolean relative) {
        return relative ? x : x + (parent != null ? parent.getX(relative) : 0);
    }

    @Override
    public int getY(boolean relative) {
        return relative ? y : y + (parent != null ? parent.getY(relative) : 0);
    }

    public void click(SoundHandler handler, boolean flag) {
        if (flag) {
            SAOSound.play(handler, SAOSound.MENU_POPUP);
        } else {
            SAOSound.play(handler, SAOSound.DIALOG_CLOSE);
        }
    }

    @Override
    public void actionPerformed(SAOElementGUI element, SAOAction action, int data) {
        if (parent != null) {
            parent.actionPerformed(element, action, data);
        }
    }

    public SAOID ID() {
        return SAOID.NONE;
    }

    public void close(Minecraft mc) {
        if (!removed) {
            remove();
        }
    }

    void remove() {
        removed = true;
    }

    public boolean removed() {
        return removed;
    }

    public String toString() {
        return "[ ( " + getClass().getName() + " " + x + " " + y + " " + width + " " + height + " ) => " + parent + " ]";
    }

}

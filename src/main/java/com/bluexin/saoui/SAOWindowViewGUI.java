package com.bluexin.saoui;

import com.bluexin.saoui.ui.SAOConfirmGUI;
import com.bluexin.saoui.ui.SAOMessageGUI;
import com.bluexin.saoui.ui.SAOScreenGUI;
import com.bluexin.saoui.ui.SAOWindowGUI;
import com.bluexin.saoui.util.SAOActionHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOWindowViewGUI extends SAOScreenGUI {

    private final int windowWidth, windowHeight;

    private SAOWindowViewGUI(int width, int height) {
        super();
        windowWidth = width;
        windowHeight = height;
    }

    public static SAOWindowViewGUI viewMessage(final String username, final String message) {
        return new SAOWindowViewGUI(200, 40) {

            public SAOWindowGUI createWindow(int width, int height) {
                return new SAOMessageGUI(this, 0, 0, width, height, message, username);
            }

        };
    }

    @SuppressWarnings("unused")
    public static SAOWindowViewGUI viewConfirm(final String title, final String message, final SAOActionHandler handler) {
        return new SAOWindowViewGUI(200, 60) {

            public SAOWindowGUI createWindow(int width, int height) {
                return new SAOConfirmGUI(this, 0, 0, width, height, title, message, handler);
            }

        };
    }

    @Override
    protected void init() {
        super.init();
        elements.add(createWindow(windowWidth, windowHeight));
    }

    private SAOWindowGUI createWindow(int width, int height) {
        return null;
    }

    public final SAOWindowGUI getWindow() {
        return (SAOWindowGUI) elements.get(0);
    }

    @Override
    public int getX(boolean relative) {
        return super.getX(relative) + (width - windowWidth) / 2;
    }

    @Override
    public int getY(boolean relative) {
        return super.getY(relative) + (height - windowHeight) / 2;
    }

    @Override
    public void drawScreen(int cursorX, int cursorY, float f) {
        drawDefaultBackground();

        super.drawScreen(cursorX, cursorY, f);
    }

    @Override
    protected void backgroundClicked(int cursorX, int cursorY, int button) {
    }

}

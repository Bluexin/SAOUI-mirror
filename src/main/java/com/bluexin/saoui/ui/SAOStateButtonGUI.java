package com.bluexin.saoui.ui;

import com.bluexin.saoui.util.SAOID;
import com.bluexin.saoui.util.SAOIcon;
import com.bluexin.saoui.util.SAOParentGUI;
import com.bluexin.saoui.util.SAOStateHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOStateButtonGUI extends SAOButtonGUI {

    private final SAOStateHandler state;

    SAOStateButtonGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, int w, int h, String string, SAOIcon saoIcon, SAOStateHandler handler) {
        super(gui, saoID, xPos, yPos, w, h, string, saoIcon);
        state = handler;
    }

    private SAOStateButtonGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, int w, String string, SAOIcon saoIcon, SAOStateHandler handler) {
        this(gui, saoID, xPos, yPos, w, 20, string, saoIcon, handler);
    }

    public SAOStateButtonGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, String string, SAOIcon saoIcon, SAOStateHandler handler) {
        this(gui, saoID, xPos, yPos, 100, string, saoIcon, handler);
    }

    @Override
    public void update(Minecraft mc) {
        if (state != null) enabled = state.isStateEnabled(mc, this);

        super.update(mc);
    }

}

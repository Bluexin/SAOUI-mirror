package com.bluexin.saoui.ui;

import com.bluexin.saoui.util.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOPartyGUI extends SAOStateButtonGUI {

    private SAOPartyGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, int w, int h, String string, SAOIcon saoIcon, boolean forced) {
        super(gui, saoID, xPos, yPos, w, h, string, saoIcon, new SAOPartyStateHandler(forced));
    }

    private SAOPartyGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, int w, String string, SAOIcon saoIcon, boolean forced) {
        this(gui, saoID, xPos, yPos, w, 20, string, saoIcon, forced);
    }

    public SAOPartyGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, String string, SAOIcon saoIcon, boolean forced) {
        this(gui, saoID, xPos, yPos, 100, string, saoIcon, forced);
    }

    private static final class SAOPartyStateHandler implements SAOStateHandler {

        private final boolean forced;

        private SAOPartyStateHandler(boolean forced) {
            this.forced = forced;
        }

        @Override
        public boolean isStateEnabled(Minecraft mc, SAOStateButtonGUI button) {
            return forced || PartyHelper.instance().isPartyMember(mc.thePlayer.getName());
        }

    }

}

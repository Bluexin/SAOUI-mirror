package com.bluexin.saoui.ui;

import com.bluexin.saoui.util.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOPartyGUI extends SAOStateButtonGUI {

    private SAOPartyGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, int w, int h, String string, SAOIcon saoIcon) {
        super(gui, saoID, xPos, yPos, w, h, string, saoIcon, new SAOPartyStateHandler(saoID));
    }

    private SAOPartyGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, int w, String string, SAOIcon saoIcon) {
        this(gui, saoID, xPos, yPos, w, 20, string, saoIcon);
    }

    public SAOPartyGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, String string, SAOIcon saoIcon) {
        this(gui, saoID, xPos, yPos, 100, string, saoIcon);
    }

    private static final class SAOPartyStateHandler implements SAOStateHandler {

        private final SAOID id;

        private SAOPartyStateHandler(SAOID id) {

            this.id = id;
        }

        @Override
        public boolean isStateEnabled(Minecraft mc, SAOStateButtonGUI button) {
            return PartyHelper.instance().shouldHighlight(id);
        }

    }

}

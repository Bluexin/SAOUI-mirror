package com.bluexin.saoui.ui;

import com.bluexin.saoui.util.SAOIcon;
import com.bluexin.saoui.SAOMod;
import com.bluexin.saoui.util.SAOID;
import com.bluexin.saoui.util.SAOParentGUI;
import com.bluexin.saoui.util.SAOStateHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOPartyGUI extends SAOStateButtonGUI {

    private SAOPartyGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, int w, int h, String string, SAOIcon saoIcon, boolean partyFlag) {
        super(gui, saoID, xPos, yPos, w, h, string, saoIcon, new SAOPartyStateHandler(partyFlag));
    }

    private SAOPartyGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, int w, String string, SAOIcon saoIcon, boolean partyFlag) {
        this(gui, saoID, xPos, yPos, w, 20, string, saoIcon, partyFlag);
    }

    public SAOPartyGUI(SAOParentGUI gui, SAOID saoID, int xPos, int yPos, String string, SAOIcon saoIcon, boolean partyFlag) {
        this(gui, saoID, xPos, yPos, 100, string, saoIcon, partyFlag);
    }

    private static final class SAOPartyStateHandler implements SAOStateHandler {

        private final boolean flag;

        private SAOPartyStateHandler(boolean partyFlag) {
            flag = partyFlag;
        }

        @Override
        public boolean isStateEnabled(Minecraft mc, SAOStateButtonGUI button) {
            return (SAOMod.isPartyMember(mc.thePlayer.getName()) == flag);
        }

    }

}

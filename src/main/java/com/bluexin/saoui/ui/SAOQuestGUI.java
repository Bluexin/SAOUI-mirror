package com.bluexin.saoui.ui;

import com.bluexin.saoui.util.SAOID;
import com.bluexin.saoui.util.SAOIcon;
import com.bluexin.saoui.util.SAOParentGUI;
import net.minecraft.stats.Achievement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOQuestGUI extends SAOButtonGUI {

    private final Achievement achievement;

    private SAOQuestGUI(SAOParentGUI gui, int xPos, int yPos, int w, int h, Achievement ach0) {
        super(gui, SAOID.QUEST, xPos, yPos, w, h, ach0.getStatName().getFormattedText(), SAOIcon.QUEST);
        achievement = ach0;
    }

    public SAOQuestGUI(SAOParentGUI gui, int xPos, int yPos, int w, Achievement ach0) {
        this(gui, xPos, yPos, w, 20, ach0);
    }

    public SAOQuestGUI(SAOParentGUI gui, int xPos, int yPos, Achievement ach0) {
        this(gui, xPos, yPos, 150, ach0);
    }

    public Achievement getAchievement() {
        return achievement;
    }

}

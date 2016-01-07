package com.bluexin.saoui.ui;

import com.bluexin.saoui.util.SAOID;
import com.bluexin.saoui.util.SAOParentGUI;
import com.bluexin.saoui.util.SAOSkill;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;

/**
 * Part of SAOUI
 *
 * @author Bluexn
 */
public class SkillButton extends SAOButtonGUI {
    private final SAOSkill skill;

    public SkillButton(SAOParentGUI gui, int xPos, int yPos, SAOSkill skill) {
        super(gui, SAOID.SKILL, xPos, yPos, skill.toString(), skill.icon, skill.shouldHighlight());
        this.skill = skill;
    }

    public void action(Minecraft mc, GuiInventory parent) {
        this.skill.activate(mc, parent);
        this.highlight = skill.shouldHighlight();
    }
}

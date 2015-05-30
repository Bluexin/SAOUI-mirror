package com.thejackimonster.saoui.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum SAOSkill {

    SPRINTING(SAOIcon.SPRINTING, SAOID.SKILL),
    SNEAKING(SAOIcon.SNEAKING, SAOID.SKILL),
    CRAFTING(SAOIcon.CRAFTING, SAOID.SKILL);

    public final SAOIcon icon;
    public final SAOID id;

    SAOSkill(SAOIcon saoIcon, SAOID saoId) {
        icon = saoIcon;
        id = saoId;
    }

    public final String toString() {
        final String name = name();

        return name.charAt(0) + name.substring(1, name.length()).toLowerCase();
    }

}

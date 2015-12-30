package com.bluexin.saoui.util;

import com.bluexin.saoui.SAOMod;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.BooleanSupplier;

@SideOnly(Side.CLIENT)
public enum SAOSkill {

    SPRINTING(SAOIcon.SPRINTING, SAOID.SKILL, () -> SAOMod.IS_SPRINTING),
    SNEAKING(SAOIcon.SNEAKING, SAOID.SKILL, () -> SAOMod.IS_SNEAKING),
    CRAFTING(SAOIcon.CRAFTING, SAOID.SKILL, () -> false);

    public final SAOIcon icon;
    public final SAOID id;
    private final BooleanSupplier shouldHighlight;

    SAOSkill(SAOIcon saoIcon, SAOID saoId, BooleanSupplier shouldHighlight) {
        this.icon = saoIcon;
        this.id = saoId;
        this.shouldHighlight = shouldHighlight;
    }

    public final String toString() {
        final String name = name();

        return StatCollector.translateToLocal("skill" + name.charAt(0) + name.substring(1, name.length()).toLowerCase());
    }

    /**
     * Whether this skill's button should highlight or not.
     *
     * @return whether it should be highlighted
     */
    public boolean shouldHighlight() {
        return shouldHighlight.getAsBoolean();
    } // Doing it this way might come in handy when building an API
}

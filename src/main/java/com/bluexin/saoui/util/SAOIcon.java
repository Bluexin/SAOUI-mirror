package com.bluexin.saoui.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum SAOIcon {

    NONE,
    OPTION,
    HELP,
    LOGOUT,
    CANCEL,
    CONFIRM,
    SETTINGS,
    NAVIGATION,
    MESSAGE,
    SOCIAL,
    PROFILE,
    EQUIPMENT,
    ITEMS,
    SKILLS,
    GUILD,
    PARTY,
    FRIEND,
    CREATE,
    INVITE,
    QUEST,
    FIELD_MAP,
    DUNGEON_MAP,
    ARMOR,
    ACCESSORY,
    MESSAGE_RECEIVED,
    CRAFTING,
    SPRINTING,
    SNEAKING;

    private static final int SRC_SIZE = 16;

    private int getSrcX() {
        return (index() % 16) * SRC_SIZE;
    }

    private int getSrcY() {
        return (index() / 16) * SRC_SIZE;
    }

    private int index() {
        return (ordinal() - 1);
    }

    public final void glDraw(int x, int y, float z) {
        if (index() >= 0) {
            SAOGL.glBindTexture(SAOOption.ORIGINAL_UI.getValue() ? SAOResources.icons : SAOResources.iconsCustom);
            SAOGL.glTexturedRect(x, y, z, getSrcX(), getSrcY(), SRC_SIZE, SRC_SIZE);
        }
    }

    public final void glDraw(int x, int y) {
        if (index() >= 0) {
            SAOGL.glBindTexture(SAOOption.ORIGINAL_UI.getValue() ? SAOResources.icons : SAOResources.iconsCustom);
            SAOGL.glTexturedRect(x, y, getSrcX(), getSrcY(), SRC_SIZE, SRC_SIZE);
        }
    }

}

package com.bluexin.saoui.commands;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum CommandType {

    INVITE_PARTY,
    DISSOLVE_PARTY,
    UPDATE_PARTY,

    CONFIRM_INVITE_PARTY,
    CANCEL_INVITE_PARTY,

    ADD_FRIEND_REQUEST,

    ACCEPT_ADD_FRIEND,
    CANCEL_ADD_FRIEND;

    public static final String PREFIX = "[SAOUI "; // TODO: config-based
    public static final String SUFFIX = "]";

    static CommandType getCommand(String id) {
        try {
            return valueOf(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public final String toString() {
        return (PREFIX + name() + SUFFIX);
    }

    public final String key() {
        return this.name().replace("_", "");
    }

}

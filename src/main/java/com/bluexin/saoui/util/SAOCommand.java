package com.bluexin.saoui.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum SAOCommand {

    INVITE_PARTY,
    DISSOLVE_PARTY,
    UPDATE_PARTY,

    CONFIRM_INVITE_PARTY,
    CANCEL_INVITE_PARTY,

    ADD_FRIEND_REQUEST,

    ACCEPT_ADD_FRIEND,
    CANCEL_ADD_FRIEND;

    private static final String PREFIX = "[SAOUI ";
    private static final String SUFFIX = "]";

    // TODO: add info (for non-saoui users) and data (parser)
    public static SAOCommand getCommand(String data) {
        if (data.startsWith(PREFIX)) {
            final int nextData = data.indexOf(SUFFIX, PREFIX.length());
            final String id = data.substring(PREFIX.length(), nextData);

            try {
                return valueOf(id);
            } catch (IllegalArgumentException e) {
                return null;
            }
        } else return null;
    }

    public final String[] getContent(String data) {
        final int index = toString().length() + 1;

        if (index >= data.length()) return new String[0];
        else return data.substring(index).split(" ");
    }

    public final String toString() {
        return (PREFIX + name() + SUFFIX);
    }

}

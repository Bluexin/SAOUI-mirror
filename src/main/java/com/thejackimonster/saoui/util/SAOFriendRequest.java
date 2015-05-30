package com.thejackimonster.saoui.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class SAOFriendRequest {

    private final String friendName;
    public int ticks;

    public SAOFriendRequest(String name, int maxTicks) {
        friendName = name;
        ticks = maxTicks;
    }

    private boolean equals(SAOFriendRequest request) {
        return equals(request == null ? (String) null : request.friendName);
    }

    public final boolean equals(String name) {
        return friendName.equals(name);
    }

    public final boolean equals(Object object) {
        if (object instanceof SAOFriendRequest) {
            return equals((SAOFriendRequest) object);
        } else {
            return equals(String.valueOf(object));
        }
    }

}

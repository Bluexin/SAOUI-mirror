package com.bluexin.saoui.commands;

import com.bluexin.saoui.util.FriendsHandler;
import com.bluexin.saoui.util.PartyHelper;
import com.bluexin.saoui.util.TriConsumer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum CommandType {

    INVITE_PARTY((mc, username, args) -> PartyHelper.instance().inviteParty(mc, username, args)), // Confirmed to work!
    DISSOLVE_PARTY((mc, username, args) -> PartyHelper.instance().dissolveParty(mc, username)),
    UPDATE_PARTY((mc, username, args) -> PartyHelper.instance().updateParty(username, args)),

    CONFIRM_INVITE_PARTY((mc, username, args) -> PartyHelper.instance().confirmInviteParty(mc, username, args)), // Confirmed to work!
    CANCEL_INVITE_PARTY((mc, username, args) -> PartyHelper.instance()),

    ADD_FRIEND_REQUEST((mc, username, args) -> FriendsHandler.instance().addFriendRequest(mc, username)), // Confirmed to work! (except it crashes the receiver)

    ACCEPT_ADD_FRIEND((mc, username, args) -> FriendsHandler.instance().acceptAddFriend(username)),
    CANCEL_ADD_FRIEND((mc, username, args) -> FriendsHandler.instance().cancelAddFriend(username));

    public static final String PREFIX = "[SAOUI "; // TODO: config-based
    public static final String SUFFIX = "]";
    private final TriConsumer<Minecraft, String, String[]> action;

    CommandType(TriConsumer<Minecraft, String, String[]> action) {
        this.action = action;
    }

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
        return "saouiCommand" + this.name().replace("_", "");
    }

    public void action(Minecraft mc, String username, String[] args) {
        this.action.accept(mc, username, args);
    }

}

package com.bluexin.saoui.ui;

import com.bluexin.saoui.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class SAOFriendGUI extends SAOButtonGUI {

    private EntityPlayer friend;

    private SAOFriendGUI(SAOParentGUI gui, int xPos, int yPos, int w, int h, String name) {
        super(gui, SAOID.FRIEND, xPos, yPos, w, h, name, SAOIcon.NONE);
        enabled = false;
    }

    private SAOFriendGUI(SAOParentGUI gui, int xPos, int yPos, int w, String name) {
        this(gui, xPos, yPos, w, 20, name);
    }

    public SAOFriendGUI(SAOParentGUI gui, int xPos, int yPos, String name) {
        this(gui, xPos, yPos, 100, name);
    }

    @Override
    public void update(Minecraft mc) {
        final EntityPlayer player = getPlayer(mc);
        enabled = (player != null);

        if (enabled && FriendsHandler.instance().isFriend(player)) {
            highlight = true;
            icon = SAOIcon.NONE;
        } else {
            highlight = false;
            icon = SAOIcon.INVITE;
        }

        super.update(mc);
    }

    private EntityPlayer getPlayer(Minecraft mc) {
        if (friend == null || friend.isDead || !friend.isEntityAlive()) friend = findPlayer(mc);

        return friend;
    }

    private EntityPlayer findPlayer(Minecraft mc) {
        final List<EntityPlayer> players = StaticPlayerHelper.listOnlinePlayers(mc);

        for (final EntityPlayer player : players) {
            if (StaticPlayerHelper.getName(player).equals(caption)) {
                return player;
            }
        }

        return null;
    }
}


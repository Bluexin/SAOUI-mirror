package com.thejackimonster.saoui.ui;

import com.thejackimonster.saoui.SAOMod;
import com.thejackimonster.saoui.util.SAOID;
import com.thejackimonster.saoui.util.SAOIcon;
import com.thejackimonster.saoui.util.SAOParentGUI;
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
        this(gui, xPos, yPos, 150, name);
    }

    @Override
    public void update(Minecraft mc) {
        final EntityPlayer player = getPlayer(mc);
        enabled = (player != null);

        if ((enabled) && (SAOMod.isFriend(player))) {
            highlight = true;
            icon = SAOIcon.NONE;
        } else {
            highlight = false;
            icon = SAOIcon.INVITE;
        }

        super.update(mc);
    }

    private EntityPlayer getPlayer(Minecraft mc) {
        if ((friend == null) || (friend.isDead) || (!friend.isEntityAlive())) {
            friend = findPlayer(mc);
        }

        return friend;
    }

    private EntityPlayer findPlayer(Minecraft mc) {
        final List<EntityPlayer> players = SAOMod.listOnlinePlayers(mc);

        for (final EntityPlayer player : players) {
            if (SAOMod.getName(player).equals(caption)) {
                return player;
            }
        }

        return null;
    }

}

package com.bluexin.saoui.ui;

import com.bluexin.saoui.SAOMod;
import com.bluexin.saoui.util.SAOParentGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class SAOFriendsGUI extends SAOListGUI {

    public SAOFriendsGUI(Minecraft mc, SAOParentGUI gui, int xPos, int yPos, int w, int h) {
        super(gui, xPos, yPos, w, h);
        init(mc);
    }

    private void init(Minecraft mc) {
        final List<EntityPlayer> list = SAOMod.listOnlinePlayers(mc);

        if (list.contains(mc.thePlayer)) {
            list.remove(mc.thePlayer);
        }

        elements.addAll(list.stream().map(player -> new SAOFriendGUI(this, 0, 0, SAOMod.getName(player))).collect(Collectors.toList()));
    }

}

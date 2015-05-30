package com.thejackimonster.saoui.util;

import com.thejackimonster.saoui.SAOMod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class SAOResources {

    public static final ResourceLocation gui = new ResourceLocation(SAOMod.MODID, "textures/gui.png");
    public static final ResourceLocation icons = new ResourceLocation(SAOMod.MODID, "textures/icons.png");
    public static final ResourceLocation effects = new ResourceLocation(SAOMod.MODID, "textures/gui.png");
    public static final ResourceLocation entities = new ResourceLocation(SAOMod.MODID, "textures/entities.png");

    private SAOResources() {
    }

    public static final String FRIEND_REQUEST_TITLE = "Friend Request";
    public static final String FRIEND_REQUEST_TEXT = "%s wants to add you as friend.";

    public static final String PARTY_INVITATION_TITLE = "Invite";
    public static final String PARTY_INVITATION_TEXT = "%s invites you to join a party.";

    public static final String PARTY_DISSOLVING_TITLE = "Dissolve";
    public static final String PARTY_DISSOLVING_TEXT = "You will disband your party?";

    public static final String PARTY_LEAVING_TITLE = "Leave";
    public static final String PARTY_LEAVING_TEXT = "You will leave your party?";

    public static final String MESSAGE_TITLE = "Message";
    public static final String MESSAGE_FROM = "from %s";

    public static final String DEAD_ALERT = "You are dead";

}

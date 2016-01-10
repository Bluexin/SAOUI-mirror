package com.bluexin.saoui.util;

import com.bluexin.saoui.SAOMod;
import com.bluexin.saoui.SAOWindowViewGUI;
import com.bluexin.saoui.commands.Command;
import com.bluexin.saoui.commands.CommandType;
import com.bluexin.saoui.ui.SAOConfirmGUI;
import com.bluexin.saoui.ui.SAOWindowGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

import java.util.List;
import java.util.stream.Stream;

/**
 * Part of SAOUI
 *
 * @author Bluexin
 */
public class PartyHelper {
    private static PartyHelper instance = new PartyHelper();
    private String[] party;
    private int partyTicks;

    private PartyHelper() {

    }

    public static PartyHelper instance() {
        return instance;
    }

    public void inviteParty(Minecraft mc, String username, String... args) {
        if (!isPartyMember(StaticPlayerHelper.getName(mc))) {
            final GuiScreen keepScreen = mc.currentScreen;
            final boolean ingameFocus = mc.inGameHasFocus;

            final String text = StatCollector.translateToLocalFormatted(ConfigHandler._PARTY_INVITATION_TEXT, username);

            mc.displayGuiScreen(SAOWindowViewGUI.viewConfirm(ConfigHandler._PARTY_INVITATION_TITLE, text, (element, action, data) -> {
                final SAOID id = element.ID();

                if (id == SAOID.CONFIRM) {
                    party = args.length > 0 ? args : null;

                    if (party != null) partyTicks = 1000;

                    new Command(CommandType.CONFIRM_INVITE_PARTY, username).send(mc);
                } else new Command(CommandType.CANCEL_INVITE_PARTY, username).send(mc);

                mc.displayGuiScreen(keepScreen);

                if (ingameFocus) mc.setIngameFocus();
                else mc.setIngameNotInFocus();
            }));

            if (ingameFocus) mc.setIngameNotInFocus();
        }
    }

    public String[] listPartyMembers() {
        return party;
    }

    public boolean isPartyMember(String username) {
        return party != null && Stream.of(party).anyMatch(member -> member.equals(username));
    }

    public boolean isPartyLeader(String username) {
        return (party != null) && (party[0].equals(username));
    }

    private void addParty(Minecraft mc, String username) {
        if ((party != null) && (!isPartyMember(username))) {
            final String[] resized = new String[party.length + 1];

            System.arraycopy(party, 0, resized, 0, party.length);
            resized[party.length] = username;

            party = resized;

            updateParty(mc);
        }
    }

    private void removeParty(Minecraft mc, String username) {
        if (isPartyMember(username)) {
            final String[] resized = new String[party.length - 1];
            int index = 0;

            for (final String member : party) {
                if (!member.equals(username)) {
                    resized[index++] = member;
                }
            }

            if (resized.length > 1) {
                party = resized;
                updateParty(mc);
            } else {
                party = null;
            }
        }
    }

    public void updateParty(Minecraft mc) {
        if (party != null)
            Stream.of(party).filter(pl -> !pl.equals(StaticPlayerHelper.getName(mc))).forEach(member -> new Command(CommandType.UPDATE_PARTY, member, party));
    }

    public boolean createParty(Minecraft mc, double range) {
        // TODO: this looks suspicious...
        final List<EntityPlayer> found = StaticPlayerHelper.listOnlinePlayers(mc, true, range);

        if (found.contains(mc.thePlayer)) found.remove(mc.thePlayer);

        if (found.size() > 0) {
            party = new String[]{StaticPlayerHelper.getName(mc)};

            partyTicks = 10000;
            found.forEach(player -> inviteParty(mc, StaticPlayerHelper.getName(player)));

            return true;
        } else return false;
    }

    public void inviteParty(Minecraft mc, String username) {
        if (party != null && !isPartyMember(username))
            new Command(CommandType.INVITE_PARTY, username, party[0]).send(mc);
    }

    public void dissolveParty(Minecraft mc) {
        if (party != null) {
            if (party[0].equals(StaticPlayerHelper.getName(mc)))
                Stream.of(party).skip(1).forEach(member -> new Command(CommandType.DISSOLVE_PARTY, member).send(mc));
            else new Command(CommandType.DISSOLVE_PARTY, party[0]).send(mc);
        }

        partyTicks = 0;
        party = null;
    }

    public void updateParty(String username, String[] args) {
        if (isPartyLeader(username)) party = args.length <= 1 ? null : args;
    }

    public void dissolveParty(Minecraft mc, String username) {
        if (isPartyLeader(StaticPlayerHelper.getName(mc))) removeParty(mc, username);
        else if (isPartyLeader(username)) {
            final SAOWindowGUI window = SAOMod.getWindow(mc);

            if (window != null && window.getTitle().equals(ConfigHandler._PARTY_INVITATION_TITLE) && window instanceof SAOConfirmGUI)
                ((SAOConfirmGUI) window).cancel();

            party = null;
        }
    }

    public void confirmInviteParty(Minecraft mc, String username, String... args) {
        if (isPartyLeader(StaticPlayerHelper.getName(mc))) {
            final boolean inParty = isPartyMember(username);

            if ((inParty) && (args.length > 0)) addParty(mc, args[0]);
            else addParty(mc, username);
        } else if (isPartyMember(StaticPlayerHelper.getName(mc)))
            new Command(CommandType.CONFIRM_INVITE_PARTY, username, party[0]).send(mc);
        else new Command(CommandType.DISSOLVE_PARTY, username).send(mc);
    }
}

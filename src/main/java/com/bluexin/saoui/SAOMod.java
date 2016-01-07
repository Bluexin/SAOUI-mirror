package com.bluexin.saoui;

import com.bluexin.saoui.ui.SAOConfirmGUI;
import com.bluexin.saoui.ui.SAOWindowGUI;
import com.bluexin.saoui.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Mod(modid = SAOMod.MODID, name = SAOMod.NAME, version = SAOMod.VERSION)
@SideOnly(Side.CLIENT)
public class SAOMod {
    // TODO: fix threading issues (see inspection)
    public static final String MODID = "saoui";
    public static final String NAME = "Sword Art Online UI";
    public static final String VERSION = "1.3";
    public static final float UNKNOWN_TIME_DELAY = -1F;
    private static final double MAX_RANGE = 256.0D;
    private static final float HEALTH_ANIMATION_FACTOR = 0.075F;
    private static final float HEALTH_FRAME_FACTOR = HEALTH_ANIMATION_FACTOR * HEALTH_ANIMATION_FACTOR * 0x40 * 0x64;
    public static boolean IS_SPRINTING = false; // TODO: move somewhere else, maybe make skills have a activate/deactivate thing
    public static boolean IS_SNEAKING = false;
    public static Map<UUID, SAOColorCursor> colorStates;
    public static int REPLACE_GUI_DELAY = 0;
    public static boolean verChecked = false;
    public static boolean replaceGUI;
    private static Map<UUID, Float> healthSmooth;
    private static Map<UUID, Float> hungerSmooth;
    private static Configuration config;
    private static File friendsFile;
    private static String[] friends;
    private static List<SAOFriendRequest> friendRequests;
    private static String[] party;
    private static int partyTicks;

    private static List<EntityPlayer> listOnlinePlayers(Minecraft mc, boolean search, double range) {
        final List<EntityPlayer> players = new ArrayList<>();

        if (mc.thePlayer == null) {
            return players;
        }

        NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
        @SuppressWarnings("unchecked")
        Collection<Object> list = nethandlerplayclient.func_175106_d();

        if (!search) {
            range = MAX_RANGE;
        }

        final AxisAlignedBB box = AxisAlignedBB.fromBounds(
                mc.thePlayer.posX - range, mc.thePlayer.posY - range, mc.thePlayer.posZ - range,
                mc.thePlayer.posX + range, mc.thePlayer.posY + range, mc.thePlayer.posZ + range
        );

        @SuppressWarnings("unchecked")
        final List<Object> entities = mc.theWorld.getEntitiesWithinAABB(EntityPlayer.class, box);

        list.stream().filter(element -> element instanceof NetworkPlayerInfo).forEach(element -> {
            final NetworkPlayerInfo info = (NetworkPlayerInfo) element;
            final String infoName = unformatName(info.getGameProfile().getName());
            entities.stream().filter(ent -> ent instanceof EntityPlayer).map(ent -> (EntityPlayer) ent)
                    .filter(pl -> infoName.equals(getName(pl))).forEach(players::add);
        });

        return players;
    }

    public static List<EntityPlayer> listOnlinePlayers(Minecraft mc) {
        return listOnlinePlayers(mc, true, mc.gameSettings.renderDistanceChunks * 16.0D);
    }

    public static EntityPlayer findOnlinePlayer(Minecraft mc, String username) {
        return listOnlinePlayers(mc).stream().filter(player -> getName(player).equals(username)).findAny().orElse(null);
    }

    public static boolean[] isOnline(Minecraft mc, String[] names) { // TODO: update a boolean[] upon player join server? (/!\ client-side)
        final List<EntityPlayer> players = listOnlinePlayers(mc);
        final boolean[] online = new boolean[names.length];

        for (int i = 0; i < names.length; i++) {
            final int index = i;
            online[i] = players.stream().anyMatch(player -> getName(player).equals(names[index]));
        }

        return online;
    }

    public static boolean isOnline(Minecraft mc, String name) {
        return isOnline(mc, new String[]{name})[0];
    }

    private static void sendSAOCommand(Minecraft mc, SAOCommand command, String username, String... args) {
        if (mc.thePlayer == null || !SAOOption.CLIENT_CHAT_PACKETS.getValue()) return;

        final String format = I18n.format("commands.message.usage");
        final String cmd = format.substring(0, format.indexOf(' '));

        final String message = SAOJ8String.join(" ", cmd, username, String.valueOf(command), SAOJ8String.join(" ", args));

        mc.thePlayer.sendChatMessage(message);
    }

    //TODO: move this to the eventHandler or some command processor (maybe in the enum)
    public static void receiveSAOCommand(final Minecraft mc, SAOCommand command, final String username, final String... args) {
        if (mc.thePlayer == null || !SAOOption.CLIENT_CHAT_PACKETS.getValue()) return;

        if (command == SAOCommand.INVITE_PARTY) {
            if (!isPartyMember(getName(mc))) {
                final GuiScreen keepScreen = mc.currentScreen;
                final boolean ingameFocus = mc.inGameHasFocus;

                final String text = StatCollector.translateToLocalFormatted(ConfigHandler._PARTY_INVITATION_TEXT, username);

                mc.displayGuiScreen(SAOWindowViewGUI.viewConfirm(ConfigHandler._PARTY_INVITATION_TITLE, text, (element, action, data) -> {
                    final SAOID id = element.ID();

                    if (id == SAOID.CONFIRM) {
                        party = args.length > 0 ? args : null;

                        if (party != null) partyTicks = 1000;

                        sendSAOCommand(mc, SAOCommand.CONFIRM_INVITE_PARTY, username);
                    } else sendSAOCommand(mc, SAOCommand.CANCEL_INVITE_PARTY, username);

                    mc.displayGuiScreen(keepScreen);

                    if (ingameFocus) mc.setIngameFocus();
                    else mc.setIngameNotInFocus();
                }));

                if (ingameFocus) mc.setIngameNotInFocus();
            }
        } else if (command == SAOCommand.DISSOLVE_PARTY) {
            if (isPartyLeader(getName(mc))) removeParty(mc, username);
            else if (isPartyLeader(username)) {
                final SAOWindowGUI window = getWindow(mc);

                if (window != null && window.getTitle().equals(ConfigHandler._PARTY_INVITATION_TITLE) && window instanceof SAOConfirmGUI)
                    ((SAOConfirmGUI) window).cancel();

                party = null;
            }
        } else if (command == SAOCommand.UPDATE_PARTY) { // TODO: check! (looks suspicious to me)
            if (isPartyLeader(username)) party = args.length <= 1 ? null : args;
        } else if (command == SAOCommand.CONFIRM_INVITE_PARTY) {
            if (isPartyLeader(getName(mc))) {
                final boolean inParty = isPartyMember(username);

                if ((inParty) && (args.length > 0)) addParty(mc, args[0]);
                else addParty(mc, username);
            } else if (isPartyMember(getName(mc))) sendSAOCommand(mc, command, party[0], username);
            else sendSAOCommand(mc, SAOCommand.DISSOLVE_PARTY, username);
        } else if (command == SAOCommand.ADD_FRIEND_REQUEST) {
            if (!isFriend(username)) {
                final GuiScreen keepScreen = mc.currentScreen;
                final boolean ingameFocus = mc.inGameHasFocus;

                final String text = StatCollector.translateToLocalFormatted(ConfigHandler._FRIEND_REQUEST_TEXT, username);

                mc.displayGuiScreen(SAOWindowViewGUI.viewConfirm(ConfigHandler._FRIEND_REQUEST_TITLE, text, (element, action, data) -> {
                    final SAOID id = element.ID();

                    if (id == SAOID.CONFIRM && (isFriend(username) || addFriends(username)))
                        sendSAOCommand(mc, SAOCommand.ACCEPT_ADD_FRIEND, username);
                    else sendSAOCommand(mc, SAOCommand.CANCEL_ADD_FRIEND, username);

                    mc.displayGuiScreen(keepScreen);

                    if (ingameFocus) mc.setIngameFocus();
                    else mc.setIngameNotInFocus();
                }));

                if (ingameFocus) mc.setIngameNotInFocus();
            } else sendSAOCommand(mc, SAOCommand.ACCEPT_ADD_FRIEND, username);
        } else if (command == SAOCommand.ACCEPT_ADD_FRIEND) {
            synchronized (friendRequests) {
                int index = -1;

                for (int i = 0; i < friendRequests.size(); i++)
                    if (friendRequests.get(i).equals(username)) {
                        index = i;
                        break;
                    }

                if (index >= 0 && (isFriend(username) || addFriends(username))) friendRequests.remove(index);
            }
        } else if (command == SAOCommand.CANCEL_ADD_FRIEND) {
            synchronized (friendRequests) {
                int index = -1;

                for (int i = 0; i < friendRequests.size(); i++)
                    if (friendRequests.get(i).equals(username)) {
                        index = i;
                        break;
                    }

                if (index >= 0) friendRequests.remove(index);
            }
        }
    }

    private static String[] loadFriends() {
        try {
            final FileInputStream stream = new FileInputStream(friendsFile);
            final String[] friends;

            if (stream.available() != 0) {
                final int count = (stream.read() & 0xFF);

                friends = new String[count];

                for (int i = 0; i < count; i++) {
                    final int length = (stream.read() & 0xFF);
                    final byte[] bytes = new byte[length];

                    stream.read(bytes, 0, length);

                    friends[i] = new String(bytes);
                }
            } else friends = new String[0];

            stream.close();

            return friends;
        } catch (IOException e) {
            return new String[0];
        }
    }

    public static String[] listFriends() {
        if (friends == null) friends = loadFriends();

        return friends;
    }

    public static void addFriendRequest(Minecraft mc, String... names) {
        synchronized (friendRequests) {
            for (final String name : names)
                if (!friendRequests.contains(new SAOFriendRequest(name, 10000)) && !isFriend(name)) {
                    friendRequests.add(new SAOFriendRequest(name, 10000));
                    sendSAOCommand(mc, SAOCommand.ADD_FRIEND_REQUEST, name);
                }
        }
    }

    private static boolean addFriends(String... names) {
        friends = listFriends();
        final ArrayList<String> newNames = new ArrayList<>();

        Stream.of(names).forEach(name -> {
            if (Stream.of(friends).noneMatch(friend -> friend.equals(name))) newNames.add(name);
        });

        return newNames.size() <= 0 || addRawFriends((String[]) newNames.toArray());
    }

    private static boolean isFriend(String name) {
        return Stream.of(listFriends()).anyMatch(friend -> friend.equals(name));
    }

    public static boolean isFriend(EntityPlayer player) {
        return isFriend(getName(player));
    }

    private static boolean addRawFriends(String[] names) {
        friends = listFriends();

        final String[] resized = new String[friends.length + names.length];

        System.arraycopy(friends, 0, resized, 0, friends.length);
        System.arraycopy(names, 0, resized, friends.length, names.length);

        if (writeFriends(resized)) {
            friends = resized;
            return true;
        } else return false;
    }

    private static boolean writeFriends(String[] friends) {
        final String[] data = friends == null ? new String[0] : friends;

        synchronized (friendsFile) {
            try (FileOutputStream stream = new FileOutputStream(friendsFile)) {
                final int count = (data.length % 0x100);
                stream.write(count);

                for (int i = 0; i < count; i++) {
                    final byte[] bytes = data[i].getBytes();
                    final int length = (bytes.length % 0x100);

                    stream.write(length);
                    stream.write(bytes, 0, length);
                }

                stream.flush();
                stream.close();

                return true;
            } catch (IOException e) {
                if (ConfigHandler.DEBUG) e.printStackTrace();

                return false;
            }
        }
    }

    public static String[] listPartyMembers() {
        return party;
    }

    public static boolean isPartyMember(String username) {
        return party != null && Stream.of(party).anyMatch(member -> member.equals(username));
    }

    public static boolean isPartyLeader(String username) {
        return (party != null) && (party[0].equals(username));
    }

    private static void addParty(Minecraft mc, String username) {
        if ((party != null) && (!isPartyMember(username))) {
            final String[] resized = new String[party.length + 1];

            System.arraycopy(party, 0, resized, 0, party.length);
            resized[party.length] = username;

            party = resized;

            updateParty(mc);
        }
    }

    private static void removeParty(Minecraft mc, String username) {
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

    private static void updateParty(Minecraft mc) {
        if (party != null) {
            final String memberString = SAOJ8String.join(" ", party);
            Stream.of(party).filter(pl -> !pl.equals(getName(mc))).forEach(member -> sendSAOCommand(mc, SAOCommand.UPDATE_PARTY, member, memberString));
        }
    }

    public static boolean createParty(Minecraft mc, double range) {
        // TODO: this looks suspicious...
        final List<EntityPlayer> found = listOnlinePlayers(mc, true, range);

        if (found.contains(mc.thePlayer)) found.remove(mc.thePlayer);

        if (found.size() > 0) {
            party = new String[]{getName(mc)};

            partyTicks = 10000;
            found.forEach(player -> inviteParty(mc, getName(player)));

            return true;
        } else return false;
    }

    public static void inviteParty(Minecraft mc, String username) {
        if (party != null && !isPartyMember(username)) sendSAOCommand(mc, SAOCommand.INVITE_PARTY, username, party[0]);
    }

    public static void dissolveParty(Minecraft mc) {
        if (party != null) {
            if (party[0].equals(getName(mc)))
                Stream.of(party).skip(1).forEach(member -> sendSAOCommand(mc, SAOCommand.DISSOLVE_PARTY, member));
            else sendSAOCommand(mc, SAOCommand.DISSOLVE_PARTY, party[0]);
        }

        partyTicks = 0;
        party = null;
    }

    public static String unformatName(String name) {
        int index = name.indexOf("�");

        while (index != -1) {
            if (index + 1 < name.length()) name = name.replace(name.substring(index, index + 2), "");
            else name = name.replace("�", "");

            index = name.indexOf("�");
        }

        return name;
    }

    private static SAOWindowGUI getWindow(Minecraft mc) {
        return mc.currentScreen != null && mc.currentScreen instanceof SAOWindowViewGUI ? ((SAOWindowViewGUI) mc.currentScreen).getWindow() : null;
    }

    public static String getName(EntityPlayer player) {
        return player == null ? "" : player.getName();
    }

    public static String getName(Minecraft mc) {
        return getName(mc.thePlayer);
    }

    public static float getHealth(final Minecraft mc, final Entity entity, final float time) {
        if (SAOOption.SMOOTH_HEALTH.getValue()) {
            final float healthReal;
            final UUID uuid = entity.getUniqueID();

            if (entity instanceof EntityLivingBase) healthReal = ((EntityLivingBase) entity).getHealth();
            else healthReal = entity.isDead ? 0F : 1F;

            if (healthSmooth.containsKey(uuid)) {
                float healthValue = healthSmooth.get(uuid);

                if ((healthReal <= 0) && (entity instanceof EntityLivingBase)) {
                    final float value = (float) (18 - ((EntityLivingBase) entity).deathTime) / 18;

                    if (value <= 0) healthSmooth.remove(uuid);

                    return healthValue * value;
                } else if (Math.round(healthValue * 10) != Math.round(healthReal * 10))
                    healthValue = healthValue + (healthReal - healthValue) * (gameTimeDelay(mc, time) * HEALTH_ANIMATION_FACTOR);
                else healthValue = healthReal;

                healthSmooth.put(uuid, healthValue);
                return healthValue;
            } else {
                healthSmooth.put(uuid, healthReal);
                return healthReal;
            }
        } else
            return (entity instanceof EntityLivingBase ? ((EntityLivingBase) entity).getHealth() : (entity.isDead ? 0F : 1F));
    }

    private static int gameFPS(Minecraft mc) {
        /*final List<String> output = new ArrayList<>();

        if (SAONewChatGUI.reformat(mc.debug, "%s fps, %s chunk updates", output)) {
            try {
                return Integer.parseInt(output.get(0));
            } catch (NumberFormatException e) {
                return mc.getLimitFramerate();
            }
        } else {
        }*/
        return mc.getLimitFramerate();
    }

    private static float gameTimeDelay(Minecraft mc, float time) {
        return time >= 0F ? time : HEALTH_FRAME_FACTOR / gameFPS(mc);
    }

    public static float getMaxHealth(final Entity entity) {
        return entity instanceof EntityLivingBase ? ((EntityLivingBase) entity).getMaxHealth() : 1F;
    }

    public static float getHungerFract(final Minecraft mc, final Entity entity, final float time) {
        if (!(entity instanceof EntityPlayer)) return 1.0F;
        EntityPlayer player = (EntityPlayer) entity;
        final float hunger;
        if (SAOOption.SMOOTH_HEALTH.getValue()) {
            final UUID uuid = entity.getUniqueID();

            hunger = player.getFoodStats().getFoodLevel();

            if (hungerSmooth.containsKey(uuid)) {
                float hungerValue = hungerSmooth.get(uuid);

                if (hunger <= 0) {
                    final float value = (float) (18 - player.deathTime) / 18;

                    if (value <= 0) hungerSmooth.remove(uuid);

                    return hungerValue * value;
                } else if (Math.round(hungerValue * 10) != Math.round(hunger * 10))
                    hungerValue = hungerValue + (hunger - hungerValue) * (gameTimeDelay(mc, time) * HEALTH_ANIMATION_FACTOR);
                else hungerValue = hunger;

                hungerSmooth.put(uuid, hungerValue);
                return hungerValue / 20.0F;
            } else {
                hungerSmooth.put(uuid, hunger);
                return hunger / 20.0F;
            }
        } else return player.getFoodStats().getFoodLevel() / 20.0F;
    }

    public static SAOColorState getColorState(final EntityPlayer entity) {
        final UUID uuid = entity.getUniqueID();

        if (!colorStates.containsKey(uuid)) colorStates.put(uuid, new SAOColorCursor());

        return colorStates.get(uuid).get();
    }

    public static void onDamagePlayer(final EntityPlayer entity) {
        final UUID uuid = entity.getUniqueID();

        if (colorStates.containsKey(uuid)) colorStates.get(uuid).set(SAOColorState.VIOLENT);
        else colorStates.put(uuid, new SAOColorCursor(SAOColorState.VIOLENT, true));
    }

    public static void onKillPlayer(final EntityPlayer entity) {
        final UUID uuid = entity.getUniqueID();

        if (colorStates.containsKey(uuid)) colorStates.get(uuid).set(SAOColorState.KILLER);
        else colorStates.put(uuid, new SAOColorCursor(SAOColorState.KILLER, true));
    }

    public static boolean isCreative(AbstractClientPlayer player) {
        NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(player.getGameProfile().getId());
        return networkplayerinfo != null && networkplayerinfo.getGameType() == WorldSettings.GameType.SPECTATOR;
    }

    @NetworkCheckHandler()
    public boolean matchModVersions(Map<String, String> remoteVersions, Side side) { // This will at least detect if the server has SAOsoc forge mod. Now to detect plugin
//        System.out.println(side + " handshake.\nContains saoui: " + remoteVersions.containsKey("saoui") + "\nContains saosoc: " + remoteVersions.containsKey("saosoc"));
        return true; // TODO: check if contains SAOSOC, which version?, save to some struct
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        FMLCommonHandler.instance().bus().register(new SAOEventHandler());
        MinecraftForge.EVENT_BUS.register(new SAOEventHandler());
        FMLCommonHandler.instance().bus().register(new SAORenderHandler());
        MinecraftForge.EVENT_BUS.register(new SAORenderHandler());

        ConfigHandler.preInit(event);

        friendRequests = new ArrayList<>();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        /*final Minecraft mc = Minecraft.getMinecraft();
        friendsFile = new File(mc.mcDataDir, ".sao_friends");

        if (!friendsFile.exists()) writeFriends(friends);

        friends = loadFriends();*/

        if (healthSmooth == null) healthSmooth = new HashMap<>();
        else healthSmooth.clear();

        if (hungerSmooth == null) hungerSmooth = new HashMap<>();
        else hungerSmooth.clear();

        if (colorStates == null) colorStates = new HashMap<>();
        else colorStates.clear();

        replaceGUI = true;
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        /*
        If some mobs don't get registered this way, that means the mods don't register their renderers at the right place.
         */
        final Minecraft mc = Minecraft.getMinecraft();
        RenderManager manager = mc.getRenderManager();
        manager.entityRenderMap.keySet().stream().filter(key -> key instanceof Class<?>).filter(key -> EntityLivingBase.class.isAssignableFrom((Class<?>) key)).forEach(key -> {
            final Object value = manager.entityRenderMap.get(key);

            if (value instanceof Render && !(value instanceof SAORenderBase)) {
                final Render render = new SAORenderBase((Render) value);
                manager.entityRenderMap.put(key, render);
                render.func_177068_d();
            }

        });
    }

}

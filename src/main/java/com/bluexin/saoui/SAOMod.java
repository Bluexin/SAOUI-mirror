package com.bluexin.saoui;

import com.bluexin.saoui.ui.SAOWindowGUI;
import com.bluexin.saoui.util.ConfigHandler;
import com.bluexin.saoui.util.FriendsHandler;
import com.bluexin.saoui.util.SAOGL;
import com.bluexin.saoui.util.SAOOption;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = SAOMod.MODID, name = SAOMod.NAME, version = SAOMod.VERSION)
@SideOnly(Side.CLIENT)
public class SAOMod {
    public static final String MODID = "saoui";
    public static final String NAME = "Sword Art Online UI";
    public static final String VERSION = "1.5";
    public static final float UNKNOWN_TIME_DELAY = -1F;
    public static final double MAX_RANGE = 256.0D;
    public static boolean IS_SPRINTING = false; // TODO: move somewhere else, maybe make skills have a activate/deactivate thing
    public static boolean IS_SNEAKING = false;
    public static boolean verChecked = false;
    public static boolean replaceGUI = SAORenderHandler.replaceGUI;
    // TODO: optimize things, ie remove public and static!

    @Mod.Instance(MODID)
    public static SAOMod instance;

    public static SAOWindowGUI getWindow(Minecraft mc) {
        return mc.currentScreen != null && mc.currentScreen instanceof SAOWindowViewGUI ? ((SAOWindowViewGUI) mc.currentScreen).getWindow() : null;
    }


    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(new SAOEventHandler());
        MinecraftForge.EVENT_BUS.register(new SAOEventHandler());
        FMLCommonHandler.instance().bus().register(new SAORenderHandler());
        MinecraftForge.EVENT_BUS.register(new SAORenderHandler());

        ConfigHandler.preInit(event);
        FriendsHandler.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        replaceGUI = true;
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        final Minecraft mc = Minecraft.getMinecraft();

        SAOGL.setFont(mc, SAOOption.CUSTOM_FONT.getValue());
        FMLInterModComms.sendRuntimeMessage(SAOMod.MODID, "VersionChecker", "addVersionCheck", "https://gitlab.com/saomc/PublicVersions/raw/master/saoui1.8ver.json");
    }

}

package com.bluexin.saoui;

import com.bluexin.saoui.ui.SAOWindowGUI;
import com.bluexin.saoui.util.ConfigHandler;
import com.bluexin.saoui.util.FriendsHandler;
import com.bluexin.saoui.util.SAOGL;
import com.bluexin.saoui.util.SAOOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@Mod(modid = SAOMod.MODID, name = SAOMod.NAME, version = SAOMod.VERSION)
@SideOnly(Side.CLIENT)
public class SAOMod {
    public static final String MODID = "saoui";
    public static final String NAME = "Sword Art Online UI";
    public static final String VERSION = "1.3";
    public static final float UNKNOWN_TIME_DELAY = -1F;
    public static final double MAX_RANGE = 256.0D;
    public static boolean IS_SPRINTING = false; // TODO: move somewhere else, maybe make skills have a activate/deactivate thing
    public static boolean IS_SNEAKING = false;
    public static int REPLACE_GUI_DELAY = 0;
    public static boolean verChecked = false;
    public static boolean replaceGUI;
    // TODO: optimize things, ie remove public and static!

    @Mod.Instance(MODID)
    public static SAOMod instance;

    public static SAOWindowGUI getWindow(Minecraft mc) {
        return mc.currentScreen != null && mc.currentScreen instanceof SAOWindowViewGUI ? ((SAOWindowViewGUI) mc.currentScreen).getWindow() : null;
    }

    @NetworkCheckHandler()
    public boolean matchModVersions(Map<String, String> remoteVersions, Side side) {
        return true;
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
        Map skinMap = (Map) ObfuscationReflectionHelper.getPrivateValue((Class) manager.getClass(), manager, "skinMap", "field_178636_l", "l");
        skinMap.keySet().stream().forEach(key -> {
            final Object value = skinMap.get(key);

            if (value instanceof Render && !(value instanceof SAORenderPlayer)) {
                final Render render = new SAORenderPlayer((Render) value);
                skinMap.put(key, render);
                render.func_177068_d();
            }
        });

        SAOGL.setFont(mc, SAOOption.CUSTOM_FONT.getValue());

        // FMLInterModComms?
    }

}

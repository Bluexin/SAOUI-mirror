package com.bluexin.saoui;

import com.bluexin.saoui.ui.SAOScreenGUI;
import com.bluexin.saoui.util.SAOOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class SAORenderHandler {

    public static final List<EntityLivingBase> deadHandlers = new ArrayList<>();
    public static boolean replaceGUI;
    public static int REPLACE_GUI_DELAY = 0;
    private final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void checkingameGUI(TickEvent.RenderTickEvent e) {
        boolean b = mc.ingameGUI instanceof SAOIngameGUI;
        if (mc.ingameGUI != null && SAOOption.DEFAULT_UI.getValue() == b)
            mc.ingameGUI = b ? new GuiIngameForge(mc) : new SAOIngameGUI(mc);

        deadHandlers.forEach(ent -> {
            final boolean deadStart = (ent.deathTime == 1);
            final boolean deadExactly = (ent.deathTime >= 18);
            if (deadStart) {
                ent.deathTime++;
                SAOSound.playAtEntity(ent, SAOSound.PARTICLES_DEATH);
            }

            if (deadExactly) {
                StaticRenderer.doSpawnDeathParticles(mc, ent);
                ent.setDead();
            }
        });
        deadHandlers.removeIf(ent -> ent.isDead);
    }

    @SubscribeEvent
    public void checkGuiInstance(TickEvent.RenderTickEvent e) {
        if ((mc.currentScreen == null) && (mc.inGameHasFocus)) replaceGUI = true;
        else if (replaceGUI) {
            if (mc.currentScreen != null && !(mc.currentScreen instanceof SAOScreenGUI)) {
                if (REPLACE_GUI_DELAY > 0) REPLACE_GUI_DELAY--;
                else if ((mc.currentScreen instanceof GuiIngameMenu) || ((mc.currentScreen instanceof GuiInventory) && (!SAOOption.DEFAULT_INVENTORY.getValue()))) {
                    final boolean inv = (mc.currentScreen instanceof GuiInventory);

                    mc.currentScreen.mc = mc;
                    if (mc.playerController.isInCreativeMode() && mc.currentScreen instanceof GuiInventory)
                        mc.displayGuiScreen(new GuiContainerCreative(mc.thePlayer));
                    else try {
                        SAOSound.play(mc, SAOSound.ORB_DROPDOWN);
                        mc.displayGuiScreen(new SAOIngameMenuGUI((GuiInventory) (inv ? mc.currentScreen : null)));
                        replaceGUI = false;
                    } catch (NullPointerException ignored) {
                    }
                } else if ((mc.currentScreen instanceof GuiGameOver) && (!SAOOption.DEFAULT_DEATH_SCREEN.getValue())) {
                    mc.currentScreen.mc = mc;

                    if (mc.ingameGUI instanceof SAOIngameGUI) {
                        try {
                            mc.displayGuiScreen(null);
                            mc.displayGuiScreen(new SAODeathGUI((GuiGameOver) mc.currentScreen));
                            replaceGUI = false;
                        } catch (NullPointerException ignored) {
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void renderPlayer(RenderPlayerEvent.Pre e) {
        StaticRenderer.render(e.renderer.getRenderManager(), e.entity, e.x, e.y, e.z);
    }

    @SubscribeEvent
    public void renderEntity(RenderLivingEvent.Pre e) {
        StaticRenderer.render(e.renderer.getRenderManager(), e.entity, e.x, e.y, e.z);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        SAORenderDispatcher.dispatch();
    }
}


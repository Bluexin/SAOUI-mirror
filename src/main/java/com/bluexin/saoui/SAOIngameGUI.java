package com.bluexin.saoui;

import com.bluexin.saoui.util.*;
import com.bluexin.saoui.ui.SAOIconGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

@SideOnly(Side.CLIENT)
public class SAOIngameGUI extends GuiIngame {

    private final SAONewChatGUI chatLine;
    private final Queue<String[]> messages;
    private final SAOIconGUI receivedMessage;

    private boolean openedMessage;

    public SAOIngameGUI(Minecraft mc) {
        super(mc);
        chatLine = new SAONewChatGUI(this, mc, persistantChatGUI);
        messages = new ArrayDeque<>();
        receivedMessage = new SAOIconGUI(null, SAOID.MESSAGE, 0, 0, SAOIcon.MESSAGE_RECEIVED);
        openedMessage = false;

        receivedMessage.visibility = 0;
        receivedMessage.highlight = true;
    }

    @Override
    public void renderGameOverlay(float time) {
        if (SAOOption.DEFAULT_UI.value) {
            super.renderGameOverlay(time);
            return;
        }

        drawOverlay(time);
    }

    private void drawOverlay(float time) {
        final ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        final int width = resolution.getScaledWidth();
        final int height = resolution.getScaledHeight();

        final FontRenderer fontRenderer = mc.fontRendererObj;

        mc.entityRenderer.setupOverlayRendering();

        SAOGL.glBlend(true);

        if (Minecraft.isFancyGraphicsEnabled()) {
            func_180480_a(this.mc.thePlayer.getBrightness(time), resolution);
        } else {
            SAOGL.tryBlendFuncSeparate(770, 771, 1, 0);
        }

        final ItemStack helmet = mc.thePlayer.inventory.armorItemInSlot(3);

        if (mc.gameSettings.thirdPersonView == 0 && helmet != null && helmet.getItem() == Item.getItemFromBlock(Blocks.pumpkin)) {
            func_180476_e(resolution);
        }

        if (!mc.thePlayer.isPotionActive(Potion.confusion)) {
            final float portalEffect = mc.thePlayer.prevTimeInPortal + (mc.thePlayer.timeInPortal - mc.thePlayer.prevTimeInPortal) * time;

            if (portalEffect > 0.0F) {
                func_180474_b(portalEffect, resolution);
            }
        }

        SAOGL.glColor(1.0F, 1.0F, 1.0F, 1.0F);
        SAOGL.glBindTexture(icons);
        SAOGL.glBlend(true);

        if ((SAOOption.CROSS_HAIR.value) && (showCrosshair())) {
            mc.mcProfiler.startSection("cross-hair");

            SAOGL.tryBlendFuncSeparate(775, 769, 1, 0);
            SAOGL.glAlpha(true);

            drawTexturedModalRect(width / 2 - 7, height / 2 - 7, 0, 0, 16, 16);

            SAOGL.glAlpha(false);
            SAOGL.tryBlendFuncSeparate(770, 771, 1, 0);

            mc.mcProfiler.endSection();
        }

        SAOGL.glStartUI(mc);

        if (mc.playerController.shouldDrawHUD() || SAOOption.FORCE_HUD.value) {
            drawHUD(time, fontRenderer);
        }

        SAOGL.glBlend(false);
        float f2;
        int k;
        int j1;

        if (this.mc.thePlayer.getSleepTimer() > 0) {
            this.mc.mcProfiler.startSection("sleep");

            SAOGL.glDepth(false);
            SAOGL.glAlpha(false);

            j1 = this.mc.thePlayer.getSleepTimer();
            f2 = (float) j1 / 100.0F;

            if (f2 > 1.0F) {
                f2 = 1.0F - (float) (j1 - 100) / 10.0F;
            }

            k = (int) (220.0F * f2) << 24 | 1052704;
            drawRect(0, 0, width, height, k);

            SAOGL.glAlpha(true);
            SAOGL.glDepth(true);

            this.mc.mcProfiler.endSection();
        }

        SAOGL.glColor(1.0F, 1.0F, 1.0F, 1.0F);

        mc.mcProfiler.startSection("inventorySlots");

        SAOGL.glAlpha(true);

        SAOGL.glBindTexture(SAOOption.ORIGINAL_UI.value? SAOResources.gui: SAOResources.guiCustom);
        SAOGL.glColor(1, 1, 1, 1);

        final InventoryPlayer inv = mc.thePlayer.inventory;
        final int slotCount = 9;
        final int slotsY = (height - (slotCount * 22)) / 2;

        for (int i = 0; i < slotCount; i++) {
            SAOGL.glColorRGBA(i == inv.currentItem ? 0xE0BE62FF : 0xCDCDCDFF);
            SAOGL.glTexturedRect(width - 24, slotsY + (22 * i), zLevel, 0, 25, 20, 20);
        }

        SAOGL.glColor(1, 1, 1, 1);

        SAOGL.glRescaleNormal(true);

        RenderHelper.enableGUIStandardItemLighting();

        for (int i = 0; i < slotCount; i++) {
            super.renderHotbarItem(i, width - 22, slotsY + 2 + (22 * i), time, mc.thePlayer);
        }

        RenderHelper.disableStandardItemLighting();

        SAOGL.glRescaleNormal(false);
        SAOGL.glBlend(false);

        mc.mcProfiler.endSection();

        SAOGL.glColor(1, 1, 1, 1);

        if (mc.gameSettings.heldItemTooltips) {
            drawTooltips(fontRenderer, width, height);
        }

        if (mc.gameSettings.showDebugInfo) {
            overlayDebug.renderDebugInfo(resolution);
        }

        int l;

        if (this.recordPlayingUpFor > 0) {
            this.mc.mcProfiler.startSection("overlayMessage");
            f2 = (float) this.recordPlayingUpFor - time;
            k = (int) (f2 * 255.0F / 20.0F);

            if (k > 255) {
                k = 255;
            }

            if (k > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) (width / 2), (float) (height - 68), 0.0F);

                SAOGL.glBlend(true);
                SAOGL.tryBlendFuncSeparate(770, 771, 1, 0);

                l = 16777215;

                if (this.recordIsPlaying) {
                    l = Color.HSBtoRGB(f2 / 50.0F, 0.7F, 0.6F) & 16777215;
                }

                this.func_175179_f().drawString(this.recordPlaying, -this.func_175179_f().getStringWidth(this.recordPlaying) / 2, -4, l + (k << 24 & -16777216));

                SAOGL.glBlend(false);

                GlStateManager.popMatrix();
            }

            this.mc.mcProfiler.endSection();
        }

        if (this.field_175195_w > 0) {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            f2 = (float) this.field_175195_w - time;
            k = 255;

            if (this.field_175195_w > this.field_175193_B + this.field_175192_A) {
                float f3 = (float) (this.field_175199_z + this.field_175192_A + this.field_175193_B) - f2;
                k = (int) (f3 * 255.0F / (float) this.field_175199_z);
            }

            if (this.field_175195_w <= this.field_175193_B) {
                k = (int) (f2 * 255.0F / (float) this.field_175193_B);
            }

            k = MathHelper.clamp_int(k, 0, 255);

            if (k > 8) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float) (width / 2), (float) (height / 2), 0.0F);

                SAOGL.glBlend(true);
                SAOGL.tryBlendFuncSeparate(770, 771, 1, 0);

                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0F, 4.0F, 4.0F);
                l = k << 24 & -16777216;
                this.func_175179_f().drawString(this.field_175201_x, (float) (-this.func_175179_f().getStringWidth(this.field_175201_x) / 2), -10.0F, 16777215 | l, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                this.func_175179_f().drawString(this.field_175200_y, (float) (-this.func_175179_f().getStringWidth(this.field_175200_y) / 2), 5.0F, 16777215 | l, true);
                GlStateManager.popMatrix();

                SAOGL.glBlend(false);

                GlStateManager.popMatrix();
            }

            this.mc.mcProfiler.endSection();
        }

        ScoreObjective scoreobjective = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1);

        if (scoreobjective != null) {
            GlStateManager.translate(-30, 0, 0);

            func_180475_a(scoreobjective, resolution);

            GlStateManager.translate(30, 0, 0);
        }

        SAOGL.glBlend(true);
        SAOGL.tryBlendFuncSeparate(770, 771, 1, 0);
        SAOGL.glAlpha(false);

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, (float) (height - 48), 0.0F);

        mc.mcProfiler.startSection("chat");
        chatLine.drawChat(updateCounter);
        mc.mcProfiler.endSection();

        GlStateManager.popMatrix();

        scoreobjective = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(0);

        if (this.mc.gameSettings.keyBindPlayerList.isPressed() && (!this.mc.isIntegratedServerRunning() || this.mc.thePlayer.sendQueue.func_175106_d().size() > 1 || scoreobjective != null)) {
            this.overlayPlayerList.func_175246_a(true);
            this.overlayPlayerList.func_175249_a(width, mc.theWorld.getScoreboard(), scoreobjective);
        } else {
            this.overlayPlayerList.func_175246_a(false);
        }

        SAOGL.glColor(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.disableLighting();

        SAOGL.glAlpha(true);

        if (!messages.isEmpty()) {
            receivedMessage.x = 8;
            receivedMessage.y = height * 3 / 4;

            receivedMessage.visibility = 1;
        } else {
            receivedMessage.visibility = 0;
        }

        if (receivedMessage.visibility > 0) {
            receivedMessage.update(mc);
            receivedMessage.draw(mc, Mouse.getX(), Mouse.getY());

            final String numberString = String.valueOf(messages.size());
            SAOGL.glString(numberString, receivedMessage.getX(false), receivedMessage.getY(false), SAOColor.HOVER_FONT_COLOR, true);
        }

        final Item clock = Item.getByNameOrId("clock");

        if ((clock != null) && (mc.thePlayer.inventory.hasItem(clock))) {
            final long worldTime = mc.theWorld.getWorldTime();
            final long timeHour = (worldTime / 1000) % 24;
            final long timeMinute = (worldTime % 1000) * 60 / 1000;

            final String timeString = (timeHour < 10 ? "0" : "") + String.valueOf(timeHour) + ":" + (timeMinute < 10 ? "0" : "") + String.valueOf(timeMinute);

            SAOGL.glString(timeString, width - (SAOGL.glStringWidth(timeString) + 8), 8, SAOColor.DEFAULT_COLOR, true);
        }

        SAOGL.glEndUI(mc);
    }

    private void drawHUD(float time, FontRenderer fontRenderer) {
        final String username = mc.thePlayer.getName();
        int maxNameWidth = fontRenderer.getStringWidth(username);

        mc.mcProfiler.startSection("username");

        SAOGL.glColor(1, 1, 1, 1);
        SAOGL.glBindTexture(SAOOption.ORIGINAL_UI.value? SAOResources.gui: SAOResources.guiCustom);

        //SAOGL.glTexturedRect(2, 2, zLevel, 0, 0, 10, 15); // I'll leave these old ones in there
        //SAOGL.glTexturedRect(13, 2, zLevel, 10, 0, 5, 15);
        SAOGL.glTexturedRect(2, 2, zLevel, 0, 0, 16, 15);

        final int usernameBoxes = 1 + (maxNameWidth + 4) / 5;

        SAOGL.glTexturedRect(18, 2, zLevel, usernameBoxes * 5, 15, 16, 0, 5, 15);
        SAOGL.glString(fontRenderer, username, 18, 3 + (15 - fontRenderer.FONT_HEIGHT) / 2, 0xFFFFFFFF);

        SAOGL.glBindTexture(SAOOption.ORIGINAL_UI.value? SAOResources.gui: SAOResources.guiCustom);
        SAOGL.glColor(1, 1, 1, 1);

        mc.mcProfiler.endSection();

        mc.mcProfiler.startSection("healthBar");

        final int offsetUsername = 18 + usernameBoxes * 5;
        final int healthBarWidth = 234;

        SAOGL.glTexturedRect(offsetUsername, 2, zLevel, 21, 0, healthBarWidth, 15);

        final int healthWidth = 216;
        final int healthHeight = SAOOption.ORIGINAL_UI.value? 9 : 4;

        final int healthValue = (int) (SAOMod.getHealth(mc, mc.thePlayer, time) / SAOMod.getMaxHealth(mc.thePlayer) * healthWidth);
        SAOHealthStep.getStep(mc, mc.thePlayer, time).glColor();

        if (SAOOption.ORIGINAL_UI.value) {
            int h = healthHeight;
            for (int i = 0; i < healthValue; i++) {
                SAOGL.glTexturedRect(offsetUsername + 1 + i, 5, zLevel, (healthHeight - h), 15, 1, h);

                if (((i >= 105) && (i <= 110)) || (i >= healthValue - h)) {
                    h--;

                    if (h <= 0) {
                        break;
                    }
                }
            }
        } else {
            int h = healthValue <= 12? 12 - healthValue: 0;
            int o = healthHeight;
            int stepOne = (int) (healthWidth / 3.0F - 3);
            int stepTwo = (int) (healthWidth / 3.0F * 2.0F - 3);
            int stepThree = healthWidth - 3;
            for (int i = 0; i < healthValue; i++) {
                SAOGL.glTexturedRect(offsetUsername + 4 + i, 6 + (healthHeight - o), zLevel, h, 236 + (healthHeight - o), 1, o);
                if (healthValue < healthWidth && i >= healthValue - 3) o--;

                if (healthValue <= 12) {
                    h++;
                    if (h > 12) break;
                } else if ((i >= stepOne && i <= stepOne + 3) || (i >= stepTwo && i <= stepTwo + 3) || (i >= stepThree)) {
                    h++;

                    if (h > 12) {
                        break;
                    }
                }
            }

            if (healthValue >= stepTwo && healthValue < stepThree)
                SAOGL.glTexturedRect(offsetUsername + healthValue, 6, zLevel, 11, 245, 7, 4);
            if (healthValue >= stepOne && healthValue < stepTwo + 4)
                SAOGL.glTexturedRect(offsetUsername + healthValue, 6, zLevel, 4, 245, 7, 4);
            if (healthValue < stepOne + 4 && healthValue > 0) {
                SAOGL.glTexturedRect(offsetUsername + healthValue + 2, 6, zLevel, 0, 245, 4, 4);
                for (int i = 0; i < healthValue - 2; i++) SAOGL.glTexturedRect(offsetUsername + i  + 4, 6, zLevel, 0, 245, 4, 4);
            }

            final int foodValue = (int) (SAOMod.getHungerFract(mc.thePlayer) * healthWidth);
            h = foodValue < 12? 12 - foodValue: 0;
            o = healthHeight;
            SAOGL.glColorRGBA(0x8EE1E8);
            for (int i = 0; i < foodValue; i++) {
                SAOGL.glTexturedRect(offsetUsername + i + 4, 9, zLevel, h, 240, 1, o);
                if (foodValue < healthWidth && i >= foodValue - 3) o--;

                if (foodValue <= 12) {
                    h++;
                    if (h > 12) break;
                } else if ((i >= stepOne && i <= stepOne + 3) || (i >= stepTwo && i <= stepTwo + 3) || (i >= stepThree)) {
                    h++;

                    if (h > 12) {
                        break;
                    }
                }
            }

            if (foodValue >= stepTwo && foodValue < stepThree)
                SAOGL.glTexturedRect(offsetUsername + foodValue, 9, zLevel, 11, 249, 7, 4);
            if (foodValue >= stepOne && foodValue < stepTwo + 4)
                SAOGL.glTexturedRect(offsetUsername + foodValue, 9, zLevel, 4, 249, 7, 4);
            if (foodValue < stepOne + 4 && foodValue > 0) {
                SAOGL.glTexturedRect(offsetUsername + foodValue + 2, 9, zLevel, 0, 249, 4, 4);
                for (int i = 0; i < foodValue - 2; i++) SAOGL.glTexturedRect(offsetUsername + i  + 4, 9, zLevel, 0, 249, 4, 4);
            }
        }


        final String healthStr = String.valueOf((int) SAOMod.getHealth(mc, mc.thePlayer, time)) + " / " + String.valueOf((int) SAOMod.getMaxHealth(mc.thePlayer));
        final int healthStrWidth = fontRenderer.getStringWidth(healthStr);

        final int healthBoxes = (healthStrWidth + 4) / 5;

        SAOGL.glColor(1, 1, 1, 1);
        SAOGL.glTexturedRect(offsetUsername + 113, 13, zLevel, 60, 15, 5, 13);
        SAOGL.glTexturedRect(offsetUsername + 118, 13, zLevel, healthBoxes * 5, 13, 65, 15, 5, 13);
        SAOGL.glTexturedRect(offsetUsername + 118 + healthBoxes * 5, 13, zLevel, 70, 15, 5, 13);

        SAOGL.glString(healthStr, offsetUsername + 118, 16, 0xFFFFFFFF);

        mc.mcProfiler.endSection();

        mc.mcProfiler.startSection("effects");

        final int offsetForEffects = offsetUsername + healthBarWidth - 4;
        final List<SAOEffect> effects = SAOEffect.getEffects(mc.thePlayer);

        SAOGL.glBindTexture(SAOOption.ORIGINAL_UI.value? SAOResources.gui: SAOResources.guiCustom);

        for (int i = 0; i < effects.size(); i++) {
            effects.get(i).glDraw(offsetForEffects + i * 11, 2, zLevel);
        }

        mc.mcProfiler.endSection();

        int hpBarOffset = 26;

        if (SAOMod.isPartyMember(username)) {
            mc.mcProfiler.startSection("party");

            final List<EntityPlayer> players = SAOMod.listOnlinePlayers(mc);

            if (players.contains(mc.thePlayer)) {
                players.remove(mc.thePlayer);
            }

            int index = 0;
            for (final EntityPlayer player : players) {
                final String playerName = player.getName();

                if (!SAOMod.isPartyMember(playerName)) {
                    continue;
                }

                SAOGL.glBindTexture(SAOOption.ORIGINAL_UI.value? SAOResources.gui: SAOResources.guiCustom);

                SAOGL.glTexturedRect(2, 19 + index * 15, zLevel, 85, 15, 10, 13);
                SAOGL.glTexturedRect(13, 19 + index * 15, zLevel, 80, 15, 5, 13);

                final int nameWidth = fontRenderer.getStringWidth(playerName);
                final int nameBoxes = (nameWidth + 4) / 5 + 1;

                if (nameWidth > maxNameWidth) {
                    maxNameWidth = nameWidth;
                }

                SAOGL.glTexturedRect(18, 19 + index * 15, zLevel, nameBoxes * 5, 13, 65, 15, 5, 13);

                int offset = 18 + nameBoxes * 5;

                SAOGL.glTexturedRect(offset, 19 + index * 15, zLevel, 40, 28, 100, 13);

                final int hpWidth = 97;
                final int hpHeight = 3;

                final int hpValue = (int) (SAOMod.getHealth(mc, player, time) / SAOMod.getMaxHealth(player) * hpWidth);
                SAOHealthStep.getStep(mc, player, time).glColor();

                int hp = hpHeight;
                for (int j = 0; j < hpValue; j++) {
                    SAOGL.glTexturedRect(offset + 1 + j, 24 + index * 15, zLevel, (hpHeight - hp), 15, 1, hp);

                    if (j >= hpValue - hp) {
                        hp--;

                        if (hp <= 0) {
                            break;
                        }
                    }
                }

                offset += 100;

                SAOGL.glColor(1, 1, 1, 1);
                SAOGL.glTexturedRect(offset, 19 + index * 15, zLevel, 70, 15, 5, 13);
                SAOGL.glString(playerName, 18, 20 + index * 15 + (13 - fontRenderer.FONT_HEIGHT) / 2, 0xFFFFFFFF);

                index++;
            }

            mc.mcProfiler.endSection();

            hpBarOffset += (index * 15);
        }

        mc.mcProfiler.startSection("expLevel");

        SAOGL.glBindTexture(SAOOption.ORIGINAL_UI.value? SAOResources.gui: SAOResources.guiCustom);

        final int offsetHealth = offsetUsername + 113 + (healthBoxes + 2) * 5;

        final String levelStr = StatCollector.translateToLocal("displayLvShort") + ": " + String.valueOf(mc.thePlayer.experienceLevel);
        final int levelStrWidth = fontRenderer.getStringWidth(levelStr);

        final int levelBoxes = (levelStrWidth + 4) / 5;

        SAOGL.glTexturedRect(offsetHealth, 13, zLevel, 60, 15, 5, 13);
        SAOGL.glTexturedRect(offsetHealth + 5, 13, zLevel, levelBoxes * 5, 13, 65, 15, 5, 13);
        SAOGL.glTexturedRect(offsetHealth + (1 + levelBoxes) * 5, 13, zLevel, 75, 15, 5, 13);

        SAOGL.glString(levelStr, offsetHealth + 5, 16, 0xFFFFFFFF);
        SAOGL.glBindTexture(SAOOption.ORIGINAL_UI.value? SAOResources.gui: SAOResources.guiCustom);

        mc.mcProfiler.endSection();
    }

    private void drawTooltips(FontRenderer fontRenderer, int width, int height) {
        mc.mcProfiler.startSection("toolHighlight");

        if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null) {
            String name = this.highlightingItemStack.getDisplayName();

            if (this.highlightingItemStack.hasDisplayName()) {
                name = EnumChatFormatting.ITALIC + name;
            }

            final int x = (width - 4) - fontRenderer.getStringWidth(name);
            final int y = (height - (6 + fontRenderer.FONT_HEIGHT)) - fontRenderer.FONT_HEIGHT;

            int alpha = (int) ((float) this.remainingHighlightTicks * 256.0F / 10.0F);

            if (alpha > 0xFF) {
                alpha = 0xFF;
            }

            if (alpha > 0x00) {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

                SAOGL.glString(name, x, y, 0xFFFFFF00 | (alpha), true);

                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }

        mc.mcProfiler.endSection();
    }

    @Override
    public GuiNewChat getChatGUI() {
        return chatLine;
    }

    public void onMessage(String username, String message) {
        if (messages.isEmpty()) {
            SAOSound.play(Minecraft.getMinecraft(), SAOSound.MESSAGE);
        }

        messages.add(new String[]{username, message});
    }

    public boolean backgroundClicked(int cursorX, int cursorY, int button) {
        return !SAOOption.DEFAULT_UI.value && (receivedMessage.mouseOver(cursorX, cursorY, button)) && (receivedMessage.mouseReleased(mc, cursorX, cursorY, button)) && openMessage();

    }

    public boolean viewMessageAuto() {
        return messages.size() != 0 && openMessage();

    }

    private boolean openMessage() {
        SAOSound.play(Minecraft.getMinecraft(), SAOSound.MENU_POPUP);

        final String[] message = messages.poll();
        mc.displayGuiScreen(SAOWindowViewGUI.viewMessage(message[0], message[1]));
        return true;
    }

}

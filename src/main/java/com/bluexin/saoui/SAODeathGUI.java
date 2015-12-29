package com.bluexin.saoui;

import com.bluexin.saoui.ui.SAOAlertGUI;
import com.bluexin.saoui.ui.SAOElementGUI;
import com.bluexin.saoui.ui.SAOScreenGUI;
import com.bluexin.saoui.util.SAOAction;
import com.bluexin.saoui.util.SAOColor;
import com.bluexin.saoui.util.SAOCursorStatus;
import com.bluexin.saoui.util.SAOID;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAODeathGUI extends SAOScreenGUI {

    private final GuiGameOver gameOver;
    private final SAOCursorStatus oldCursorStatus;

    public SAODeathGUI(GuiGameOver guiGamOver) {
        super();
        gameOver = guiGamOver;
        oldCursorStatus = CURSOR_STATUS;

        CURSOR_STATUS = SAOCursorStatus.HIDDEN;
    }

    @Override
    protected void init() {
        super.init();

        if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
            elements.add(new SAOAlertGUI(this, 0, 0, SAOMod._DEAD_ALERT, SAOColor.HARDCORE_DEAD_COLOR));
        } else {
            elements.add(new SAOAlertGUI(this, 0, 0, SAOMod._DEAD_ALERT, SAOColor.DEAD_COLOR));
        }
    }

    @Override
    public int getX(boolean relative) {
        return super.getX(relative) + width / 2;
    }

    @Override
    public int getY(boolean relative) {
        return super.getY(relative) + height / 2;
    }

    @Override
    public void drawScreen(int cursorX, int cursorY, float f) {
        drawDefaultBackground();

        GlStateManager.pushMatrix();
        GlStateManager.translate(-width / 2, -height / 2, 0.0F);
        GlStateManager.scale(2.0F, 2.0F, 2.0F);

        super.drawScreen(cursorX, cursorY, f);

        GlStateManager.popMatrix();
    }

    @Override
    public void actionPerformed(SAOElementGUI element, SAOAction action, int data) {
        final SAOID id = element.ID();

        element.click(mc.getSoundHandler(), false);

        if (id == SAOID.ALERT) {
            if (!this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
                gameOver.confirmClicked(false, 0);
            } else {
                gameOver.confirmClicked(true, 1);
            }
        }
    }

    protected void backgroundClicked(int cursorX, int cursorY, int button) {
        if (!this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
            if (!((SAOIngameGUI) this.mc.ingameGUI).backgroundClicked(cursorX, cursorY, button)) {
                this.mc.thePlayer.respawnPlayer();
                this.mc.displayGuiScreen((GuiScreen) null);
                mc.setIngameFocus();
            }
        } else if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
            if (!((SAOIngameGUI) this.mc.ingameGUI).backgroundClicked(cursorX, cursorY, button)) {
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld((WorldClient) null);
                this.mc.displayGuiScreen(new GuiMainMenu());
            }
        } else {
            this.mc.theWorld.sendQuittingDisconnectingPacket();
            this.mc.loadWorld((WorldClient) null);
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    @Override
    public void close() {
        super.close();

        CURSOR_STATUS = oldCursorStatus;
    }

}

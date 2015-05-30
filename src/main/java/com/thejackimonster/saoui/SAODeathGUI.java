package com.thejackimonster.saoui;

import java.io.IOException;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.thejackimonster.saoui.ui.SAOAlertGUI;
import com.thejackimonster.saoui.ui.SAOElementGUI;
import com.thejackimonster.saoui.ui.SAOScreenGUI;
import com.thejackimonster.saoui.ui.SAOWindowGUI;
import com.thejackimonster.saoui.util.SAOAction;
import com.thejackimonster.saoui.util.SAOColor;
import com.thejackimonster.saoui.util.SAOCursorStatus;
import com.thejackimonster.saoui.util.SAOID;

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

	protected void init() {
		super.init();
		
		elements.add(new SAOAlertGUI(this, 0, 0, SAOMod._DEAD_ALERT, SAOColor.DEAD_COLOR));
	}

	public int getX(boolean relative) {
		return super.getX(relative) + width / 2;
	}

	public int getY(boolean relative) {
		return super.getY(relative) + height / 2;
	}

	public void drawScreen(int cursorX, int cursorY, float f) {
		drawDefaultBackground();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate(-width / 2, -height / 2, 0.0F);
		GlStateManager.scale(2.0F, 2.0F, 2.0F);
		
		super.drawScreen(cursorX, cursorY, f);
		
		GlStateManager.popMatrix();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {}

	public void actionPerformed(SAOElementGUI element, SAOAction action, int data) {
		final SAOID id = element.ID();
		
		element.click(mc.getSoundHandler(), false);
		
		if (id == SAOID.ALERT) {
			gameOver.confirmClicked(true, 1);
		}
	}

	protected void backgroundClicked(int cursorX, int cursorY, int button) {
		if (!mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
			if (button == 0) {
				if (!((SAOIngameGUI) mc.ingameGUI).backgroundClicked(cursorX, cursorY, button)) {
					gameOver.confirmClicked(false, 1);
					mc.setIngameFocus();
				}
			}
		}
	}

	public void close() {
		super.close();
		
		CURSOR_STATUS = oldCursorStatus;
	}

}

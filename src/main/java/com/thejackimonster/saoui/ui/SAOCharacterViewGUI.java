package com.thejackimonster.saoui.ui;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.thejackimonster.saoui.util.SAOColor;
import com.thejackimonster.saoui.util.SAOGL;
import com.thejackimonster.saoui.util.SAOOption;
import com.thejackimonster.saoui.util.SAOParentGUI;
import com.thejackimonster.saoui.util.SAOResources;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOCharacterViewGUI extends SAOElementGUI {

	private final EntityPlayer character;

	private int clickIndex;

	public SAOCharacterViewGUI(SAOParentGUI gui, int xPos, int yPos, int w, int h, EntityPlayer player) {
		super(gui, xPos, yPos, w, h);
		character = player;
	}

	public void draw(Minecraft mc, int cursorX, int cursorY) {
		super.draw(mc, cursorX, cursorY);
		
		clickIndex = -1;
		
		if (visibility > 0) {
			SAOGL.glBindTexture(SAOResources.gui);
			SAOGL.glColorRGBA(SAOColor.multiplyAlpha(SAOColor.DEFAULT_COLOR, visibility));
			
			int left = getX(false) + width / 2;
			int top = getY(false) + height * 13 / 16;
			
			final int size = width * height / 550;
			
			final int shadowX = size;
			final int shadowY = size / 2 + Math.max(Math.min((cursorY - top) / 12, 0), -size / 2 + 2);
			
			final int shadowOffset = Math.max((cursorY - top) / 10, 0);
			
			SAOGL.glTexturedRect(left - shadowX / 2, (top - shadowY / 2), shadowX, shadowY, 200, 85, 56, 30);
			
			drawCharacter(character, left, top, size, cursorX, cursorY);
			
			left = getX(false) + width / 2;
			top = getY(false) + height / 2;
			
			final int width2 = (width / 2) - 14;
			final int height2 = (height / 2) - 14;
			
			for (int angle = 0; angle < 12; angle++) {
				final int x = (int) (left + Math.sin(Math.toRadians(angle * 30)) * width2);
				final int y = (int) (top + Math.cos(Math.toRadians(angle * 30)) * height2);
				
				final boolean hovered = ((cursorX >= x - 10) && (cursorY >= y - 10) && (cursorX <= x + 10) && (cursorY <= y + 10));
				
				SAOGL.glBindTexture(SAOResources.gui);
				
				SAOGL.glColorRGBA(SAOColor.multiplyAlpha(hovered? SAOColor.HOVER_COLOR : SAOColor.DEFAULT_FONT_COLOR, visibility));
				SAOGL.glTexturedRect(x - 10, y - 10, 0, 25, 20, 20);
				
				if ((angle + 4 < 9) || (angle + 4 >= 12)) {
					final int index = (angle + 4 >= 12? (angle - 8) % 9 : (angle + 4) % 9);
					final Slot slot = character.inventoryContainer.getSlotFromInventory(character.inventory, index);
					
					if ((slot.getHasStack()) && (slot.getStack().getItem() != null)) {
						SAOGL.glColorRGBA(SAOColor.multiplyAlpha(hovered? SAOColor.HOVER_FONT_COLOR : SAOColor.DEFAULT_COLOR, visibility));
						SAOSlotGUI.getIcon(slot.getStack()).glDraw(x - 8, y - 8);
					}
					
					if (hovered) {
						clickIndex = index;
					}
				}
			}
		}
	}

	public boolean keyTyped(Minecraft mc, char ch, int key) {
		if (character == mc.thePlayer) {
			for (int i = 0; i < 9; i++) {
				if (key == mc.gameSettings.keyBindsHotbar[i].getKeyCode()) {
					character.inventory.currentItem = i;
					return true;
				}
			}
		}
		
		return super.keyTyped(mc, ch, key);
	}

	public boolean mousePressed(Minecraft mc, int cursorX, int cursorY, int button) {
		if ((clickIndex >= 0) && (button == 0) && (character == mc.thePlayer)) {
			character.inventory.currentItem = clickIndex;
			return true;
		}
		
		return super.mousePressed(mc, cursorX, cursorY, button);
	}

	public static final void drawCharacter(EntityPlayer character, int x, int y, int size, int cursorX, int cursorY) {
		final float mouseX = (float) x - cursorX;
		final float mouseY = (float) y - size * 1.67F - cursorY;
		
		final boolean value = SAOOption.COLOR_CURSOR.value;
		
		SAOOption.COLOR_CURSOR.value = false;
		GuiInventory.drawEntityOnScreen(x, y, size, mouseX, mouseY, character);
		SAOOption.COLOR_CURSOR.value = value;
		
		SAOGL.glRescaleNormal(true);
		SAOGL.glTexture2D(true);
		SAOGL.glBlend(true);
		
		SAOGL.tryBlendFuncSeparate(770, 771, 1, 0);
	}

}

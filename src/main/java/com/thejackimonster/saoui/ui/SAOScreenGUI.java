package com.thejackimonster.saoui.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

import com.thejackimonster.saoui.SAOIngameGUI;
import com.thejackimonster.saoui.util.SAOAction;
import com.thejackimonster.saoui.util.SAOColor;
import com.thejackimonster.saoui.util.SAOCursorStatus;
import com.thejackimonster.saoui.util.SAOGL;
import com.thejackimonster.saoui.util.SAOOption;
import com.thejackimonster.saoui.util.SAOParentGUI;
import com.thejackimonster.saoui.util.SAOResources;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;

@SideOnly(Side.CLIENT)
public abstract class SAOScreenGUI extends GuiScreen implements SAOParentGUI {

	private static final float ROTATION_FACTOR = 0.25F;
	protected static SAOCursorStatus CURSOR_STATUS = SAOCursorStatus.SHOW;

	private int mouseX, mouseY;

	private int mouseDown;
	private float mouseDownValue;

	protected final List<SAOElementGUI> elements;

	private float[] rotationYaw, rotationPitch;
	private boolean grabbed;

	protected SAOScreenGUI() {
		super();
		elements = new ArrayList<>();
		grabbed = false;
	}

	public void initGui() {
		if (CURSOR_STATUS != SAOCursorStatus.DEFAULT) {
			Mouse.setGrabbed(true);
			grabbed = true;
		}
		
		super.initGui();
		elements.clear();
		init();
	}

	protected void init() {
		if (mc.thePlayer != null) {
			rotationYaw = new float[] { mc.thePlayer.rotationYaw };
			rotationPitch = new float[] { mc.thePlayer.rotationPitch };
		}
	}

	private int getCursorX() {
		return SAOOption.CURSOR_MOVEMENT.value? (width / 2 - mouseX) / 2 : 0;
	}

	private int getCursorY() {
		return SAOOption.CURSOR_MOVEMENT.value? (height / 2 - mouseY) / 2 : 0;
	}

	public int getX(boolean relative) {
		return getCursorX();
	}

	public int getY(boolean relative) {
		return getCursorY();
	}

	public void updateScreen() {
		for (int i = elements.size() - 1; i >= 0; i--) {
			if (i >= elements.size()) {
				continue;
			}
			
			if (elements.get(i).removed()) {
				elements.get(i).close(mc);
				elements.remove(i);
				continue;
			}
			
			elements.get(i).update(mc);
		}
	}

	public void drawScreen(int cursorX, int cursorY, float f) {
		mouseX = cursorX;
		mouseY = cursorY;
		
		if (mc.thePlayer != null) {
			mc.thePlayer.rotationYaw = rotationYaw[0] - getCursorX() * ROTATION_FACTOR;
			mc.thePlayer.rotationPitch = rotationPitch[0] - getCursorY() * ROTATION_FACTOR;
		}
		
		super.drawScreen(cursorX, cursorY, f);
		
		SAOGL.glStartUI(mc);
		
		SAOGL.glBlend(true);
		SAOGL.tryBlendFuncSeparate(770, 771, 1, 0);
		
		for (int i = elements.size() - 1; i >= 0; i--) {
			if (i >= elements.size()) {
				continue;
			}
			
			elements.get(i).draw(mc, cursorX, cursorY);
		}
		
		if (CURSOR_STATUS == SAOCursorStatus.SHOW) {
			SAOGL.glBindTexture(SAOResources.gui);
			
			SAOGL.glBlend(true);
			SAOGL.tryBlendFuncSeparate(770, 771, 1, 0);
			
			if (mouseDown != 0) {
				final float fval = f * 0.1F;
				
				if (mouseDownValue + fval < 1.0F) {
					mouseDownValue += fval;
				} else {
					mouseDownValue = 1.0F;
				}
				
				SAOGL.glColorRGBA(SAOColor.multiplyAlpha(SAOColor.CURSOR_COLOR, mouseDownValue));
				SAOGL.glTexturedRect(cursorX - 7, cursorY - 7, 35, 115, 15, 15);
				
				SAOGL.glColorRGBA(SAOColor.DEFAULT_COLOR);
			} else {
				mouseDownValue = 0;
				
				SAOGL.glColorRGBA(SAOColor.CURSOR_COLOR);
			}
			
			SAOGL.glTexturedRect(cursorX - 7, cursorY - 7, 20, 115, 15, 15);
		}
		
		SAOGL.glEndUI(mc);
	}

	protected void keyTyped(char ch, int key) throws IOException {
		super.keyTyped(ch, key);
		
		for (int i = elements.size() - 1; i >= 0; i--) {
			if (i >= elements.size()) {
				continue;
			}
			
			if (elements.get(i).focus) {
				if (elements.get(i).keyTyped(mc, ch, key)) {
					actionPerformed(elements.get(i), SAOAction.KEY_TYPED, key);
				}
			}
		}
	}

	protected void mouseClicked(int cursorX, int cursorY, int button) throws IOException {
		super.mouseClicked(cursorX, cursorY, button);
		mouseDown |= (0x1 << button);
		
		boolean clickedElement = false;
		
		for (int i = elements.size() - 1; i >= 0; i--) {
			if (i >= elements.size()) {
				continue;
			}
			
			if (elements.get(i).mouseOver(cursorX, cursorY)) {
				if (elements.get(i).mousePressed(mc, cursorX, cursorY, button)) {
					actionPerformed(elements.get(i), SAOAction.getAction(button, true), button);
				}
				
				clickedElement = true;
			}
		}
		
		if (!clickedElement) {
			backgroundClicked(cursorX, cursorY, button);
		}
	}

	protected void mouseReleased(int cursorX, int cursorY, int button) {
		super.mouseReleased(cursorX, cursorY, button);
		mouseDown &= (~(0x1 << button));
		
		for (int i = elements.size() - 1; i >= 0; i--) {
			if (i >= elements.size()) {
				continue;
			}
			
			if (elements.get(i).mouseOver(cursorX, cursorY, button)) {
				if (elements.get(i).mouseReleased(mc, cursorX, cursorY, button)) {
					actionPerformed(elements.get(i), SAOAction.getAction(button, false), button);
				}
			}
		}
	}

	protected void backgroundClicked(int cursorX, int cursorY, int button) {
		if (button == 0) {
			if (!((SAOIngameGUI) mc.ingameGUI).backgroundClicked(cursorX, cursorY, button)) {
				mc.displayGuiScreen(null);
				mc.setIngameFocus();
			}
		}
	}

	private void mouseWheel(int cursorX, int cursorY, int delta) {
		for (int i = elements.size() - 1; i >= 0; i--) {
			if (i >= elements.size()) {
				continue;
			}
			
			if (elements.get(i).mouseOver(cursorX, cursorY)) {
				if (elements.get(i).mouseWheel(mc, cursorX, cursorY, delta)) {
					actionPerformed(elements.get(i), SAOAction.MOUSE_WHEEL, delta);
				}
			}
		}
	}

	public void actionPerformed(SAOElementGUI element, SAOAction action, int data) {
		element.click(mc.getSoundHandler(), false);
	}

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		
		if (Mouse.hasWheel()) {
			final int x = Mouse.getEventX() * width / mc.displayWidth;
			final int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;
			final int delta = Mouse.getEventDWheel();
			
			if (delta != 0) {
				mouseWheel(x, y, delta);
			}
		}
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	public void onGuiClosed() {
		if (grabbed) {
			Mouse.setGrabbed(false);
		}
		
		close();
	}

	protected void close() {
		for (int i = elements.size() - 1; i >= 0; i--) {
			if (i >= elements.size()) {
				continue;
			}
			
			elements.get(i).close(mc);
			elements.remove(i);
		}
	}

}

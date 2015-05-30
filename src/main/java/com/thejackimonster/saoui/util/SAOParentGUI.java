package com.thejackimonster.saoui.util;

import com.thejackimonster.saoui.ui.SAOElementGUI;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface SAOParentGUI extends SAOActionHandler {

	public int getX(boolean relative);
	public int getY(boolean relative);

}

package com.thejackimonster.saoui.util;

import com.thejackimonster.saoui.ui.SAOStateButtonGUI;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public interface SAOStateHandler {

	boolean isStateEnabled(Minecraft mc, SAOStateButtonGUI button);

}

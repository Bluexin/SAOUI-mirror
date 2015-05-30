package com.thejackimonster.saoui.util;

import com.thejackimonster.saoui.ui.SAOStateButtonGUI;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface SAOStateHandler {

    boolean isStateEnabled(Minecraft mc, SAOStateButtonGUI button);

}

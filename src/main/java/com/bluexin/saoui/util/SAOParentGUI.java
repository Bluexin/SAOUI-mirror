package com.bluexin.saoui.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface SAOParentGUI extends SAOActionHandler {

    int getX(boolean relative);

    int getY(boolean relative);

}

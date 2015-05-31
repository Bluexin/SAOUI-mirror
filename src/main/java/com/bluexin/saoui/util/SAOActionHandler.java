package com.bluexin.saoui.util;

import com.bluexin.saoui.ui.SAOElementGUI;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface SAOActionHandler {

    void actionPerformed(SAOElementGUI element, SAOAction action, int data);

}

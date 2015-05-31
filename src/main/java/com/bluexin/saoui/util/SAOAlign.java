package com.bluexin.saoui.util;

import com.bluexin.saoui.ui.SAOElementGUI;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum SAOAlign {

    CENTER((element, relative, width) -> element.getX(relative) + (element.width - width) / 2),

    LEFT((element, relative, width) -> element.getX(relative)),

    RIGHT((element, relative, width) -> element.getX(relative) + (element.width - width));

    private final SAOPositioner positioner;

    SAOAlign(SAOPositioner pos) {
        positioner = pos;
    }

    public int getX(SAOElementGUI element, boolean relative, int size) {
        return positioner.getX(element, relative, size);
    }

    private interface SAOPositioner {

        int getX(SAOElementGUI element, boolean relative, int width);

    }

}

package com.bluexin.saoui.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class SAOColorCursor {

    private static final long STATE_TIME = 24 * 60 * 60 * 20; // DAY IN TICKS

    private SAOColorState colorState;
    private long downgradeTime;

    public SAOColorCursor(SAOColorState defaultState, boolean set) {
        colorState = defaultState;
        if (set) set(defaultState);
        else downgradeTime = 0;
    }

    public SAOColorCursor() {
        this(SAOColorState.INNOCENT, false);
    }

    public final void update() {
        if (downgradeTime == 0) colorState = SAOColorState.INNOCENT;
        else downgradeTime--;
    }

    public final void set(SAOColorState state) {
        if (state.ordinal() >= colorState.ordinal()) {
            colorState = state;
            downgradeTime = STATE_TIME;
        }
    }

    public final SAOColorState get() {
        return colorState;
    }

}

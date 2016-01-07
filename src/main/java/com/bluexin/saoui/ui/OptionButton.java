package com.bluexin.saoui.ui;

import com.bluexin.saoui.util.SAOID;
import com.bluexin.saoui.util.SAOIcon;
import com.bluexin.saoui.util.SAOOption;
import com.bluexin.saoui.util.SAOParentGUI;

/**
 * Part of SAOUI
 *
 * @author Bluexin
 */
public class OptionButton extends SAOButtonGUI {
    private final SAOOption option;

    public OptionButton(SAOParentGUI gui, int xPos, int yPos, SAOOption option) {
        super(gui, option.isCategory ? SAOID.OPT_CAT : SAOID.OPTION, xPos, yPos, option.toString(), SAOIcon.OPTION);
        this.highlight = option.getValue();
        this.option = option;
    }

    public SAOOption getOption() {
        return this.option;
    }

    public void action() {
        this.highlight = this.option.flip();
    }
}

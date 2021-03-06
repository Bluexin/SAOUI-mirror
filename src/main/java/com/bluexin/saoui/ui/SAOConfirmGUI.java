package com.bluexin.saoui.ui;

import com.bluexin.saoui.util.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOConfirmGUI extends SAOWindowGUI {

    private final SAOTextGUI textText;
    private final SAOContainerGUI buttonBox;
    private final SAOActionHandler actionHandler;

    public SAOConfirmGUI(SAOParentGUI gui, int xPos, int yPos, int w, int h, String title, String text, SAOActionHandler handler) {
        super(gui, xPos, yPos, w, h, title);
        elements.add(textText = new SAOTextGUI(this, 0, 0, text, width));
        elements.add(buttonBox = new SAOContainerGUI(this, 0, 0, width, 40));
        buttonBox.elements.add(new SAOIconGUI(buttonBox, SAOID.CONFIRM, width / 4 - 10, 10, SAOIcon.CONFIRM));
        buttonBox.elements.add(new SAOIconGUI(buttonBox, SAOID.CANCEL, width * 3 / 4 - 10, 10, SAOIcon.CANCEL));
        actionHandler = handler;
    }

    public final String getText() {
        return textText.getText();
    }

    public final void setText(String text) {
        textText.setText(text);
    }

    @Override
	protected int getSize() {
        return Math.max(super.getSize() - 20, 60);
    }

    @Override
	public void actionPerformed(SAOElementGUI element, SAOAction action, int data) {
        if (actionHandler != null) actionHandler.actionPerformed(element, action, data);
        else super.actionPerformed(element, action, data);
    }

    public final void confirm() {
        actionPerformed(buttonBox.elements.get(0), SAOAction.LEFT_RELEASED, 0);
    }

    public final void cancel() {
        actionPerformed(buttonBox.elements.get(1), SAOAction.LEFT_RELEASED, 0);
    }

    @Override
	protected int getBoxSize(boolean bottom) {
        return bottom ? 40 : super.getBoxSize(bottom);
    }

}

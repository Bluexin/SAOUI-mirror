package com.bluexin.saoui.ui;

import com.bluexin.saoui.util.SAOInventory;
import com.bluexin.saoui.util.SAOParentGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SAOInventoryGUI extends SAOListGUI {

    public final Container slots;
    public final SAOInventory filter;

    private boolean opened;

    public SAOInventoryGUI(SAOParentGUI gui, int xPos, int yPos, int w, int h, Container containerSlots, SAOInventory inventory) {
        super(gui, xPos, yPos, w, h);
        slots = containerSlots;
        filter = inventory;
        opened = false;
    }

    @Override
    public void update(Minecraft mc) {
        if (!opened) {
            mc.thePlayer.openContainer = slots;
            opened = true;
        }

        super.update(mc);

        for (int i = 0; i < slots.inventorySlots.size(); i++) {
            final Slot slot = slots.getSlot(i);

            if (slot != null) {
                boolean state = equipped(slot.slotNumber);

                final ItemStack stack = slot.getStack();
                boolean found = false;

                for (int j = elements.size() - 1; j >= 0; j--) {
                    if (j >= elements.size()) continue;

                    if (elements.get(j) instanceof SAOSlotGUI) {
                        final SAOSlotGUI gui = (SAOSlotGUI) elements.get(j);

                        if (gui.getSlotNumber() == slot.slotNumber) {
                            gui.refreshSlot(slot);

                            if (!gui.removed()) {
                                if (filter.isFine(gui.getStack(), state)) found = true;
                                else gui.remove();
                            }
                        }
                    }
                }

                if (!found && stack != null && filter.isFine(stack, state)) {
                    if (state) elements.add(0, new SAOSlotGUI(this, 0, getOffset(elements.size()), slot));
                    else elements.add(new SAOSlotGUI(this, 0, getOffset(elements.size()), slot));
                }
            }
        }

        if (elements.isEmpty()) elements.add(new SAOEmptySlot(this, 0, getOffset(elements.size())));
        else {
            final SAOSlotGUI slot = (SAOSlotGUI) elements.get(elements.size() - 1);
            if (slot.getSlotNumber() == -1) slot.remove();
        }

        slots.detectAndSendChanges();
    }

    private boolean equipped(int number) {
        final boolean state;

        if (filter.equals(SAOInventory.EQUIPMENT)) state = (number >= 5) && (number < 9);
        else state = (number >= 36) && (number < 45);

        return state;
    }

    @Override
    protected void update(Minecraft mc, int index, SAOElementGUI element) {
        super.update(mc, index, element);

        if (element instanceof SAOSlotGUI) {
            final SAOSlotGUI slot = (SAOSlotGUI) element;

            slot.highlight = equipped(slot.getSlotNumber());
        }
    }

    public void handleMouseClick(Minecraft mc, Slot slot, int slotNumber, int flag, int method) {
        if (slot != null) slotNumber = slot.slotNumber;

        mc.playerController.windowClick(slots.windowId, slotNumber, flag, method, mc.thePlayer);
    }

    @Override
    public void close(Minecraft mc) {
        super.close(mc);

        if (mc.thePlayer != null) slots.onContainerClosed(mc.thePlayer);
    }

}

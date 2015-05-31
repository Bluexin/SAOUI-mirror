package com.bluexin.saoui.util;

import net.minecraft.block.BlockPumpkin;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum SAOInventory {

    EQUIPMENT((stack, state) -> {
        final Item item = stack.getItem();

        return (item instanceof ItemArmor) || ((item instanceof ItemBlock) && (((ItemBlock) item).block instanceof BlockPumpkin));
    }),

    WEAPONS((stack, state) -> {
        final Item item = stack.getItem();

        return (item instanceof ItemSword) || (item instanceof ItemTool) || (item instanceof ItemBow);
    }),

    ACCESSORY((stack, state) -> {
        final Item item = stack.getItem();

        return (
                (item instanceof ItemExpBottle) ||
                        (item instanceof ItemBucket) ||
                        (item instanceof ItemPotion) ||
                        (item instanceof ItemFishingRod) ||
                        (item instanceof ItemCarrotOnAStick) ||
                        (item instanceof ItemEnchantedBook) ||
                        (item instanceof ItemEditableBook) ||
                        (item instanceof ItemMapBase) ||
                        (item instanceof ItemNameTag) ||
                        (item instanceof ItemSaddle) ||
                        (item instanceof ItemWritableBook) ||
                        (item instanceof ItemLead) ||
                        (item instanceof ItemFlintAndSteel) ||
                        (item instanceof ItemShears)
        );
    }),

    ITEMS(new ItemFilter() {

        @Override
        public boolean filter(ItemStack stack, boolean state) {
            return !state || (!EQUIPMENT.isFine(stack, state));
        }

    });

    private final ItemFilter itemFilter;

    SAOInventory(ItemFilter filter) {
        itemFilter = filter;
    }

    public final boolean isFine(ItemStack stack, boolean state) {
        return itemFilter.filter(stack, state);
    }

    private interface ItemFilter {
        boolean filter(ItemStack stack, boolean state);
    }

}

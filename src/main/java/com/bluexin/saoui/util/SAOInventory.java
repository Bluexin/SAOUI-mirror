package com.bluexin.saoui.util;

import baubles.api.IBauble;
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

    WEAPONS((ItemFilter) (stack, state) -> {
        final Item item = stack.getItem();

        return item instanceof ItemSword;
    }),

    BOWS((ItemFilter) (stack, state) -> {
        final Item item = stack.getItem();

        return item instanceof ItemBow;
    }),

    PICKAXE((ItemFilter) (stack, state) -> {
        final Item item = stack.getItem();

        return item instanceof ItemPickaxe;
    }),

    AXE((ItemFilter) (stack, state) -> {
        final Item item = stack.getItem();

        return item instanceof ItemAxe;
    }),

    SHOVEL((ItemFilter) (stack, state) -> {
        final Item item = stack.getItem();

        return item instanceof ItemSpade;
    }),

    COMPATTOOLS((ItemFilter) (stack, state) -> {
        final Item item = stack.getItem();

        return ((item instanceof ItemTool) || (item instanceof ItemBow) || (item instanceof ItemSword));
    }),

    ACCESSORY((ItemFilter) (stack, state) -> {
        final Item item = stack.getItem();

        return (
                (item instanceof IBauble)
        );
    }),

    CONSUMABLES((ItemFilter) (stack, state) -> {
        final Item item = stack.getItem();

        return (
                (item instanceof ItemExpBottle) ||
                        (item instanceof ItemPotion) ||
                        (item instanceof ItemFood)
        );
    }),

    ITEMS((stack, state) -> !state || (!EQUIPMENT.isFine(stack, state)));

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

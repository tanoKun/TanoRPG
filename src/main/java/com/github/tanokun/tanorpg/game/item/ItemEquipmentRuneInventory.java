package com.github.tanokun.tanorpg.game.item;

import com.github.tanokun.tanorpg.game.item.type.ItemRune;

public class ItemEquipmentRuneInventory {
    private ItemRune slot1;

    private boolean slot1_open = false;

    public ItemRune getSlot1() {
        return slot1;
    }

    public void setSlot1(ItemRune slot1) {
        this.slot1 = slot1;
    }

    public void setSlot1_open(boolean slot1_open) {
        this.slot1_open = slot1_open;
    }

    public boolean isSlot1_open() {
        return slot1_open;
    }

}

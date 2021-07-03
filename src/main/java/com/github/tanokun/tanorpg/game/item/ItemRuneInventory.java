package com.github.tanokun.tanorpg.game.item;

import com.github.tanokun.tanorpg.game.item.type.ItemRune;

public class ItemRuneInventory {
    private ItemRune slot1;
    private ItemRune slot2;
    private ItemRune slot3;
    private ItemRune slot4;

    private boolean slot1_open = true;
    private boolean slot2_open = false;
    private boolean slot3_open = false;
    private boolean slot4_open = false;


    public ItemRune getSlot1() {
        return slot1;
    }

    public ItemRune getSlot2() {
        return slot2;
    }

    public ItemRune getSlot3() {
        return slot3;
    }

    public ItemRune getSlot4() {
        return slot4;
    }

    public void setSlot1(ItemRune slot1) {
        this.slot1 = slot1;
    }

    public void setSlot2(ItemRune slot2) {
        this.slot2 = slot2;
    }

    public void setSlot3(ItemRune slot3) {
        this.slot3 = slot3;
    }

    public void setSlot4(ItemRune slot4) {
        this.slot4 = slot4;
    }

    public void setSlot1_open(boolean slot1_open) {
        this.slot1_open = slot1_open;
    }

    public void setSlot2_open(boolean slot2_open) {
        this.slot2_open = slot2_open;
    }

    public void setSlot3_open(boolean slot3_open) {
        this.slot3_open = slot3_open;
    }

    public void setSlot4_open(boolean slot4_open) {
        this.slot4_open = slot4_open;
    }

    public boolean isSlot1_open() {
        return slot1_open;
    }

    public boolean isSlot2_open() {
        return slot2_open;
    }

    public boolean isSlot3_open() {
        return slot3_open;
    }

    public boolean isSlot4_open() {
        return slot4_open;
    }
}

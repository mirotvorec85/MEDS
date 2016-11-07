package org.meds.item;

import java.util.HashMap;
import java.util.Map;

import org.meds.util.Valued;

public enum ItemFlags implements Valued {


    /**
     * Calculate stats as Magic Item (more Protection and less Armour)
     */
    IsMagic(0x001),
    /**
     * As a loot from Unique creatures
     */
    IsRare(0x002),
    /**
     * Can not be given to another player.
     */
    IsPersonal(0x004);

    private static Map<Integer, ItemFlags> values = new HashMap<>();

    static {
        for (ItemFlags flag : ItemFlags.values())
            ItemFlags.values.put(flag.value, flag);
    }

    public static ItemFlags parse(int value) {
        return ItemFlags.values.get(value);
    }

    private final int value;

    ItemFlags(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }
}

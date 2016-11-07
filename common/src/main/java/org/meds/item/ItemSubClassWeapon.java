package org.meds.item;

import org.meds.util.Valued;

public enum ItemSubClassWeapon implements Valued {

    None(0),
    Sword(1),
    Axe(2),
    Knife(3),
    Staff(4),
    Spear(5),
    Maul(6);

    private static ItemSubClassWeapon[] values = new ItemSubClassWeapon[7];

    static {
        for (ItemSubClassWeapon subClass : ItemSubClassWeapon.values())
            ItemSubClassWeapon.values[subClass.value] = subClass;
    }

    public static ItemSubClassWeapon parse(int value) {
        return ItemSubClassWeapon.values[value];
    }

    private final int value;

    ItemSubClassWeapon(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
}

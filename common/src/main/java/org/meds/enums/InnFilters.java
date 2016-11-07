package org.meds.enums;

import org.meds.util.Valued;

public enum InnFilters implements Valued {

    /**
     * All filters are disabled.
     */
    Disabled(0),
    Weapon(1),
    /**
     * Body, Head and Shields
     */
    Armour(2),
    /**
     * Clothes only (Neck, Back, Gloves, Waist, Legs, Feet, Rings).
     */
    Clothe(3),
    /**
     * Potions and Elixirs
     */
    Usable(4),
    Components(5),
    Essences(6),
    /**
     * Recipes, Runes, Books, Tablets
     */
    Books(7),;

    private static InnFilters[] values = new InnFilters[8];

    static {
        for (InnFilters state : InnFilters.values())
            InnFilters.values[state.value] = state;
    }

    public static InnFilters parse(int value) {
        return InnFilters.values[value];
    }

    private final int value;

    InnFilters(int value) {
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

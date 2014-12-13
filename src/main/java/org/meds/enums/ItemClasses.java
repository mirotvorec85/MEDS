package org.meds.enums;

import org.meds.util.Valued;

public enum ItemClasses implements Valued
{
    Generic(0),
    Head(1),
    Neck(2),
    /**
     * or Cape
     */
    Back(3),
    Body(4),
    Hands(5),
    /**
     * Any weapon
     */
    Weapon(6),
    /**
     * Magic Wand or Shield
     */
    Shield(7),
    Waist(8),
    Legs(9),
    Foot(10),
    Ring(11),
    /**
     * Potions, Elixirs, Crosses, etc.
     */
    Usable(12),
    /**
     * Items for Dump
     */
    Component(13),
    Gemm(14),
    Recipe(15),
    Artefact(16);

    private static final ItemClasses[] values = new ItemClasses[17];

    static
    {
        for (ItemClasses itemClass : ItemClasses.values())
            ItemClasses.values[itemClass.value] = itemClass;
    }

    public static ItemClasses parse(int value)
    {
        return ItemClasses.values[value];
    }

    private final int value;

    private ItemClasses(int value)
    {
        this.value = value;
    }

    @Override
    public int getValue()
    {
        return this.value;
    }

    @Override
    public String toString()
    {
        return Integer.toString(this.value);
    }
}

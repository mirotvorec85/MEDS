package org.meds.enums;

import org.meds.util.Valued;

public enum ItemReinforcementTypes implements Valued
{
    None(0),
    Fire(1),
    Ice(2),
    Lightning(3);

    private static final ItemReinforcementTypes[] values = new ItemReinforcementTypes[4];

    static
    {
        for (ItemReinforcementTypes type : ItemReinforcementTypes.values())
            ItemReinforcementTypes.values[type.value] = type;
    }

    public static ItemReinforcementTypes parse(int value) {
        if (value < 0 || value >= values.length)
            return null;
        return ItemReinforcementTypes.values[value];
    }

    private final int value;

    private ItemReinforcementTypes(int value)
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

package meds.enums;

import meds.util.Valued;

public enum ItemEssenceLevels implements Valued
{
    None(0),

    Miraculous(6),
    Condescend(7),
    Fabulous(8),
    Attentive(9),
    Reign(10);

    private static final ItemEssenceLevels[] values = new ItemEssenceLevels[11];

    static
    {
        for (ItemEssenceLevels type : ItemEssenceLevels.values())
            ItemEssenceLevels.values[type.value] = type;
    }

    public static ItemEssenceLevels parse(int value)
    {
        if (value < 0 || value >= values.length)
            return null;
        return ItemEssenceLevels.values[value];
    }

    private final int value;

    private ItemEssenceLevels(int value)
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

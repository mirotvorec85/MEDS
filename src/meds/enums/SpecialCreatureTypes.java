package meds.enums;

import meds.util.Valued;

public enum SpecialCreatureTypes implements Valued
{
    Normal(0),
    Champion(1),
    DeathAura(2),
    Vampire(3),
    Fanatic(4),
    Possessed(5),
    Spectral(6),
    AbsorbAura(7),
    Freeze(8),
    Elementalist(9);

    private static SpecialCreatureTypes[] values = new SpecialCreatureTypes[10];

    static
    {
        for (SpecialCreatureTypes state : SpecialCreatureTypes.values())
            SpecialCreatureTypes.values[state.value] = state;
    }

    public static SpecialCreatureTypes parse(int value)
    {
        return SpecialCreatureTypes.values[value];
    }

    private final int value;

    private SpecialCreatureTypes(int value)
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

package org.meds.enums;

import java.util.HashMap;
import java.util.Map;

import org.meds.util.Valued;

public enum CreatureFlags implements Valued
{
    None(0),
    LocationBind(0x0001),
    Unique(0x0002),
    Beast(0x0004),
    QuestGiver(0x0008);

    private static Map<Integer, CreatureFlags> values = new HashMap<>();

    static
    {
        for (CreatureFlags flag : CreatureFlags.values())
            CreatureFlags.values.put(flag.value, flag);
    }

    public static CreatureFlags parse(int value)
    {
        return CreatureFlags.values.get(value);
    }

    private final int value;

    private CreatureFlags(int value)
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

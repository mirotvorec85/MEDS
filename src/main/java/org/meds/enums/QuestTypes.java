package org.meds.enums;

import org.meds.util.Valued;

public enum QuestTypes implements Valued
{
    Delivery(0),
    Kill(1),
    Collect(2),
    KillCollect(3),
    Talk(4),

    _Mining(7); // Not sure

    private static QuestTypes[] values = new QuestTypes[10];

    static
    {
        for (QuestTypes state : QuestTypes.values())
            QuestTypes.values[state.value] = state;
    }

    public static QuestTypes parse(int value)
    {
        return QuestTypes.values[value];
    }

    private final int value;

    private QuestTypes(int value)
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

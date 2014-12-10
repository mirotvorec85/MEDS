package org.meds.enums;

import org.meds.util.Valued;

public enum QuestStatuses implements Valued
{
    None(0),
    Taken(1),
    Completed(2),
    Retaket(3),
    Failed(4);

    private static QuestStatuses[] values = new QuestStatuses[5];

    static
    {
        for (QuestStatuses state : QuestStatuses.values())
            QuestStatuses.values[state.value] = state;
    }

    public static QuestStatuses parse(int value)
    {
        return QuestStatuses.values[value];
    }

    private final int value;

    private QuestStatuses(int value)
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

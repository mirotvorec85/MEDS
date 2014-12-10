package org.meds.enums;

import org.meds.util.Valued;

public enum GuildLessonStatuses implements Valued
{
    /**
     * This guild requires another guild to be learned.
     */
    Unavailable(0),
    /**
     * A Player doesn't have gold or experience required values.
     */
    RequirementsFailed(1),
    Available(2),
    Learned(3);

    private static GuildLessonStatuses[] values = new GuildLessonStatuses[4];

    static
    {
        for (GuildLessonStatuses state : GuildLessonStatuses.values())
            GuildLessonStatuses.values[state.value] = state;
    }

    public static GuildLessonStatuses parse(int value)
    {
        return GuildLessonStatuses.values[value];
    }

    private final int value;

    private GuildLessonStatuses(int value)
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

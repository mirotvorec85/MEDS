package org.meds.enums;

import org.meds.util.Valued;

public enum ClanMemberStatuses implements Valued
{
    Private(0),
    Advisor(1),
    Leader(2),
    Officer(3),
    Recruit(4);

    private static final ClanMemberStatuses[] values = new ClanMemberStatuses[5];

    static
    {
        for (ClanMemberStatuses parameter : ClanMemberStatuses.values())
            ClanMemberStatuses.values[parameter.value] = parameter;
    }

    public static ClanMemberStatuses parse(int value)
    {
        return ClanMemberStatuses.values[value];
    }

    private final int value;

    private ClanMemberStatuses(int value)
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

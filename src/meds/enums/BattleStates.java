package meds.enums;

import meds.util.Valued;

public enum BattleStates implements Valued
{
    NoBattle(0),
    EnterBattle(1),
    TargetDead(2),
    Death(3),
    Battle(4),
    Runaway(5);

    private static BattleStates[] values = new BattleStates[6];

    static
    {
        for (BattleStates state : BattleStates.values())
            BattleStates.values[state.value] = state;
    }

    public static BattleStates parse(int value)
    {
        return BattleStates.values[value];
    }

    private final int value;

    private BattleStates(int value)
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

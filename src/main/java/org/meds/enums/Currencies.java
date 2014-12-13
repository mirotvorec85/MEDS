package org.meds.enums;

import org.meds.util.Valued;

public enum Currencies implements Valued
{
    Gold(0),
    Platinum(1),
    QuestPoint(2),
    TournamentPoint(3),
    Bank(4),
    Honor(5),
    Valor(6),
    Conquest(7),
    Faith(8),
    Achievement(9),
    _epidemic(10),
    Candy(11);

    private final int value;

    private Currencies(int value)
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

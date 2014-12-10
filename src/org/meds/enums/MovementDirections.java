package org.meds.enums;

import org.meds.util.Valued;

public enum MovementDirections implements Valued
{
    Up(0),
    Down(1),
    North(2),
    South(3),
    West(4),
    East(5),

    None(6);

    private static MovementDirections[] values = new MovementDirections[7];

    static
    {
        for (MovementDirections state : MovementDirections.values())
            MovementDirections.values[state.value] = state;
    }

    public static MovementDirections parse(int value)
    {
        if (value > MovementDirections.values.length || value < 0)
            return null;
        return MovementDirections.values[value];
    }

    private final int value;

    private MovementDirections(int value)
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

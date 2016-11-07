package org.meds.enums;

import org.meds.util.Valued;

public enum Religions implements Valued {

    None(0),

    Sun(1),
    Moon(2),
    Order(3),
    Chaos(4);

    private static Religions[] values = new Religions[6];

    static {
        for (Religions state : Religions.values())
            Religions.values[state.value] = state;
    }

    public static Religions parse(int value) {
        return Religions.values[value];
    }

    private final int value;

    Religions(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
}

package org.meds.enums;

import org.meds.util.Valued;

public enum CreatureBossTypes implements Valued {

    Normal(0, -1/* No locale*/),
    Champion(1, 51),
    DeathAura(2, 52),
    Vampire(3, 53),
    Fanatic(4, 54),
    Possessed(5, 55),
    Spectral(6, 56),
    AbsorbAura(7, 57),
    Freeze(8, 58),
    Elementalist(9, 59);

    private static CreatureBossTypes[] values = new CreatureBossTypes[10];

    static {
        for (CreatureBossTypes state : CreatureBossTypes.values())
            CreatureBossTypes.values[state.value] = state;
    }

    public static CreatureBossTypes parse(int value) {
        return CreatureBossTypes.values[value];
    }

    private final int value;
    private final int titleStringId;

    CreatureBossTypes(int value, int titleStringId) {
        this.value = value;
        this.titleStringId = titleStringId;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    public int getTitleStringId() {
        return this.titleStringId;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
}

package org.meds.enums;

import org.meds.Locale;
import org.meds.util.Valued;

public enum Professions implements Valued {

    Agriculture(1, 22),
    Harvesting(2, 23),
    Melting(3, 24),
    Extraction(4, 25),
    Mining(5, 26),
    Aeronautics(6, 27),
    Herbalism(7, 28),
    Alchemy(8, 29),
    Hunting(9, 30),
    Fishing(10, 31),
    SleightOfHands(11, 32);

    private final int value;
    private final int titleId;

    private Professions(int value, int titleId) {
        this.value = value;
        this.titleId = titleId;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    public String getTitle() {
        return Locale.getString(this.titleId);
    }

    @Override
    public String toString() {
        return this.getTitle();
    }
}

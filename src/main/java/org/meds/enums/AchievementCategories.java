package org.meds.enums;

import org.meds.util.Valued;

public enum AchievementCategories implements Valued {

    PvM(1),
    PvP(2);

    private static AchievementCategories[] values = new AchievementCategories[3];

    static {
        for (AchievementCategories state : AchievementCategories.values())
            AchievementCategories.values[state.value] = state;
    }

    public static AchievementCategories parse(int value) {
        return AchievementCategories.values[value];
    }

    private final int value;

    private AchievementCategories(int value) {
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

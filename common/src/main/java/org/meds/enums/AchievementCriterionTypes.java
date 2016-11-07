package org.meds.enums;

import org.meds.util.Valued;

public enum AchievementCriterionTypes implements Valued {

    CreatureTemplate(1),
    Kingdom(2),
    Region(3),
    SpecialCreature(4),
    TargetReligion(5),
    TargetRace(6),
    TargetReligiousStatus(7),
    UnderSiege(8),
    ClanWar(9),
    AchievementComplete(10);

    private static final AchievementCriterionTypes[] values = new AchievementCriterionTypes[AchievementCriterionTypes.values().length + 1];

    static {
        for (AchievementCriterionTypes state : AchievementCriterionTypes.values())
            AchievementCriterionTypes.values[state.value] = state;
    }

    public static AchievementCriterionTypes parse(int value) {
        return AchievementCriterionTypes.values[value];
    }

    private final int value;

    AchievementCriterionTypes(int value) {
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

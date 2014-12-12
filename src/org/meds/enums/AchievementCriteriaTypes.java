package org.meds.enums;

import org.meds.util.Valued;

public enum AchievementCriteriaTypes implements Valued {

    CreatureSlain(1),
    PlayerSlain(2),
    Kingdom(3),
    Region(4),
    SpecialCreature(5),
    TargetReligion(6),
    TargetRace(7),
    TargetReligiousStatus(8),
    UnderSiege(9),
    ClanWar(10),
    AchievementComplete(11);

    private static AchievementCriteriaTypes[] values = new AchievementCriteriaTypes[12];

    static {
        for (AchievementCriteriaTypes state : AchievementCriteriaTypes.values())
            AchievementCriteriaTypes.values[state.value] = state;
    }

    public static AchievementCriteriaTypes parse(int value) {
        return AchievementCriteriaTypes.values[value];
    }

    private final int value;

    private AchievementCriteriaTypes(int value) {
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

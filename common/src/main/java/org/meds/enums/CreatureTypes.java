package org.meds.enums;

import org.meds.util.Valued;

public enum CreatureTypes implements Valued {

    Normal(0, -1/* No locale string */),
    EarthAdept(1, 35),
    FireAdept(2, 36),
    ColdAdept(3, 37),
    LightningAdept(4, 38),
    MoonCultist(5, 39),
    SunCultist(6, 40),
    OrderAdept(7, 41),
    ChaosAdept(8, 42),
    Barbarian(9, 43),
    Mauler(10, 44),
    Swordsman(11, 45),
    Monk(12, 46),
    Butcher(13, 47),
    Vindicator(14, 48),
    Assassin(15, 49),
    Duelist(16, 50);

    private static final CreatureTypes[] values = new CreatureTypes[17];

    static {
        for (CreatureTypes type : CreatureTypes.values())
            CreatureTypes.values[type.value] = type;
    }

    public static CreatureTypes parse(int value) {

        if (value < 0 || value >= CreatureTypes.values.length)
            return null;
        return CreatureTypes.values[value];
    }

    private final int value;
    private final int titleStringId;

    CreatureTypes(int value, int titleStringId) {
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
}

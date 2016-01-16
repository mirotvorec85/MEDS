package org.meds.enums;

import org.meds.util.Valued;

public enum SpecialLocationTypes implements Valued {

    Generic(0),
    Star(1),
    MilitarySchool(2),
    MagicSchool(3),
    WeaponShop(4),
    ArmourShop(5),
    DepartmentStore(6),
    JewelerShop(7),
    ScribeShop(8),
    MagicShop(9),
    Dump(10),
    AlchemicalShop(11),
    Laboratory(12),
    Bank(13),
    Bar(14),
    Inn(15),
    Dock(16),

    ArtifactWeaponShop(22),
    ArtifactArmourShop(23),
    ArtifactDepartmentStore(24),
    ArtifactJewelerShop(25),
    Arane(26),

    PrivateShop(31),

    _GoldenFlask(35);

    private static SpecialLocationTypes[] values = new SpecialLocationTypes[36];

    static {
        for (SpecialLocationTypes type : SpecialLocationTypes.values())
            SpecialLocationTypes.values[type.getValue()] = type;
    }

    public static SpecialLocationTypes parse(int value) {
        return SpecialLocationTypes.values[value];
    }

    private final int value;

    SpecialLocationTypes(int value) {
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

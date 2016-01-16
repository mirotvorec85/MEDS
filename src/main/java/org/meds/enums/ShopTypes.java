package org.meds.enums;

import org.meds.util.Valued;

public enum ShopTypes implements Valued {

    None(0),
    AlchemicalShop(1),
    ArmourShop(2),
    Dump(3),
    JewelerShop(4),
    MagicShop(5),
    ScribeShop(6),
    DepartmentStore(7),
    WeaponShop(8),
    ArtifactArmourShop(9),
    ArtifactJewelerShop(10),
    ArtifactDepartmentStore(11),
    ArtifactWeaponShop(12),
    _GoldenFlask(13);

    private static ShopTypes[] values = new ShopTypes[14];

    static {
        for (ShopTypes state : ShopTypes.values())
            ShopTypes.values[state.value] = state;
    }

    public static ShopTypes parse(int value) {
        return ShopTypes.values[value];
    }

    private final int value;

    ShopTypes(int value) {
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

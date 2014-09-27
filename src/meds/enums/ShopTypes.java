package meds.enums;

import meds.util.Valued;

public enum ShopTypes implements Valued
{
    None(0),
    AlchemicalShop(1),
    ArmourShop(2),
    Dump(3),
    JewelerShop(4),
    MagicShop(5),
    ScribeShop(6),
    DepartmentStore(7),
    WeaponShop(8),
    ArtefactArmourShop(9),
    ArtefactJewelerShop(10),
    ArtefactDepartmentStore(11),
    ArtefactWeaponShop(12),
    _GoldenFlask(13);

    private static ShopTypes[] values = new ShopTypes[14];

    static
    {
        for (ShopTypes state : ShopTypes.values())
            ShopTypes.values[state.value] = state;
    }

    public static ShopTypes parse(int value)
    {
        return ShopTypes.values[value];
    }

    private final int value;

    private ShopTypes(int value)
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

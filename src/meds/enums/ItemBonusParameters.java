package meds.enums;

import meds.util.Valued;

public enum ItemBonusParameters implements Valued
{
    BaseMinDamage(0),
    BaseMaxDamage(1),
    BaseProtection(2),
    BaseArmour(3),
    BaseChanceToHit(4),
    BonusConstitution(5),
    BonusStrength(6),
    BonusDexterity(7),
    BonusIntelligence(8),
    BonusDamage(9),
    BonusProtection(10),
    BonusChanceToHit(11),
    BonusArmour(12),
    BonusChanceToCast(13),
    BonusMagicDamage(14),
    BonusHealth(15),
    BonusMana(16),
    BonusHealthRegeneration(17),
    BonusManaRegeneration(18),
    BonusFireResistance(19),
    BonusFrostResistance(20),
    BonusLightningResistance(21),
    MinIntelligence(22),
    MinStrength(23),
    MinConstitution(24),
    MinDexterity(25),
    BonusVampiric(26);

    private static final ItemBonusParameters[] values = new ItemBonusParameters[27];

    static
    {
        for (ItemBonusParameters parameter : ItemBonusParameters.values())
            ItemBonusParameters.values[parameter.value] = parameter;
    }

    public static ItemBonusParameters parse(int value)
    {
        if (value < 0 || value >= values.length)
            return null;
        return ItemBonusParameters.values[value];
    }

    private final int value;

    private ItemBonusParameters(int value)
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

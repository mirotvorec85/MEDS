package org.meds.item;

import org.meds.util.Valued;

public enum ItemTotemicTypes implements Valued {

    None(0, 0, null, 0),
    /**
     * Bonus Constitution
     */
    Mammoth(1, 5, ItemBonusParameters.BonusConstitution, 1d),
    /**
     * Bonus Strength
     */
    Tiger(2, 6, ItemBonusParameters.BonusStrength, 1d),
    /**
     * Bonus Dexterity
     */
    Cat(3, 7, ItemBonusParameters.BonusDexterity, 1d),
    /**
     * Bonus Mind
     */
    Owl(4, 8, ItemBonusParameters.BonusIntelligence, 3d),
    /**
     * Bonus Damage
     */
    Bear(5, 9, ItemBonusParameters.BonusDamage, 1d),
    /**
     * Bonus Protection
     */
    Turtle(6, 10, ItemBonusParameters.BonusProtection, 1d),
    /**
     * Bonus Chance To Hit
     */
    Hawk(7, 11, ItemBonusParameters.BonusChanceToHit, 1d),
    /**
     * Bonus Armour
     */
    Monkey(8, 12, ItemBonusParameters.BonusArmour, 3d),
    /**
     * Bonus Chance To Cast
     */
    Octopus(9, 13, ItemBonusParameters.BonusChanceToCast, 1d),
    /**
     * Bonus Magic Damage
     */
    Spider(10, 14, ItemBonusParameters.BonusMagicDamage, 1d),
    /**
     * Bonus Health
     */
    Whale(11, 15, ItemBonusParameters.BonusHealth, 12d),
    /**
     * Bonus Mana
     */
    Dragon(12, 16, ItemBonusParameters.BonusMana, 9d),
    /**
     * Bonus Health Recovery
     */
    Reptile(13, 17, ItemBonusParameters.BonusHealthRegeneration, 0.33d),
    /**
     * Bonus Mana Recovery
     */
    Ant(14, 18, ItemBonusParameters.BonusManaRegeneration, 0.33d),
    /**
     * Bonus Fire Resistance
     */
    Scorpion(15, 19, ItemBonusParameters.BonusFireResistance, 1d),
    /**
     * Bonus Cold Resistance
     */
    Penguin(16, 20, ItemBonusParameters.BonusFrostResistance, 1d),
    /**
     * Bonus Lightning Resistance
     */
    Eel(17, 21, ItemBonusParameters.BonusLightningResistance, 1d);

    private static final ItemTotemicTypes[] values = new ItemTotemicTypes[18];

    static {
        for (ItemTotemicTypes type : ItemTotemicTypes.values())
            ItemTotemicTypes.values[type.value] = type;
    }

    public static ItemTotemicTypes parse(int value) {
        if (value < 0 || value >= values.length)
            return null;
        return ItemTotemicTypes.values[value];
    }

    private final int value;
    private final int titleStringId;
    private final ItemBonusParameters parameter;
    private final double ratio;

    ItemTotemicTypes(int value, int titleStringId, ItemBonusParameters parameter, double ratio) {
        this.value = value;
        this.titleStringId = titleStringId;
        this.parameter = parameter;
        this.ratio = ratio;
    }

    public int getTitleStringId() {
        return titleStringId;
    }

    public ItemBonusParameters getParameter() {
        return parameter;
    }

    public double getRatio() {
        return ratio;
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

package org.meds.enums;

import org.meds.util.Valued;

public enum ItemTotemicTypes implements Valued
{
    None(0),
    /**
     * Bonus Constitution
     */
    Mammoth(1),
    /**
     * Bonus Strength
     */
    Tiger(2),
    /**
     * Bonus Dexterity
     */
    Cat(3),
    /**
     * Bonus Mind
     */
    Owl(4),
    /**
     * Bonus Damage
     */
    Bear(5),
    /**
     * Bonus Protection
     */
    Turtle(6),
    /**
     * Bonus Chance To Hit
     */
    Hawk(7),
    /**
     * Bonus Armour
     */
    Monkey(8),
    /**
     * Bonus Chance To Cast
     */
    Octopus(9),
    /**
     * Bonus Magic Damage
     */
    Spider(10),
    /**
     * Bonus Health
     */
    Whale(11),
    /**
     * Bonus Mana
     */
    Dragon(12),
    /**
     * Bonus Health Recovery
     */
    Reptile(13),
    /**
     * Bonus Mana Recovery
     */
    And(14),
    /**
     * Bonus Fire Resistance
     */
    Scorpion(15),
    /**
     * Bonus Cold Resistance
     */
    Penguin(16),
    /**
     * Bonus Lightning Resistance
     */
    Eel(17);

    private static final ItemTotemicTypes[] values = new ItemTotemicTypes[18];

    static
    {
        for (ItemTotemicTypes type : ItemTotemicTypes.values())
            ItemTotemicTypes.values[type.value] = type;
    }

    public static ItemTotemicTypes parse(int value)
    {
        return ItemTotemicTypes.values[value];
    }

    private final int value;

    private ItemTotemicTypes(int value)
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

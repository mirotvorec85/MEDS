package org.meds.enums;

import org.meds.util.Valued;

public enum ItemTotemicTypes implements Valued
{
    None(0, 0),
    /**
     * Bonus Constitution
     */
    Mammoth(1, 5),
    /**
     * Bonus Strength
     */
    Tiger(2, 6),
    /**
     * Bonus Dexterity
     */
    Cat(3, 7),
    /**
     * Bonus Mind
     */
    Owl(4, 8),
    /**
     * Bonus Damage
     */
    Bear(5, 9),
    /**
     * Bonus Protection
     */
    Turtle(6, 10),
    /**
     * Bonus Chance To Hit
     */
    Hawk(7, 11),
    /**
     * Bonus Armour
     */
    Monkey(8, 12),
    /**
     * Bonus Chance To Cast
     */
    Octopus(9, 13),
    /**
     * Bonus Magic Damage
     */
    Spider(10, 14),
    /**
     * Bonus Health
     */
    Whale(11, 15),
    /**
     * Bonus Mana
     */
    Dragon(12, 16),
    /**
     * Bonus Health Recovery
     */
    Reptile(13, 17),
    /**
     * Bonus Mana Recovery
     */
    Ant(14, 18),
    /**
     * Bonus Fire Resistance
     */
    Scorpion(15, 19),
    /**
     * Bonus Cold Resistance
     */
    Penguin(16, 20),
    /**
     * Bonus Lightning Resistance
     */
    Eel(17, 21);

    private static final ItemTotemicTypes[] values = new ItemTotemicTypes[18];

    static
    {
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

    private ItemTotemicTypes(int value, int titleStringId) {
        this.value = value;
        this.titleStringId = titleStringId;
    }

    public int getTitleStringId() {
        return titleStringId;
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

package org.meds.enums;

import org.meds.util.Valued;

public enum ItemEssenceTypes implements Valued
{
    None(0),
    /**
     * Bonus Health
     */
    Life(1),
    /**
     * Bonus Mana
     */
    Sorcery(2),
    /**
     * Bonus Damage
     */
    Violence(3),
    /**
     * Bonus Magic Damage
     */
    Hatred(4),
    /**
     * Bonus Chance To Hit
     */
    Precision(5),
    /**
     * Bonus Chance To Cast
     */
    Knowledge(6),
    /**
     * Bonus Power (Strength)
     */
    Power(7),
    /**
     * Bonus Dexterity
     */
    Quickness(8),
    /**
     * Bonus Mind (Intelligence)
     */
    Mind(9),
    /**
     * Bonus Constitution
     */
    Might(10),
    /**
     * Bonus Armour
     */
    Wind(11),
    /**
     * Bonus Protection
     */
    Stone(12),
    /**
     * Bonus Health Recovery
     */
    Spring(13),
    /**
     * Bonus Mana recovery
     */
    Mystic(14),
    /**
     * Fire resistance
     */
    Flame(15),
    /**
     * Bonus Cold resistance
     */
    Ice(16),
    /**
     * Bonus Lightning resistance
     */
    Lightning(17),
    /**
     * Bonus Fire, Cold, Lightning resistance
     */
    Elements(18),
    /**
     * Bonus HP for each of your hit/combat spell
     */
    Vampire(19),
    /**
     * Bonus % of damage reflection (similar to Mirror Shield)
     */
    Reflection(20),
    /**
     * Bonus % of damage absorption
     */
    Soaking(21),
    /**
     * Bonus MP for each of your hit/combat spell
     */
    Understanding(22),
    /**
     * Bonus % suppress absorption/damage reflection
     */
    Supression(23),
    /**
     * Bonus % of maximum load capacity
     */
    Lightness(24),
    /**
     * God remembers what is it.
     */
    Destruction(25);


    private static final ItemEssenceTypes[] values = new ItemEssenceTypes[26];

    static
    {
        for (ItemEssenceTypes type : ItemEssenceTypes.values())
            ItemEssenceTypes.values[type.value] = type;
    }

    public static ItemEssenceTypes parse(int value)
    {
        return ItemEssenceTypes.values[value];
    }

    private final int value;

    private ItemEssenceTypes(int value)
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

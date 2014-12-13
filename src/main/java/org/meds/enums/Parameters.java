package org.meds.enums;

import java.util.HashMap;
import java.util.Map;

import org.meds.util.Valued;

public enum Parameters implements Valued
{
    None(0),
    /* Base Parameters */
    Constitution(1),
    Strength(2),
    Dexterity(3),
    Intelligence(4),
    /// <summary>
    /// MinDamage or clean Damage (from Strength and Dexterity stats, guilds or essences + Parameters.MinDamage)
    /// </summary>
    Damage(5),

    Protection(6),
    ChanceToHit(7),
    Armour(8),
    ChanceToCast(9),
    MagicDamage(10),
    Health (11),
    Mana(12),
    HealthRegeneration(13),
    ManaRegeneration(14),
    FireResistance(15),
    FrostResistance(16),
    LightningResistance(17),
    /// <summary>
    /// Supposed source is Weapon Max Damage and Auras Max Damage only
    /// </summary>
    MaxDamage(18),
    MaxWeight(19),
    Suppress(20),
    Absorb(21),
    Reflect(22),

    /// <summary>
    /// Hack parameter.
    /// Supposed source is Weapon Min Damage and Auras Min Damage only
    /// </summary>
    MinDamage(100),
    AllResistance(101);

    private static final Map<Integer, Parameters> values = new HashMap<Integer, Parameters>();

    static
    {
        for (Parameters parameter : Parameters.values())
            Parameters.values.put(parameter.value, parameter);
    }

    public static Parameters parse(int value)
    {
        return Parameters.values.get(value);
    }

    private final int value;

    private Parameters(int value)
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

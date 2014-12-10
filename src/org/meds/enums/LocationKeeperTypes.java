package org.meds.enums;

import org.meds.util.Valued;

public enum LocationKeeperTypes implements Valued
{
    /**
     * Ordinary Location.
     */
    None(0),
    Banker(1),
    Bartender(2),
    /**
     * Inn
     */
    Headwaither(3),
    /**
     * Jewelery and Artifact Jewelery Shops
     */
    Jeweler(4),
    /**
     * Dump
     */
    DumpMerchant(5),
    /**
     * Laboratory, Magic and Alchemical Shops, Magic Guilds
     */
    Magician(6),
    /**
     * Military Guilds
     */
    Trainer(7),
    /**
     * Department Store, Weapon/Armour Shops
     */
    Merchant(8),
    Scribe(9);

    private static LocationKeeperTypes[] values = new LocationKeeperTypes[10];

    static
    {
        for (LocationKeeperTypes state : LocationKeeperTypes.values())
            LocationKeeperTypes.values[state.value] = state;
    }

    public static LocationKeeperTypes parse(int value)
    {
        return LocationKeeperTypes.values[value];
    }

    private final int value;

    private LocationKeeperTypes(int value)
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

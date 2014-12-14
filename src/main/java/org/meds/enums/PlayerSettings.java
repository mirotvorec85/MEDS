package org.meds.enums;

import java.util.HashMap;
import java.util.Map;

import org.meds.util.Valued;

public enum PlayerSettings implements Valued
{
    AutoLoot(0x0001),
    AutoAttack(0x0002),
    unk4(0x0004),
    unk8(0x0008),
    unk16(0x0010),
    unk32(0x0020),
    FilterSoft(0x0040),
    FilterTotal(0x0080),
    FilterTarget(0x0100),
    FilterSpells(0x0200),
    Asceticism(0x0400);

    private static Map<Integer, PlayerSettings> values = new HashMap<>();

    static
    {
        for (PlayerSettings settings : PlayerSettings.values())
            PlayerSettings.values.put(settings.value, settings);
    }

    public static PlayerSettings parse(int value)
    {
        return PlayerSettings.values.get(value);
    }

    private final int value;

    private PlayerSettings(int value)
    {
        this.value = value;
    }

    @Override
	public int getValue()
    {
        return this.value;
    }
}

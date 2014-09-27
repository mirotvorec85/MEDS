package meds.enums;

import java.util.HashMap;
import java.util.Map;

import meds.util.Valued;

public enum PlayerStatuses implements Valued
{
    Player(0x00000),
    Moderator(0x00001),
    Dealer(0x00002),
    Admin(0x00004),

    Trader(0x00010),
    Helper(0x00020),
    Justice(0x00040),
    MainJustice(0x00080),
    Pastor(0x00100),
    Silent(0x00200),
    HardLabor(0x00400),
    MainModerator(0x00800),
    Referee(0x01000);

    private static Map<Integer, PlayerStatuses> values = new HashMap<Integer, PlayerStatuses>();

    static
    {
        for (PlayerStatuses settings : PlayerStatuses.values())
            PlayerStatuses.values.put(settings.value, settings);
    }

    public static PlayerStatuses parse(int value)
    {
        return PlayerStatuses.values.get(value);
    }

    private final int value;

    private PlayerStatuses(int value)
    {
        this.value = value;
    }

    @Override
	public int getValue()
    {
        return this.value;
    }
}

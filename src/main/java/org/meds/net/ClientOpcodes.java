package org.meds.net;

import java.util.HashMap;
import java.util.Map;

public enum ClientOpcodes
{
    Ping("0"),
    SetHome("acity"),
    Attack("at"),
    BuyBackItem("bbi"), // Appeared in 2.0.0.0
    BuyItem("bi"),
    BankExchange("chp"),
    DestroyItem("di"),
    GetLocationInfo("gei"), // Appeared in 2.0.0.0
    @Deprecated
    EnterGuild("gg"), // Deprecated after 2.0.0.0
    GetItemInfo("gii"),
    GetInn("ginn"),
    GuildLearn("gl"),
    GuildLessonsInfo("gni"), // Appeared in 2.0.0.0 after new guild system.
    Movement("go"),
    GetGuildLevels("glvl"),
    EnterShop("gs"),
    LootCorpse("gtr"),
    InnFilter("ift"),
    InnGet("ig"),
    InnStore("is"),
    LearnGuildInfo("lni"), // Appeared in 2.0.0.0 after new guild system.
    Login("login"),
    Ready("lur"),
    PutMoney("pm"),
    QuestAccept("qa"),
    QuestListFilter("qf"),
    GetQuestInfo("qi"),
    QuestInfoForAccept("qre"),
    RegionLocations("r"),
    SwapItem("ri"),
    Relax("rx"),
    SetAutoLoot("sal"),
    Say("say"),
    EnterStar("scity"),
    SellItem("sell"),
    SetAutoSpell("seta"),
    TakeMoney("tm"),
    UseMagic("um"),
    UseItem("use"),
    Verification("ver"),
    Whisper("wisp");

    private static final Map<String, ClientOpcodes> values = new HashMap<>();

    static
    {
        for (ClientOpcodes opcode : ClientOpcodes.values())
            ClientOpcodes.values.put(opcode.value, opcode);
    }

    public static ClientOpcodes parse(String opcode)
    {
        return ClientOpcodes.values.get(opcode);
    }

    private final String value;

    private ClientOpcodes(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return this.value;
    }
}

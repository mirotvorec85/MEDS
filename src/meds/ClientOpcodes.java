package meds;

import java.util.HashMap;
import java.util.Map;

public enum ClientOpcodes
{
    Ping("0"),
    SetHome("acity"),
    Attack("at"),
    BuyItem("bi"),
    BankExchange("chp"),
    DestroyItem("di"),
    EnterGuild("gg"),
    GetItemInfo("gii"),
    GetInn("ginn"),
    GuildLearn("gl"),
    Movement("go"),
    GetGuildLevels("glvl"),
    EnterShop("gs"),
    LootCorpse("gtr"),
    InnFilter("ift"),
    InnGet("ig"),
    InnStore("is"),
    Login("login"),
    Ready("lur"),
    PutMoney("pm"),
    QuestAccept("qa"),
    QuestListFilter("qf"),
    GetQuestInfo("qi"),
    QuestInfoForAccept("qre"),
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

    private static Map<String, ClientOpcodes> values = new HashMap<String, ClientOpcodes>();

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

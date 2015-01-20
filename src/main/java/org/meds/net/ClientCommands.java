package org.meds.net;

import java.util.HashMap;
import java.util.Map;

public enum ClientCommands
{
    Ping("0"),
    SetHome("acity"),
    Attack("at"),
    BuyBackItem("bbi"), // Appeared in 2.0.0.0
    BuyItem("bi"),
    TradeApply("ch_apply"),
    TradeCancel("ch_depp"),
    TradeUpdate("change"),
    BankExchange("chp"),
    DestroyItem("di"),
    GetLocationInfo("gei"), // Appeared in 2.0.0.0
    GetTrade("get_ch"),
    @Deprecated
    EnterGuild("gg"), // Deprecated after 2.0.0.0
    GetItemInfo("gii"),
    GetInn("ginn"),
    GuildLearn("gl"),
    GuildLessonsInfo("gni"), // Appeared in 2.0.0.0 after new guild system.
    Movement("go"),
    GetGuildLevels("lvl"),
    GetProfessions("gproff"),
    GroupCreate("grc"),
    GroupJoin("grj"),
    GroupKick("grk"),
    GroupSettingsChange("grl"),
    GroupQuit("grq"),
    GroupChangeLeader("grx"),
    GroupDisband("grz"),
    EnterShop("gs"),
    LootCorpse("gtr"),
    InnFilter("ift"),
    InnGet("ig"),
    InnStore("is"),
    LearnGuildInfo("lni"), // Appeared in 2.0.0.0 after new guild system.
    Login("login"),
    Ready("lur"),
    SetAsceticism("opt_asket"),
    PutMoney("pm"),
    QuestAccept("qa"),
    QuestListFilter("qf"),
    GetQuestInfo("qi"),
    QuestInfoForAccept("qre"),
    RegionLocations("r"),
    SwapItem("ri"),
    RemoveLevel("rlvl"),
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

    private static final Map<String, ClientCommands> values = new HashMap<>();

    static
    {
        for (ClientCommands command : ClientCommands.values())
            ClientCommands.values.put(command.value, command);
    }

    public static ClientCommands parse(String command)
    {
        return ClientCommands.values.get(command);
    }

    private final String value;

    private ClientCommands(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return this.value;
    }
}

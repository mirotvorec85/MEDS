package meds;

public enum ServerOpcodes
{
    Aura("a"),
    CultInfo("ci"),
    BonusMagicParameter("cms"),
    UpdateQuest("cq"),
    _cs("cs"),
    DeleteAura("d"),
    DayTime("dn"),
    EquipmentInfo("eq"),
    Experience("exp"),
    _fex("fex"),
    _fpi("fpi"),
    GuildInfo("gi"),
    GuildLessonInfo("ginf"),
    GetCorpse("gtr"),
    _hoi("hoi"),
    Health("hp"),
    _hs("hs"),
    ItemInfo("ii"),
    Inn("inn"),
    InventoryUpdate("inu"),
    InventoryInfo("inv"),
    _invt("invt"),
    ClanInfo("kinf"),
    Currencies("l$"), // Appeared in 1.2.7.6
    PlayersListAdd("la"),
    PlayersListDelete("ld"),
    OnlineList("lf"),
    _lh0("lh0"),
    Location("loc"),
    LoginResult("login_result"),
    QuestListInfo("lq"),
    PlayersListUpdate("lu"),
    GuildLevels("lvl"),
    AutoSpell("mb"),
    _mem("mem"), // Character Notes
    ServerMessage("mes"),
    MagicInfo("mi"),
    ChatMessage("msg"),
    MessageList("msl"),
    NoGo("ng"),
    _omg("omg"),
    Professions("prof"),
    _prot1("prot1"),
    _prot2("prot2"),
    PositionUnitList("pss"),
    MoneyInfo("$"), // No longer exists after 1.2.7.6 (See l$ opcode instead)
    QuestInfo("q"),
    QuestFinalText("qft"),
    NpcQuestList("qli"),
    RelaxOff("r0"),
    RelaxOn("r1"),
    StarInfo("rcity"),
    _s0("s0"),
    SkillInfo("si"),
    ShopInfo("sinf"),
    Sound("snd"),
    QuestInfoForAccept("sq"),
    PlayerInfo("sti"),
    _swf("swf"),
    ServerTime("t"),
    _tc("tc"),
    CorpseList("tl"),
    Version("version"),
    Currency("w$"), // Appeared in 1.2.7.6
    BattleState("war"),
    _wg("wg"),
    _zzz("zzz");

    private final String value;

    private ServerOpcodes(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return this.value;
    }
}

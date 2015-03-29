package org.meds.database.entity;

import java.util.HashMap;
import java.util.Map;

import org.meds.Item.Prototype;

public class CharacterInfo
{
    private int characterId;
    private String name;
    private int avatarId;
    private int race;
    private int clanId;
    private int clanStatus;
    private int baseCon;
    private int baseStr;
    private int baseDex;
    private int baseInt;
    private int guildCon;
    private int guildStr;
    private int guildDex;
    private int guildInt;
    private int guildDam;
    private int guildAbs;
    private int guildChth;
    private int guildAc;
    private int guildChtc;
    private int guildMdam;
    private int guildHp;
    private int guildMp;
    private int guildHpRegen;
    private int guildMpRegen;
    private int guildFireResist;
    private int guildFrostResist;
    private int guildShockResist;
    private int health;
    private int mana;
    private int exp;
    private int religExp;
    private int level;
    private int religLevel;
    private int locationId;
    private int homeId;
    private Integer autoSpellId;
    private int settings;
    private int statuses;
    private double practiseValue;
    private String notepad;

    private Map<Integer, CharacterAchievement> achievements = new HashMap<>();
    private Map<Integer, CharacterGuild> guilds = new HashMap<>();
    private Map<Integer, CharacterCurrency> currencies = new HashMap<>();
    private Map<Prototype, CharacterInnItem> innItems = new HashMap<>();
    private Map<Integer, CharacterInventoryItem> inventoryItems = new HashMap<>();
    private Map<Integer, CharacterProfession> professions = new HashMap<>();
    private Map<Integer, CharacterQuest> quests = new HashMap<>();
    private Map<Integer, CharacterSkill> skills = new HashMap<>();
    private Map<Integer, CharacterSpell> spells = new HashMap<>();

    public int getCharacterId()
    {
        return characterId;
    }
    public void setCharacterId(int characterId)
    {
        this.characterId = characterId;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public int getAvatarId()
    {
        return avatarId;
    }
    public void setAvatarId(int avatarId)
    {
        this.avatarId = avatarId;
    }
    public int getRace()
    {
        return race;
    }
    public void setRace(int race)
    {
        this.race = race;
    }
    public int getClanId()
    {
        return clanId;
    }
    public void setClanId(Integer clanId)
    {
        if (clanId == null)
            this.clanId = 0;
        else
            this.clanId = clanId;
    }
    public int getClanStatus()
    {
        return clanStatus;
    }
    public void setClanStatus(int clanStatus)
    {
        this.clanStatus = clanStatus;
    }
    public int getBaseCon()
    {
        return baseCon;
    }
    public void setBaseCon(int baseCon)
    {
        this.baseCon = baseCon;
    }
    public int getBaseStr()
    {
        return baseStr;
    }
    public void setBaseStr(int baseStr)
    {
        this.baseStr = baseStr;
    }
    public int getBaseDex()
    {
        return baseDex;
    }
    public void setBaseDex(int baseDex)
    {
        this.baseDex = baseDex;
    }
    public int getBaseInt()
    {
        return baseInt;
    }
    public void setBaseInt(int baseInt)
    {
        this.baseInt = baseInt;
    }
    public int getGuildCon()
    {
        return guildCon;
    }
    public void setGuildCon(int guildCon)
    {
        this.guildCon = guildCon;
    }
    public int getGuildStr()
    {
        return guildStr;
    }
    public void setGuildStr(int guildStr)
    {
        this.guildStr = guildStr;
    }
    public int getGuildDex()
    {
        return guildDex;
    }
    public void setGuildDex(int guildDex)
    {
        this.guildDex = guildDex;
    }
    public int getGuildInt()
    {
        return guildInt;
    }
    public void setGuildInt(int guildInt)
    {
        this.guildInt = guildInt;
    }
    public int getGuildDam()
    {
        return guildDam;
    }
    public void setGuildDam(int guildDam)
    {
        this.guildDam = guildDam;
    }
    public int getGuildAbs()
    {
        return guildAbs;
    }
    public void setGuildAbs(int guildAbs)
    {
        this.guildAbs = guildAbs;
    }
    public int getGuildChth()
    {
        return guildChth;
    }
    public void setGuildChth(int guildChth)
    {
        this.guildChth = guildChth;
    }
    public int getGuildAc()
    {
        return guildAc;
    }
    public void setGuildAc(int guildAc)
    {
        this.guildAc = guildAc;
    }
    public int getGuildChtc()
    {
        return guildChtc;
    }
    public void setGuildChtc(int guildChtc)
    {
        this.guildChtc = guildChtc;
    }
    public int getGuildMdam()
    {
        return guildMdam;
    }
    public void setGuildMdam(int guildMdam)
    {
        this.guildMdam = guildMdam;
    }
    public int getGuildHp()
    {
        return guildHp;
    }
    public void setGuildHp(int guildHp)
    {
        this.guildHp = guildHp;
    }
    public int getGuildMp()
    {
        return guildMp;
    }
    public void setGuildMp(int guildMp)
    {
        this.guildMp = guildMp;
    }
    public int getGuildHpRegen()
    {
        return guildHpRegen;
    }
    public void setGuildHpRegen(int guildHpRegen)
    {
        this.guildHpRegen = guildHpRegen;
    }
    public int getGuildMpRegen()
    {
        return guildMpRegen;
    }
    public void setGuildMpRegen(int guildMpRegen)
    {
        this.guildMpRegen = guildMpRegen;
    }
    public int getGuildFireResist()
    {
        return guildFireResist;
    }
    public void setGuildFireResist(int guildFireResist)
    {
        this.guildFireResist = guildFireResist;
    }
    public int getGuildFrostResist()
    {
        return guildFrostResist;
    }
    public void setGuildFrostResist(int guildFrostResist)
    {
        this.guildFrostResist = guildFrostResist;
    }
    public int getGuildShockResist()
    {
        return guildShockResist;
    }
    public void setGuildShockResist(int guildShockResist)
    {
        this.guildShockResist = guildShockResist;
    }
    public int getHealth()
    {
        return health;
    }
    public void setHealth(int health)
    {
        this.health = health;
    }
    public int getMana()
    {
        return mana;
    }
    public void setMana(int mana)
    {
        this.mana = mana;
    }
    public int getExp()
    {
        return exp;
    }
    public void setExp(int exp)
    {
        this.exp = exp;
    }
    public int getReligExp()
    {
        return religExp;
    }
    public void setReligExp(int religExp)
    {
        this.religExp = religExp;
    }
    public int getLevel()
    {
        return level;
    }
    public void setLevel(int level)
    {
        this.level = level;
    }
    public int getReligLevel()
    {
        return religLevel;
    }
    public void setReligLevel(int religLevel)
    {
        this.religLevel = religLevel;
    }
    public int getLocationId()
    {
        return locationId;
    }
    public void setLocationId(int locationId)
    {
        this.locationId = locationId;
    }
    public int getHomeId()
    {
        return homeId;
    }
    public void setHomeId(int homeId) {
        this.homeId = homeId;
    }

    public Integer getAutoSpellId() {
        return this.autoSpellId;
    }
    public void setAutoSpellId(Integer autoSpellId)
    {
        this.autoSpellId = autoSpellId;
    }
    public int getSettings()
    {
        return settings;
    }
    public void setSettings(int settings)
    {
        this.settings = settings;
    }
    public int getStatuses()
    {
        return statuses;
    }
    public void setStatuses(int statuses)
    {
        this.statuses = statuses;
    }

    public double getPractiseValue() {
        return practiseValue;
    }

    public void setPractiseValue(double practiseValue) {
        this.practiseValue = practiseValue;
    }

    public String getNotepad() {
        return notepad;
    }

    public void setNotepad(String notepad) {
        this.notepad = notepad;
    }

    public Map<Integer, CharacterAchievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(Map<Integer, CharacterAchievement> achievements) {
        this.achievements = achievements;
    }
    public Map<Integer, CharacterGuild> getGuilds()
    {
        return this.guilds;
    }
    public void setGuilds(Map<Integer, CharacterGuild> guilds)
    {
        this.guilds = guilds;
    }

    public Map<Integer, CharacterCurrency> getCurrencies()
    {
        return currencies;
    }
    public void setCurrencies(Map<Integer, CharacterCurrency> currencies)
    {
        this.currencies = currencies;
    }
    public Map<Prototype, CharacterInnItem> getInnItems()
    {
        return innItems;
    }
    public void setInnItems(Map<Prototype, CharacterInnItem> innItems)
    {
        this.innItems = innItems;
    }
    public Map<Integer, CharacterInventoryItem> getInventoryItems()
    {
        return inventoryItems;
    }
    public void setInventoryItems(
            Map<Integer, CharacterInventoryItem> inventoryItems)
    {
        this.inventoryItems = inventoryItems;
    }
    public Map<Integer, CharacterProfession> getProfessions() {
        return professions;
    }
    public void setProfessions(Map<Integer, CharacterProfession> professions) {
        this.professions = professions;
    }
    public Map<Integer, CharacterQuest> getQuests()
    {
        return quests;
    }
    public void setQuests(Map<Integer, CharacterQuest> quests)
    {
        this.quests = quests;
    }
    public Map<Integer, CharacterSkill> getSkills()
    {
        return skills;
    }
    public void setSkills(Map<Integer, CharacterSkill> skills)
    {
        this.skills = skills;
    }
    public Map<Integer, CharacterSpell> getSpells()
    {
        return spells;
    }
    public void setSpells(Map<Integer, CharacterSpell> spells)
    {
        this.spells = spells;
    }
}

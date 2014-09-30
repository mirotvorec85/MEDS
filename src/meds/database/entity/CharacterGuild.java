package meds.database.entity;

import java.io.Serializable;

import meds.database.DBStorage;

public class CharacterGuild implements Serializable
{
    private static final long serialVersionUID = 3887257152944922197L;

    private int characterId;
    private int guildId;
    private int level;

    private Guild guild;

    public CharacterGuild() { }

    public CharacterGuild(int characterId, int guildId, int level)
    {
        this.characterId = characterId;
        setGuildId(guildId);
        this.level = level;
    }

    public int getCharacterId()
    {
        return characterId;
    }

    public void setCharacterId(int characterId)
    {
        this.characterId = characterId;
    }

    public int getGuildId()
    {
        return guildId;
    }

    public void setGuildId(int guildId)
    {
        this.guildId = guildId;
        this.guild = DBStorage.GuildStore.get(guildId);
    }

    public Guild getGuild()
    {
        return this.guild;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof CharacterGuild))
            return false;
        CharacterGuild cObj = (CharacterGuild)obj;

        return this.characterId == cObj.characterId && this.guildId == cObj.guildId;
    }

    @Override
    public int hashCode()
    {
        return this.characterId * 1000 + this.guildId;
    }
}

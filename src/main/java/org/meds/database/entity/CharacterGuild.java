package org.meds.database.entity;

import java.io.Serializable;

public class CharacterGuild implements Serializable
{
    private static final long serialVersionUID = 3887257152944922197L;

    private int characterId;
    private int guildId;
    private int level;

    public CharacterGuild() { }

    public CharacterGuild(int characterId, int guildId) {
        setCharacterId(characterId);
        setGuildId(guildId);
    }

    public int getCharacterId()
    {
        return characterId;
    }

    public void setCharacterId(int characterId)
    {
        this.characterId = characterId;
    }

    public int getGuildId() {
        return guildId;
    }

    public void setGuildId(int guildId)
    {
        this.guildId = guildId;
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

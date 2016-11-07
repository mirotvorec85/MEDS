package org.meds.data.domain;

import java.io.Serializable;

public class CharacterSpell implements Serializable {

    private static final long serialVersionUID = 5161464979848199454L;

    private int characterId;
    private int spellId;
    private int level;

    public CharacterSpell() {
    }

    public CharacterSpell(int characterId, int spellId, int level) {
        this.characterId = characterId;
        this.spellId = spellId;
        this.level = level;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getSpellId() {
        return spellId;
    }

    public void setSpellId(int spellId) {
        this.spellId = spellId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int hashCode() {
        return this.spellId * 1000000 + this.characterId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof CharacterSpell))
            return false;
        CharacterSpell cObj = (CharacterSpell) obj;

        return this.characterId == cObj.characterId && this.spellId == cObj.spellId;
    }

}

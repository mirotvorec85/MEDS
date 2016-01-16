package org.meds.database.entity;

import java.io.Serializable;

public class CharacterSkill implements Serializable {

    private static final long serialVersionUID = 5161464979848199454L;

    private int characterId;
    private int skillId;
    private int level;

    public CharacterSkill() {
    }

    public CharacterSkill(int skillId, int level) {
        this.skillId = skillId;
        this.level = level;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int hashCode() {
        return this.skillId * 1000000 + this.characterId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof CharacterSkill))
            return false;
        CharacterSkill cObj = (CharacterSkill) obj;

        return this.characterId == cObj.characterId && this.skillId == cObj.skillId;
    }

}

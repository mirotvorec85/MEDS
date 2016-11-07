package org.meds.data.domain;

import java.io.Serializable;

public class CharacterProfession implements Serializable {

    private static final long serialVersionUID = 1L;

    private int characterId;
    private int professionId;
    private int level;
    private double experience;

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getProfessionId() {
        return professionId;
    }

    public void setProfessionId(int professionId) {
        this.professionId = professionId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!obj.getClass().equals(CharacterProfession.class))
            return false;
        CharacterProfession that = (CharacterProfession) obj;
        return this.characterId == that.characterId &&
                this.professionId == that.professionId;
    }

    @Override
    public int hashCode() {
        return this.professionId * 1000 + this.characterId;
    }
}

package org.meds.data.domain;

public class LevelCost {

    private int level;
    private int experience;
    private int gold;

    private LevelCost nextLevelCost;
    private LevelCost prevLevelCost;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }
}

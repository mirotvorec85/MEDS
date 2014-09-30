package meds.database.entity;

import meds.database.DBStorage;

public class LevelCost
{
    private int level;
    private int experience;
    private int gold;

    private LevelCost nextLevelCost;
    private LevelCost prevLevelCost;

    public int getLevel()
    {
        return level;
    }
    public void setLevel(int level)
    {
        this.level = level;
    }
    public int getExperience()
    {
        return experience;
    }
    public void setExperience(int experience)
    {
        this.experience = experience;
    }
    public int getGold()
    {
        return gold;
    }
    public void setGold(int gold)
    {
        this.gold = gold;
    }

    public LevelCost getNextLevelCost()
    {
        if (this.nextLevelCost == null)
        {
            this.nextLevelCost = DBStorage.LevelCostStore.get(level + 1);
        }
        return this.nextLevelCost;
    }

    public LevelCost getPrevLevelCost()
    {
        if (this.prevLevelCost == null)
        {
            this.prevLevelCost = DBStorage.LevelCostStore.get(level - 1);
        }
        return this.prevLevelCost;
    }
}

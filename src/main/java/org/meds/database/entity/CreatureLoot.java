package org.meds.database.entity;

import java.io.Serializable;

public class CreatureLoot implements Serializable {

    private static final long serialVersionUID = -4230986182694368386L;

    private int creatureTemplateId;
    private int itemTemplateId;
    private int chance;
    private int count;

    public int getCreatureTemplateId() {
        return creatureTemplateId;
    }

    public void setCreatureTemplateId(int creatureTemplateId) {
        this.creatureTemplateId = creatureTemplateId;
    }

    public int getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(int itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public int getChance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CreatureLoot))
            return false;
        CreatureLoot cObj = (CreatureLoot) obj;

        return this.creatureTemplateId == cObj.creatureTemplateId && this.itemTemplateId == cObj.itemTemplateId;
    }

    @Override
    public int hashCode() {
        return this.creatureTemplateId * 1000000 + this.itemTemplateId;
    }
}

package meds.database.entity;

import java.io.Serializable;

public class CharacterInventoryItem implements Serializable
{
    private static final long serialVersionUID = -2949179500580750882L;

    private int characterId;
    private int slot;
    private int itemTemplateId;
    private int modification;
    private int durability;
    private int count;

    public int getCharacterId()
    {
        return characterId;
    }
    public void setCharacterId(int characterId)
    {
        this.characterId = characterId;
    }
    public int getSlot()
    {
        return slot;
    }
    public void setSlot(int slot)
    {
        this.slot = slot;
    }
    public int getItemTemplateId()
    {
        return itemTemplateId;
    }
    public void setItemTemplateId(int itemTemplateId)
    {
        this.itemTemplateId = itemTemplateId;
    }
    public int getModification()
    {
        return modification;
    }
    public void setModification(int modification)
    {
        this.modification = modification;
    }
    public int getDurability()
    {
        return durability;
    }
    public void setDurability(int durability)
    {
        this.durability = durability;
    }
    public int getCount()
    {
        return count;
    }
    public void setCount(int count)
    {
        this.count = count;
    }
    @Override
    public int hashCode()
    {
        return this.characterId * 1000 + this.slot;
    }
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof CharacterInventoryItem))
            return false;
        CharacterInventoryItem cObj = (CharacterInventoryItem) obj;

        return this.characterId == cObj.characterId && this.slot == cObj.slot;
    }
}

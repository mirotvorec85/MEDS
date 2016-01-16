package org.meds.database.entity;

import java.io.Serializable;

import org.meds.Item.Prototype;

public class CharacterInnItem implements Serializable {

    private static final long serialVersionUID = 3117954164373143395L;

    private int characterId;
    private int itemTemplateId;
    private int modification;
    private int durability;
    private int count;

    public CharacterInnItem() {

    }

    // TODO: Check primary key assignment
    public CharacterInnItem(int characterId, Prototype prototype, int count) {
        this.characterId = characterId;
        this.itemTemplateId = prototype.getTemplateId();
        this.modification = prototype.getModification();
        this.durability = prototype.getDurability();
        this.count = count;
    }

    public CharacterInnItem(int characterId, Prototype prototype) {
        this(characterId, prototype, 0);
    }

    @Override
    public int hashCode() {
        return this.characterId * 1000 + (this.itemTemplateId ^ this.modification ^ this.durability);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof CharacterInnItem))
            return false;
        CharacterInnItem cObj = (CharacterInnItem) obj;

        return this.characterId == cObj.characterId &&
                this.itemTemplateId == cObj.itemTemplateId &&
                this.modification == cObj.modification &&
                this.durability == cObj.durability;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(int itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public int getModification() {
        return modification;
    }

    public void setModification(int modification) {
        this.modification = modification;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

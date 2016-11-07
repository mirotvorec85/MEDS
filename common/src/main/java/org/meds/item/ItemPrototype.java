package org.meds.item;

import java.io.Serializable;

/**
 * Created by Romman on 05.11.2016.
 */
public class ItemPrototype implements Serializable, Cloneable {

    private static final long serialVersionUID = -2685946807316472347L;
    private int templateId;
    private int modification;
    private int durability;

    public ItemPrototype() {

    }

    public ItemPrototype(int templateId, int modification, int durability) {
        this.templateId = templateId;
        this.modification = modification;
        this.durability = durability;
    }

    public int getTemplateId() {
        return templateId;
    }

    private void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getModification() {
        return modification;
    }

    private void setModification(int modification) {
        this.modification = modification;
    }

    public int getDurability() {
        return durability;
    }

    private void setDurability(int durability) {
        this.durability = durability;
    }

    @Override
    public int hashCode() {
        return (this.templateId * 1000 + this.durability) | this.modification;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof ItemPrototype)
            return this.equals((ItemPrototype) obj);

        return false;
    }

    public boolean equals(ItemPrototype prototype) {
        if (prototype == null)
            return false;

        return this.templateId == prototype.templateId &&
                this.modification == prototype.modification &&
                this.durability == prototype.durability;
    }

    @Override
    public String toString() {
        return "ItemPrototype - ID: " + this.templateId +
                "; Mod: " + this.modification + "; Dur: " + this.durability;
    }
}

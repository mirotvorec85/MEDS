package org.meds.database.entity;

import org.meds.enums.SpellTypes;

public class Spell {

    private int id;
    private SpellTypes type;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeInteger() {
        return type.getValue();
    }

    public void setTypeInteger(int type) {
        this.type = SpellTypes.parse(type);
    }

    public SpellTypes getType() {
        return this.type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Spell)) {
            return false;
        }
        Spell cObj = (Spell)obj;
        return this.id == cObj.id;
    }
}

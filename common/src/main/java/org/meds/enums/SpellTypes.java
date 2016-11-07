package org.meds.enums;

public enum SpellTypes {

    Combat(0),
    Effect(1),
    unk2(2);

    public static SpellTypes parse(int value) {
        switch (value) {
            case 0:
                return Combat;
            case 1:
                return Effect;
            case 2:
                return unk2;
            default:
                return null;
        }
    }

    private final int value;

    SpellTypes(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }
}

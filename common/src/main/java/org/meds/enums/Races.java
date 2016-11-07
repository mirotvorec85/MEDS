package org.meds.enums;

public enum Races {

    Generic(0),
    Human(1),
    Dwarf(2),
    Elf(3),
    Orc(4),
    Drow(5);

    public static Races parse(int value) {
        switch (value) {
            case 1:
                return Human;
            case 2:
                return Dwarf;
            case 3:
                return Elf;
            case 4:
                return Orc;
            case 5:
                return Drow;
            default:
                return Generic;
        }
    }

    private final int value;

    Races(int value) {
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

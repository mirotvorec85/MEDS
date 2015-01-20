package org.meds.profession;

import org.meds.Player;
import org.meds.database.entity.CharacterProfession;
import org.meds.enums.Professions;

public abstract class Profession {

    public static Profession createProfession(Professions profession,
                                              CharacterProfession characterProfession,
                                              Player player) {

        Profession profInstance;
        switch (profession) {
            case Aeronautics:
                profInstance = new Aeronautics();
                break;
            case Agriculture:
                profInstance = new Agriculture();
                break;
            case Alchemy:
                profInstance = new Alchemy();
                break;
            case Extraction:
                profInstance = new Extraction();
                break;
            case Fishing:
                profInstance = new Fishing();
                break;
            case Harvesting:
                profInstance = new Harvesting();
                break;
            case Herbalism:
                profInstance = new Herbalism();
                break;
            case Hunting:
                profInstance = new Hunting();
                break;
            case Melting:
                profInstance = new Melting();
                break;
            case Mining:
                profInstance = new Mining();
                break;
            case SleightOfHands:
                profInstance = new SleightOfHands();
                break;
            default: return null;
        }

        profInstance.characterProfession = characterProfession;
        profInstance.profession = profession;
        profInstance.player = player;
        profInstance.init();
        return profInstance;
    }

    protected CharacterProfession characterProfession;
    private Professions profession;
    private Player player;

    public Professions getProfession() {
        return this.profession;
    }

    public int getLevel() {
        return this.characterProfession.getLevel();
    }

    public int getExperience() {
        return (int) this.characterProfession.getExperience();
    }

    protected void init() {

    }

    public void improve(int rate) {

    }
}

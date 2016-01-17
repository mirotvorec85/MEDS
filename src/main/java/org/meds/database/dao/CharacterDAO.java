package org.meds.database.dao;

import org.meds.database.entity.Character;
import org.meds.database.entity.CharacterInfo;

public interface CharacterDAO {

    Character findCharacter(String login);

    CharacterInfo getCharacterInfo(int id);

    /**
     * Saves a new character
     */
    void insert(Character character);

    /**
     * Updates the character record
     */
    void update(Character character);

    void insert(CharacterInfo info);

    void update(CharacterInfo info);
}

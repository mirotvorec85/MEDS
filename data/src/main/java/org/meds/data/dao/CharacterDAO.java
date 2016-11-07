package org.meds.data.dao;

import org.meds.data.domain.Character;
import org.meds.data.domain.CharacterInfo;

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

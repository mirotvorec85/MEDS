package org.meds.database.dao;

import org.meds.database.entity.Character;
import org.meds.database.entity.CharacterInfo;

public interface CharacterDAO {

    Character findCharacter(String login);

    Character getCharacterInfo(int id);

    void save(Character character);

    void save(CharacterInfo info);
}

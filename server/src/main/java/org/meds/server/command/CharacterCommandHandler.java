package org.meds.server.command;

import org.meds.data.dao.DAOFactory;
import org.meds.data.domain.Character;
import org.meds.data.domain.CharacterInfo;
import org.meds.data.domain.CharacterSpell;
import org.meds.enums.Races;
import org.meds.util.MD5Hasher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ServerCommand("character")
class CharacterCommandHandler implements CommandHandler {

    @Autowired
    private DAOFactory daoFactory;

    private Set<String> subCommands = new HashSet<>(Arrays.asList("ban", "create", "delete"));

    @Override
    public Set<String> getSubCommands() {
        return subCommands;
    }

    @Override
    public void handle(String[] args) {
        if ("create".equals(args[0])) {
            this.create(Arrays.copyOfRange(args, 1, args.length));
        }
    }

    private void create(String[] args) {
        if (args.length < 1) {
            System.out.println("Character create error: Missing character name.");
            return;
        }
        if (args.length < 2) {
            System.out.println("Character create error: Missing character password.");
            return;
        }

        String login = args[0].toLowerCase();
        String charName = java.lang.Character.toUpperCase(login.charAt(0)) + login.substring(1);

        Character character = new Character();
        character.setLogin(login);
        character.setPasswordHash(MD5Hasher.computePasswordHash(args[1]));

        daoFactory.getCharacterDAO().insert(character);
        int characterId = character.getId();

        CharacterInfo characterInfo = new CharacterInfo();
        characterInfo.setCharacterId(characterId);
        characterInfo.setName(charName);
        characterInfo.setAvatarId(22); // Elf
        characterInfo.setRace(Races.Elf.getValue());
        // Every base stat is 10
        characterInfo.setBaseCon(10);
        characterInfo.setBaseStr(10);
        characterInfo.setBaseDex(10);
        characterInfo.setBaseInt(10);
        // Location - Seastone Star
        characterInfo.setLocationId(3);
        characterInfo.setHomeId(3);
        // Notepad notes
        characterInfo.setNotepad("This is your notepad.");

        characterInfo.getSpells().put(38, new CharacterSpell(characterId, 38, 1)); // Examine
        characterInfo.getSpells().put(54, new CharacterSpell(characterId, 54, 1)); // First Aid
        characterInfo.getSpells().put(60, new CharacterSpell(characterId, 60, 1)); // Relax

        daoFactory.getCharacterDAO().insert(characterInfo);
        System.out.printf("Player %s has been created%n", charName);
    }
}

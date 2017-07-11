package org.meds.chat.commands;

import org.meds.Group;

@ChatCommand("tlrandom")
public class RandomTeamLootChatCommandHandler extends TeamLootChatCommandHandler {

    @Override
    public Group.TeamLootModes getMode() {
        return Group.TeamLootModes.Random;
    }
}

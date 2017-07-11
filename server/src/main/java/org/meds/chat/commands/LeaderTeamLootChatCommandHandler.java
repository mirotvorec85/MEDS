package org.meds.chat.commands;

import org.meds.Group;

@ChatCommand("tlleader")
public class LeaderTeamLootChatCommandHandler extends TeamLootChatCommandHandler {

    @Override
    public Group.TeamLootModes getMode() {
        return Group.TeamLootModes.Leader;
    }
}

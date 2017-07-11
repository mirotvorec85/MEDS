package org.meds.chat.commands;

import org.meds.Group;

@ChatCommand("tlffa")
public class RegularTeamLootChatCommandHandler extends TeamLootChatCommandHandler {

    @Override
    public Group.TeamLootModes getMode() {
        return Group.TeamLootModes.Regular;
    }
}

package org.meds.chat.commands;

import org.meds.Group;
import org.meds.Player;

public abstract class TeamLootChatCommandHandler extends AbstractChatCommandHandler {

    public abstract Group.TeamLootModes getMode();

    @Override
    public void handle(Player player, String[] args) {
        Group group = player.getGroup();
        if (group == null || group.getLeader() != player) {
            return;
        }

        group.setTeamLootMode(getMode());
        if (player.getSession() != null) {
            player.getSession().sendServerMessage(group.getTeamLootMode().getModeMessage())
                    .send(group.getTeamLootData());
        }
    }
}

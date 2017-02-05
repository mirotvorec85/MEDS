package org.meds.chat.commands;

import org.meds.Group;
import org.meds.Player;

public class TeamLootChatCommand extends AbstractChatCommand {

    private Group.TeamLootModes mode;

    public TeamLootChatCommand(Group.TeamLootModes mode) {
        this.mode = mode;
    }

    @Override
    public void handle(Player player, String[] args) {
        Group group = player.getGroup();
        if (group == null || group.getLeader() != player)
            return;

        group.setTeamLootMode(this.mode);
        if (player.getSession() != null) {
            player.getSession().sendServerMessage(group.getTeamLootMode().getModeMessage())
                    .send(group.getTeamLootData());
        }
    }
}

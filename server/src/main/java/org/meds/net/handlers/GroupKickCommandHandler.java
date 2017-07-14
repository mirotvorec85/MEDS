package org.meds.net.handlers;

import org.meds.Group;
import org.meds.Player;
import org.meds.World;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.GroupKick)
public class GroupKickCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;
    @Autowired
    private World world;

    @Override
    public int getMinDataLength() {
        return 1;
    }

    @Override
    public void handle(ClientCommandData data) {
        // The player is in a group and is a leader
        Group group = sessionContext.getPlayer().getGroup();
        if (group == null || group.getLeader() != sessionContext.getPlayer()) {
            return;
        }

        // Kicking target exists and is in the same group
        Player player = world.getPlayer(data.getInt(0));
        if (player == null || player.getGroup() != group) {
            return;
        }

        // Just leave as usual Quit
        player.leaveGroup();
    }
}

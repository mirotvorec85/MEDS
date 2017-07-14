package org.meds.net.handlers;

import org.meds.Group;
import org.meds.World;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.GroupChangeLeader)
public class GroupChangeLeaderCommandHandler extends CommonClientCommandHandler {

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

        group.setLeader(world.getPlayer(data.getInt(0)));
    }
}

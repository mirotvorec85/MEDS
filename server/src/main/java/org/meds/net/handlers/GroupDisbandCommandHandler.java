package org.meds.net.handlers;

import org.meds.Group;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.GroupDisband)
public class GroupDisbandCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;

    @Override
    public void handle(ClientCommandData data) {
        Group group = sessionContext.getPlayer().getGroup();

        if (group == null || group.getLeader() != sessionContext.getPlayer()) {
            return;
        }

        group.disband();
    }
}

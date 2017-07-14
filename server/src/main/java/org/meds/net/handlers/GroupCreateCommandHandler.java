package org.meds.net.handlers;

import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.GroupCreate)
public class GroupCreateCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;

    @Override
    public void handle(ClientCommandData data) {
        sessionContext.getPlayer().createGroup();
    }
}

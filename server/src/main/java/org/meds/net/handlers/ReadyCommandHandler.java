package org.meds.net.handlers;

import org.meds.World;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.Ready)
public class ReadyCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;
    @Autowired
    private World world;

    @Override
    public void handle(ClientCommandData data) {
        sessionContext.getPlayer().logIn(sessionContext.getSession());
        world.playerLoggedIn(sessionContext.getPlayer());
    }
}

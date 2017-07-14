package org.meds.net.handlers;

import org.meds.net.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.EnterStar)
public class EnterStarCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;

    @Override
    public void handle(ClientCommandData data) {
        ServerPacket startInfoPacket = new ServerPacket(ServerCommands.StarInfo)
                .add(sessionContext.getPlayer().getHome().getId())
                .add("0") // Corpse1 Location ID
                .add("0") // Corpse2 Location ID
                .add("0") // Corpse3 Location ID
                .add("") // ???
                .add("") // ???
                .add(""); // ???
        sessionContext.getSession().send(startInfoPacket);
    }
}

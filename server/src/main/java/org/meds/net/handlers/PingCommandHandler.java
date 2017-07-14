package org.meds.net.handlers;

import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.Ping)
public class PingCommandHandler extends CommonClientCommandHandler {

    @Override
    public void handle(ClientCommandData data) {
        // PING!:)
    }
}

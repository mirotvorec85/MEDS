package org.meds.net.handlers;

import org.meds.net.*;
import org.meds.server.Server;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman
 */
@ClientCommand(ClientCommandTypes.Verification)
public class VerificationCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;

    @Override
    public boolean isAuthenticatedOnly() {
        return false;
    }

    @Override
    public void handle(ClientCommandData data) {
        ServerPacket packet = new ServerPacket(ServerCommands.Version);
        // Checking Client version
        if (data.size() == 0) {
            packet.add(0);
        } else {
            int clientBuild = data.getInt(0);
            if (clientBuild < Server.Build || clientBuild > Server.MaxAllowedBuild) {
                packet.add(0);
            } else {
                packet.add(Server.Build).add(sessionContext.getSession().getKey());
            }
        }

        sessionContext.getSession().send(packet);
    }
}

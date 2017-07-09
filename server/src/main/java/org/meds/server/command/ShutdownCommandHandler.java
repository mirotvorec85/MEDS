package org.meds.server.command;

import org.meds.logging.Logging;
import org.meds.server.Server;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@ServerCommand("shutdown")
class ShutdownCommandHandler implements CommandHandler {

    @Autowired
    public Server server;

    @Override
    public Set<String> getSubCommands() {
        return null;
    }

    @Override
    public void handle(String[] args) {
        Logging.Info.log("Shutting down...");
        server.shutdown();
    }
}

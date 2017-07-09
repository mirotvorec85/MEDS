package org.meds.server.command;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@ServerCommand("help")
class HelpCommandHandler implements CommandHandler {

    @Autowired
    public ServerCommandWorker serverCommandWorker;

    @Override
    public Set<String> getSubCommands() {
        return null;
    }

    @Override
    public void handle(String[] args) {
        System.out.println("Available commands:");
        serverCommandWorker.getAvailableCommands().forEach(command -> {
            System.out.println(" " + command);
        });
    }
}

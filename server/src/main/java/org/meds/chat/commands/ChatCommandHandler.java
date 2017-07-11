package org.meds.chat.commands;

import org.meds.Player;

public interface ChatCommandHandler {

    int getMinArgsCount();

    void handle(Player player, String[] args);
}

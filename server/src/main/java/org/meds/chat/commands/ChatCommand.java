package org.meds.chat.commands;

import org.meds.Player;

public interface ChatCommand {

    int getMinArgsCount();

    void handle(Player player, String[] args);
}

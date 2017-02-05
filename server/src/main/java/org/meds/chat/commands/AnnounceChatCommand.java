package org.meds.chat.commands;

import org.meds.Player;
import org.meds.chat.ChatHandler;

public class AnnounceChatCommand extends AbstractChatCommand {

    @Override
    public int getMinArgsCount() {
        return 1;
    }

    @Override
    public void handle(Player player, String[] args) {
        ChatHandler.sendSystemMessage(args[0]);
    }
}

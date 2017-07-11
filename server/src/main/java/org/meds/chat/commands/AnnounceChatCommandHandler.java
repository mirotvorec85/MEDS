package org.meds.chat.commands;

import org.meds.Player;
import org.meds.chat.ChatHandler;
import org.springframework.beans.factory.annotation.Autowired;

@ChatCommand("announce")
public class AnnounceChatCommandHandler extends AbstractChatCommandHandler {

    @Autowired
    private ChatHandler chatHandler;

    @Override
    public int getMinArgsCount() {
        return 1;
    }

    @Override
    public void handle(Player player, String[] args) {
        chatHandler.sendSystemMessage(args[0]);
    }
}

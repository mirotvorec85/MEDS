package org.meds.chat.commands;

import org.meds.Player;
import org.meds.chat.ChatHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman
 */
@ChatCommand("locborn")
public class LocBornChatCommandHandler extends AbstractChatCommandHandler {

    @Autowired
    private ChatHandler chatHandler;

    @Override
    public void handle(Player player, String[] args) {
        if (player.getSession() == null)
            return;

        player.getSession().send(chatHandler.constructSystemMessage(player.getHome().getTitle()));
    }
}

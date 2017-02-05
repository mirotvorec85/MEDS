package org.meds.chat.commands;

import org.meds.Player;
import org.meds.chat.ChatHandler;

/**
 * Created by Romman on 31.01.2017.
 */
public class LocBornChatCommand extends AbstractChatCommand {

    @Override
    public void handle(Player player, String[] args) {
        if (player.getSession() == null)
            return;

        player.getSession().send(ChatHandler.constructSystemMessage(player.getHome().getTitle()));
    }
}

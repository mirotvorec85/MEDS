package org.meds.chat.commands;

import org.meds.Player;
import org.meds.chat.ChatHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@ChatCommand("who")
public class WhoChatCommandHandler extends AbstractChatCommandHandler {

    @Autowired
    private ChatHandler chatHandler;

    @Override
    public void handle(Player player, String[] args) {
        if (player.getSession() == null || player.getPosition() == null) {
            return;
        }

        player.getSession().send(chatHandler.constructSystemMessage(player.getPosition().getTitle()));

        List<Player> players = player.getPosition().getPlayers();
        for (Player pl : players) {
            player.getSession().send(chatHandler.constructSystemMessage(pl.getName()));
        }

        player.getSession().send(chatHandler.constructSystemMessage("Total: " + players.size()));
    }
}

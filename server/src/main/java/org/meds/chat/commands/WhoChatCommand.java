package org.meds.chat.commands;

import org.meds.Player;
import org.meds.chat.ChatHandler;

import java.util.List;

public class WhoChatCommand extends AbstractChatCommand {

    @Override
    public void handle(Player player, String[] args) {
        if (player.getSession() == null || player.getPosition() == null)
            return;

        player.getSession().send(ChatHandler.constructSystemMessage(player.getPosition().getTitle()));

        List<Player> players = player.getPosition().getPlayers();
        for (Player pl : players) {
            player.getSession().send(ChatHandler.constructSystemMessage(pl.getName()));
        }

        player.getSession().send(ChatHandler.constructSystemMessage("Total: " + players.size()));
    }
}

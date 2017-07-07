package org.meds.chat.commands;

import org.meds.Player;
import org.meds.server.Server;
import org.meds.util.DateFormatter;

import java.util.Date;

public class InfoChatCommand extends AbstractChatCommand {

    @Override
    public void handle(Player player, String[] args) {

        if (player.getSession() == null)
            return;

        player.getSession().sendServerMessage(1173, DateFormatter.format(new Date()))
                .sendServerMessage(1174, Server.getServerStartTime())
                .sendServerMessage(1175, player.getSession().getLastLoginDate())
                .sendServerMessage(1176, player.getSession().getLastLoginIp())
                .sendServerMessage(1177, player.getSession().getCurrentIp());
    }
}

package org.meds.net.handlers;

import org.meds.Locale;
import org.meds.database.LevelCost;
import org.meds.net.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.LearnGuildInfo)
public class LearnGuildInfoCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;
    @Autowired
    private Locale locale;

    @Override
    public void handle(ClientCommandData data) {
        ServerPacket packet = new ServerPacket(ServerCommands.LearnGuildInfo);
        packet.add("0");  // Always 0
        int availableCount = sessionContext.getPlayer().getLevel() - sessionContext.getPlayer().getGuildLevel();
        packet.add(availableCount); // Available levels

        // Positive - free available lessons count
        // Negative - total learned lessons
        packet.add(-sessionContext.getPlayer().getGuildLevel());
        packet.add(availableCount);
        // Next lesson gold
        packet.add(LevelCost.getGold(sessionContext.getPlayer().getGuildLevel() + 1));
        // Gold for all available lessons
        packet.add(LevelCost.getTotalGold(sessionContext.getPlayer().getGuildLevel() + 1, sessionContext.getPlayer().getLevel()));

        // Lessons reset cost
        packet.add(locale.getString(3));

        // ??? Maybe next reset cost?
        packet.add("+100500 gold");

        sessionContext.getSession().send(packet);
    }
}

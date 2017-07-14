package org.meds.net.handlers;

import org.meds.Player;
import org.meds.Trade;
import org.meds.World;
import org.meds.logging.Logging;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.GetTrade)
public class GetTradeCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;
    @Autowired
    private World world;

    @Override
    public int getMinDataLength() {
        return 1;
    }

    @Override
    public void handle(ClientCommandData data) {
        // Try to find the trader
        Player trader = world.getPlayer(data.getInt(0));
        if (trader == null) {
            return;
        }

        Player player = sessionContext.getPlayer();

        // Create a new trade
        if (player.getTrade() == null) {
            // The trader is trading already
            if (trader.getTrade() != null) {
                // The other side of the trade is this player
                if (trader.getTrade().getOtherSide().getPlayer() == player) {
                    Logging.Warn.log(toString() + " has not trade, but " + trader.toString() + " has a trade" +
                            "where the other side is the current player.");
                    // Send the existing trade data
                    player.setTrade(trader.getTrade().getOtherSide());
                    player.getTrade().sendTradeData();
                } else {
                    // TODO: Determine what to do in this situation
                    return;
                }
            } else {
                new Trade(player, trader);
            }
        } else {
            // Send the existing trade data
            player.getTrade().sendTradeData();
        }
    }
}

package org.meds.net.handlers;

import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.TradeCancel)
public class TradeCancelCommandHandler extends CommonClientCommandHandler {

    private SessionContext context;

    @Override
    public void handle(ClientCommandData data) {
        if (context.getPlayer().getTrade() == null) {
            return;
        }
        context.getPlayer().getTrade().cancel();
    }
}

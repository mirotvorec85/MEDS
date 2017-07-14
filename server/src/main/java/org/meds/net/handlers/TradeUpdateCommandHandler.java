package org.meds.net.handlers;

import org.meds.net.ClientCommandTypes;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.TradeUpdate)
public class TradeUpdateCommandHandler extends TradeCommandHandler {

    @Override
    public int getMinDataLength() {
        return 15;
    }

    @Override
    public boolean isApply() {
        return false;
    }
}

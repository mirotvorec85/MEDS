package org.meds.net.handlers;

import org.meds.net.ClientCommandTypes;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.TradeApply)
public class TradeApplyCommandHandler extends TradeCommandHandler {

    @Override
    public int getMinDataLength() {
        return 29;
    }

    @Override
    public boolean isApply() {
        return true;
    }
}

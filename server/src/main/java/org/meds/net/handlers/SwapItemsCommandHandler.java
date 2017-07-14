package org.meds.net.handlers;

import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.SwapItem)
public class SwapItemsCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;

    @Override
    public int getMinDataLength() {
        return 3;
    }

    @Override
    public void handle(ClientCommandData data) {
        int slot1 = data.getInt(0);
        int slot2 = data.getInt(1);
        int count = data.getInt(2);

        sessionContext.getPlayer().getInventory().swapItem(slot1, slot2, count);
    }
}

package org.meds.net.handlers;

import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.DestroyItem)
public class DestroyItemCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;

    @Override
    public int getMinDataLength() {
        return 2;
    }

    @Override
    public void handle(ClientCommandData data) {
        int slotId = data.getInt(0, -1);
        int count = data.getInt(1);

        sessionContext.getPlayer().getInventory().destroyItem(slotId, count);
    }
}

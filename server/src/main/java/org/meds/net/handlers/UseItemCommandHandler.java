package org.meds.net.handlers;

import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.UseItem)
public class UseItemCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;

    @Override
    public int getMinDataLength() {
        return 3;
    }

    @Override
    public void handle(ClientCommandData data) {
        int slotId = data.getInt(0, -1);
        // TODO: assign unknown second and third values
        if (slotId == -1) {
            return;
        }
        sessionContext.getPlayer().getInventory().useItem(slotId);
    }
}

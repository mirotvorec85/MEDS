package org.meds.net.handlers;

import org.meds.item.ItemPrototype;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.InnGet)
public class InnGetCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;

    @Override
    public int getMinDataLength() {
        return 4;
    }

    @Override
    public void handle(ClientCommandData data) {
        ItemPrototype prototype = new ItemPrototype(data.getInt(0), data.getInt(1, -1), data.getInt(2, -1));
        int count = data.getInt(3, -1);

        if (prototype.getTemplateId() == 0 || count == -1) {
            return;
        }
        sessionContext.getPlayer().getInn().tryTakeItem(prototype, count);
    }
}

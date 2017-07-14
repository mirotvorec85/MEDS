package org.meds.net.handlers;

import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.UseMagic)
public class UseMagicCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;

    @Override
    public int getMinDataLength() {
        return 2;
    }

    @Override
    public void handle(ClientCommandData data) {
        int spellId = data.getInt(0);
        int targetId = data.getInt(1);
        sessionContext.getPlayer().useMagic(spellId, targetId);
    }
}

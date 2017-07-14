package org.meds.net.handlers;

import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.Relax)
public class RelaxCommandHandler extends CommonClientCommandHandler {

    private static final int RELAX_SPELL_ID = 60;

    @Autowired
    private SessionContext sessionContext;

    @Override
    public void handle(ClientCommandData data) {
        /*
         * 0 - "0" ???
         */
        sessionContext.getPlayer().castSpell(RELAX_SPELL_ID);
    }
}

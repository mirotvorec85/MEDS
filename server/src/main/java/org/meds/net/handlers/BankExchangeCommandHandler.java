package org.meds.net.handlers;

import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.BankExchange)
public class BankExchangeCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext context;

    @Override
    public int getMinDataLength() {
        return 1;
    }

    @Override
    public void handle(ClientCommandData data) {
        // TODO: implement Player.bankExchange
        //context.getPlayer().bankExchange(data.getInt(0);

        //context.getSession().send(context.getPlayer().getCurrencyData());
    }
}

package org.meds.net.handlers;

import org.meds.enums.Currencies;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.PutMoney)
public class PutMoneyCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;

    @Override
    public void handle(ClientCommandData data) {
        int amount = -1;
        if (data.size() > 0) {
            amount = data.getInt(0, -1);
        }

        // Absent amount means to deposit all the gold
        if (amount == -1) {
            amount = sessionContext.getPlayer().getCurrencyAmount(Currencies.Gold);
        }

        sessionContext.getPlayer().depositMoney(amount);
    }
}

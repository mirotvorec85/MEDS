package org.meds.net.handlers;

import org.meds.enums.PlayerSettings;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.SetAutoLoot)
public class SetAutoLootCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;

    @Override
    public int getMinDataLength() {
        return 1;
    }

    @Override
    public void handle(ClientCommandData data) {
        int status = data.getInt(0);
        if (status == 1) {
            sessionContext.getPlayer().getSettings().set(PlayerSettings.AutoLoot);
        } else {
            sessionContext.getPlayer().getSettings().unset(PlayerSettings.AutoLoot);
        }
    }
}

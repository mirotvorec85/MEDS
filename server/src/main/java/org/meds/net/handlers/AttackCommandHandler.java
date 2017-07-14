package org.meds.net.handlers;

import org.meds.Unit;
import org.meds.World;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.Attack)
public class AttackCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;
    @Autowired
    private World world;

    @Override
    public int getMinDataLength() {
        return 1;
    }

    @Override
    public void handle(ClientCommandData data) {
        /*
         * 0 - Attack type ("udar", "run")
         * 1 - victim ID
         */
        if ("run".equals(data.getString(0))) {
            sessionContext.getPlayer().runAway();
        } else if ("udar".equals(data.getString(0))) {
            if (data.size() < 2)
                return;

            int victimId = data.getInt(1);
            // TODO: Find out why
            // HACK: remove last symbol of the victim id
            victimId = victimId / 10;
            Unit victim = world.getUnit(victimId);
            // target is not found
            if (victim == null) {
                return;
            }

            sessionContext.getPlayer().interact(victim);
        }
    }
}

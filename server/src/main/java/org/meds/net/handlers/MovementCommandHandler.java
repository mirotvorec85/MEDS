package org.meds.net.handlers;

import org.meds.enums.MovementDirections;
import org.meds.map.MapManager;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.Movement)
public class MovementCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;
    @Autowired
    private MapManager mapManager;

    @Override
    public int getMinDataLength() {
        return 1;
    }

    @Override
    public void handle(ClientCommandData data) {
        // Battle does not allow movement
        if (sessionContext.getPlayer().isInCombat())
            return;

        MovementDirections direction = MovementDirections.parse(data.getInt(0, -1));
        if (direction == null) {
            return;
        }

        mapManager.registerMovement(sessionContext.getPlayer(), direction);
    }
}

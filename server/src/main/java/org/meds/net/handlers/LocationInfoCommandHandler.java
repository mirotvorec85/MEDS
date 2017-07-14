package org.meds.net.handlers;

import org.meds.map.Location;
import org.meds.map.MapManager;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.GetLocationInfo)
public class LocationInfoCommandHandler extends CommonClientCommandHandler {

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
        Location location = mapManager.getLocation(data.getInt(0));
        if (location != null) {
            sessionContext.getSession().send(location.getInfoData());
        }
    }
}

package org.meds.net.handlers;

import org.meds.map.MapManager;
import org.meds.map.Region;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.RegionLocations)
public class RegionLocationsCommandHandler extends CommonClientCommandHandler {

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
        Region region = mapManager.getRegion(data.getInt(0));
        if (region != null) {
            sessionContext.getSession().send(region.getLocationListData());
        }
    }
}

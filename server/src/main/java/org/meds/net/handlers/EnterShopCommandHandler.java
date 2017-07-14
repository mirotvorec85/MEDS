package org.meds.net.handlers;

import org.meds.enums.SpecialLocationTypes;
import org.meds.map.Location;
import org.meds.map.MapManager;
import org.meds.map.Shop;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.EnterShop)
public class EnterShopCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;
    @Autowired
    private MapManager mapManager;

    @Override
    public void handle(ClientCommandData data) {
        Location location = sessionContext.getPlayer().getPosition();
        if (location.getSpecialLocationType() == SpecialLocationTypes.Generic) {
            return;
        }

        Shop shop = mapManager.getShop(location.getSpecialLocationId());
        if (shop == null) {
            return;
        }

        sessionContext.getSession().send(shop.getData());
    }
}

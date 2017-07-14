package org.meds.net.handlers;

import org.meds.enums.SpecialLocationTypes;
import org.meds.item.ItemPrototype;
import org.meds.map.MapManager;
import org.meds.map.Shop;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.BuyItem)
public class BuyItemCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;
    @Autowired
    private MapManager mapManager;

    @Override
    public int getMinDataLength() {
        return 5;
    }

    @Override
    public void handle(ClientCommandData data) {
        // Player must be at shop
        if (sessionContext.getPlayer().getPosition().getSpecialLocationType() == SpecialLocationTypes.Generic) {
            return;
        }
        Shop shop = mapManager.getShop(sessionContext.getPlayer().getPosition().getSpecialLocationId());
        // There is no shop with this id
        if (shop == null) {
            return;
        }

        ItemPrototype prototype = new ItemPrototype(
                data.getInt(0),
                data.getInt(1, -1),
                data.getInt(2, -1));
        int count = data.getInt(3, -1);
        // int unk5 = data.getInt(4); // Always 0
        if (prototype.getTemplateId() == 0 || count == -1) {
            return;
        }

        if (shop.sellItem(sessionContext.getPlayer(), prototype, count)) {
            sessionContext.getSession().send(shop.getData());
        }
    }
}

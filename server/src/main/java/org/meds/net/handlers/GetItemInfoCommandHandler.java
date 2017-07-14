package org.meds.net.handlers;

import org.meds.item.ItemInfoPacketFactory;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.ServerPacket;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.GetItemInfo)
public class GetItemInfoCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;
    @Autowired
    private ItemInfoPacketFactory itemInfoPacketFactory;

    @Override
    public void handle(ClientCommandData data) {
        ServerPacket packet = new ServerPacket();
        int templateId;
        int modification;
        for (int i = 1; i < data.size(); i += 2) {
            templateId = data.getInt(i - 1);
            modification = data.getInt(i);
            itemInfoPacketFactory.create(templateId, modification, packet);
        }
        sessionContext.getSession().send(packet);
    }
}

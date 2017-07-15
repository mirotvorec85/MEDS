package org.meds.item;

import org.meds.net.ServerCommands;
import org.meds.net.ServerPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Romman
 */
@Component
public class ItemInfoPacketFactory {

    @Autowired
    private ItemFactory itemFactory;
    @Autowired
    private ItemTitleConstructor itemTitleConstructor;

    /**
     * Gets the ServerPacket object that contains item info based on the specified template and modification data
     *
     * @param templateId
     * @param modification
     * @param packet       a packet object to which the data will be appended
     * @return
     */
    public ServerPacket create(int templateId, int modification, ServerPacket packet) {
        ItemPrototype prototype = new ItemPrototype(templateId, modification, 0);
        Item item = itemFactory.create(prototype);
        if (item == null) {
            return packet;
        }

        packet.add(ServerCommands.ItemInfo)
                .add(item.getTemplate().getId())
                .add(item.getModification())
                .add(itemTitleConstructor.getTitleAndDescription(item))
                .add(item.getTemplate().getImageId())
                .add(item.getTemplate().getItemClass())
                .add(item.getTemplate().getLevel())
                .add(item.getTemplate().getCost())
                .add(item.getTemplate().getCurrencyId())
                .add("1187244746") // Image (or even Item itself) date
                .add(ItemUtils.getMaxDurability(item.getTemplate()))
                .add(ItemUtils.getWeight(item.getTemplate()));

        for (Map.Entry<ItemBonusParameters, Integer> entry : item.getBonusParameters().entrySet()) {
            packet.add(entry.getKey());
            packet.add(entry.getValue());
        }
        return packet;

    }
}

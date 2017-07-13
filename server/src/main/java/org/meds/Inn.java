package org.meds;

import org.meds.data.domain.CharacterInnItem;
import org.meds.enums.InnFilters;
import org.meds.item.Item;
import org.meds.item.ItemFactory;
import org.meds.item.ItemPrototype;
import org.meds.net.ServerCommands;
import org.meds.net.ServerPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("prototype")
public class Inn {

    @Autowired
    private ItemFactory itemFactory;

    /**
     * How much different slots a Player can use.
     */
    private int slotCapacity;
    /**
     * Maximum count of items from all the item stacks in all slots.
     */
    private int countCapacity;

    /**
     * Current count of stored items.
     */
    private int count;

    private Player owner;

    private Map<ItemPrototype, CharacterInnItem> items;

    public Inn() {
        this.count = 0;
        this.items = new HashMap<>();
        this.slotCapacity = 100;
        this.countCapacity = 1000;
    }

    public void setOwner(Player player) {
        this.owner = player;
    }

    public void load(Map<ItemPrototype, CharacterInnItem> items) {
        this.items = items;
    }

    public void save() {
        // Not needed
        // All the items are already in the Character entity object.

        // TODO: maybe some check
        // empty items or something
    }

    public boolean tryStoreItem(ItemPrototype prototype, int count) {
        // TODO: check the inn capacity and correct an item storing count

        Item item;
        if ((item = this.owner.getInventory().takeItem(prototype, count)) == null) {
            return false;
        }

        CharacterInnItem innItem = this.items.get(prototype);
        if (innItem == null) {
            innItem = new CharacterInnItem(this.owner.getId(), prototype);
            this.items.put(prototype, innItem);
        }
        innItem.setCount(innItem.getCount() + item.getCount());

        onInnChanged();
        return true;
    }

    public boolean tryTakeItem(ItemPrototype prototype, int count) {
        // TODO: Inn capacity tracking
        CharacterInnItem innItem = this.items.get(prototype);
        if (innItem == null) {
            return false;
        }

        Item item = itemFactory.create(prototype, count);

        // Item is found but an inventory can not store anymore.
        if (!this.owner.getInventory().tryStoreItem(item, count)) {
            return false;
        }

        if (item.getCount() == 0) {
            this.items.remove(prototype);
        } else {
            innItem.setCount(item.getCount());
        }

        onInnChanged();
        return true;
    }

    public ServerPacket getInnData() {
        return this.getInnData(InnFilters.Disabled);
    }

    public ServerPacket getInnData(InnFilters filter) {
        ServerPacket packet = new ServerPacket(ServerCommands.Inn);
        packet.add(this.items.size())
            .add(this.slotCapacity)
            .add(this.count)
            .add(this.countCapacity);

        for (CharacterInnItem innItem : this.items.values()) {
            packet.add(innItem.getItemTemplateId())
                .add(innItem.getModification())
                .add(innItem.getDurability())
                .add(innItem.getCount());
        }

        return packet;
    }

    private void onInnChanged() {
        if (this.owner.getSession() != null)
            this.owner.getSession().send(getInnData());
    }
}

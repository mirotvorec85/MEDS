package org.meds;

import java.util.HashMap;
import java.util.Map;

import org.meds.Item.Prototype;
import org.meds.database.entity.CharacterInnItem;
import org.meds.enums.InnFilters;
import org.meds.net.ServerOpcodes;
import org.meds.net.ServerPacket;

public class Inn
{
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

    private Map<Prototype, CharacterInnItem> items;

    public Inn(Player owner)
    {
        this.owner = owner;
        this.count = 0;
        this.items = new HashMap<>();
        this.slotCapacity = 100;
        this.countCapacity = 1000;
    }

    public void load(Map<Prototype, CharacterInnItem> items)
    {
        this.items = items;
    }

    public void save()
    {
        // Not needed
        // All the items are already in the Character entity object.

        // TODO: maybe some check
        // empty items or something
    }

    public boolean tryStoreItem(Prototype proto, int count)
    {
        // TODO: check the inn capacity and correct an item storing count

        Item item;
        if ((item = this.owner.getInventory().takeItem(proto, count)) == null)
            return false;

        CharacterInnItem innItem = this.items.get(proto);
        if (innItem == null)
        {
            innItem = new CharacterInnItem(this.owner.getGuid(), proto);
            this.items.put(proto, innItem);
        }
        innItem.setCount(innItem.getCount() + item.getCount());

        onInnChanged();
        return true;
    }

    public boolean tryTakeItem(Prototype proto, int count)
    {
        // TODO: Inn capacity traking
        CharacterInnItem innItem = this.items.get(proto);
        if (innItem == null)
            return false;

        Item item = new Item(proto, count);

        // Item is found but an inventory can not store anymore.
        if (!this.owner.getInventory().tryStoreItem(item, count))
            return false;

        if (item.getCount() == 0)
            this.items.remove(proto);
        else
            innItem.setCount(item.getCount());

        onInnChanged();
        return true;
    }

    public ServerPacket getInnData()
    {
        return this.getInnData(InnFilters.Disabled);
    }

    public ServerPacket getInnData(InnFilters filter)
    {
        ServerPacket packet = new ServerPacket(ServerOpcodes.Inn);
        packet.add(this.items.size())
            .add(this.slotCapacity)
            .add(this.count)
            .add(this.countCapacity);

        for (CharacterInnItem innItem : this.items.values())
        {
            packet.add(innItem.getItemTemplateId())
                .add(innItem.getModification())
                .add(innItem.getDurability())
                .add(innItem.getCount());
        }

        return packet;
    }

    private void onInnChanged()
    {
        if (this.owner.getSession() != null)
            this.owner.getSession().addData(getInnData());
    }
}

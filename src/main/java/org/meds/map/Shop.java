package org.meds.map;

import java.util.Map;
import java.util.Set;

import org.meds.Item;
import org.meds.Item.Prototype;
import org.meds.Player;
import org.meds.database.DBStorage;
import org.meds.database.entity.ItemTemplate;
import org.meds.database.entity.ShopItem;
import org.meds.enums.ItemClasses;
import org.meds.enums.ShopTypes;
import org.meds.logging.Logging;
import org.meds.net.ServerCommands;
import org.meds.net.ServerPacket;

public class Shop
{
    private int id;
    private ShopTypes type;
    private int currencyId;
    private Set<ShopItem> shopItems;

    private java.util.Map<ItemTemplate, Integer> items = new java.util.HashMap<>();

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public int getTypeInteger()
    {
        return type.getValue();
    }
    public void setTypeInteger(int type)
    {
        this.type = ShopTypes.parse(type);
    }
    public ShopTypes getType()
    {
        return this.type;
    }
    public int getCurrencyId()
    {
        return currencyId;
    }
    public void setCurrencyId(int currencyId)
    {
        this.currencyId = currencyId;
    }

    public void setItems(Set<ShopItem> items)
    {
        this.shopItems = items;
    }

    public void load()
    {
        for (ShopItem item : this.shopItems)
        {
            ItemTemplate template = DBStorage.ItemTemplateStore.get(item.getItemTemplateId());
            if (template == null)
            {
                Logging.Warn.log("Shop " + this.id + " has item with template=" + item.getItemTemplateId() + " which does not exist. Skipped.");
                continue;
            }
            this.items.put(template, item.getCount());
        }
        Logging.Info.log("Shop " + this.id + ": loaded with " + this.items.size() + " items.");
    }

    public Set<ShopItem> getItems()
    {
        return null;
    }

    public ServerPacket getData()
    {
        ServerPacket packet = new ServerPacket(ServerCommands.ShopInfo);
        packet.add(this.items.size());
        packet.add(this.type);
        packet.add(0);
        for (Map.Entry<ItemTemplate, Integer> entry : this.items.entrySet())
        {
            packet.add(entry.getKey().getId())
                .add("0") // Modification (Standard shops have only default items
                .add(Item.getMaxDurability(entry.getKey()));
            // -1 in DB mean an infinite count
            // But for the client this number is 999,999
            if (entry.getValue() == -1)
                packet.add("999999");
            else
                packet.add(entry.getValue());
            packet.add(entry.getKey().getCost());
            packet.add(0);
        }

        return packet;
    }

    /**
     * The shop buys items from a player.
     * @param seller The Player seller instance
     * @param prototype Item prototype for sale
     * @param count Count of items to buy
     * @return A boolean value, indicating whether the transaction was completed
     */
    public boolean buyItem(Player seller, Prototype prototype, int count)
    {
        if (!this.isAppropriateItem(prototype))
            return false;

        Item item = seller.getInventory().takeItem(prototype, count);
        if (item == null)
            return false;

        // Add money
        // Cost is 20% less than the item cost
        seller.changeCurrency(this.currencyId, (int)(item.Template.getCost() * 0.8) * item.getCount());

        return true;
    }

    /**
     * The shop sells items to a player
     * @param buyer A Player buyer instance.
     * @param prototype Item Prototype to sell
     * @param count How much items to sell
     * @return A boolean value indicating whether the transaction was completed.
     */
    public boolean sellItem(Player buyer, Prototype prototype, int count)
    {
        ItemTemplate template = DBStorage.ItemTemplateStore.get(prototype.getTemplateId());
        if (template == null)
            return false;

        Integer shopItemCount = this.items.get(template);
        // Shop does not have the item
        if (shopItemCount == null)
            return false;

        // Correct count by source count
        if (count > shopItemCount && shopItemCount != -1)
            count = shopItemCount;

        // Correct count by payable ability
        if (buyer.getCurrencyAmount(this.currencyId) < template.getCost() * count)
            count = buyer.getCurrencyAmount(this.currencyId) / template.getCost();

        Item item = new Item(template, count);
        if (!buyer.getInventory().tryStoreItem(item))
            return false;

        // Real stored count
        count = count - item.getCount();
        buyer.changeCurrency(this.currencyId, - template.getCost() * count);

        // Subtract the bought count if not an infinite item stack
        if (shopItemCount != -1)
        {
            shopItemCount -= count;
            // The last items
            if (shopItemCount == 0)
                // Remove from the shop
                this.items.remove(template);
            else
                this.items.put(template, shopItemCount);
        }

        return true;
    }

    public boolean isAppropriateItem(Prototype prototype)
    {
        return this.isAppropriateItem(DBStorage.ItemTemplateStore.get(prototype.getTemplateId()));
    }

    public boolean isAppropriateItem(Item item)
    {
        return this.isAppropriateItem(item.Template);
    }

    public boolean isAppropriateItem(ItemTemplate template)
    {
        if (template == null)
            return false;

        ItemClasses itemClass = template.getItemClass();

        switch (this.type)
        {
            case AlchemicalShop:
                return itemClass == ItemClasses.Usable || itemClass == ItemClasses.Gemm;
            case ArmourShop:
                return itemClass == ItemClasses.Hands || itemClass == ItemClasses.Back ||
                    itemClass == ItemClasses.Waist || itemClass == ItemClasses.Legs ||
                    itemClass == ItemClasses.Head || itemClass == ItemClasses.Body ||
                    itemClass == ItemClasses.Shield || itemClass == ItemClasses.Foot ||
                    itemClass == ItemClasses.Ring || itemClass == ItemClasses.Neck ||
                    itemClass == ItemClasses.Weapon;
            case Dump:
                return itemClass == ItemClasses.Component;
            default:
                return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Shop shop = (Shop) o;

        return this.id == shop.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}

package meds;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import meds.database.DBStorage;
import meds.database.entity.ItemTemplate;
import meds.enums.ItemBonusParameters;
import meds.enums.ItemEssenceLevels;
import meds.enums.ItemEssenceTypes;
import meds.enums.ItemReinforcementTypes;
import meds.enums.ItemTotemicTypes;
import meds.util.Valued;

public class Item
{
    public static class Prototype implements Serializable, Cloneable
    {
        private static final long serialVersionUID = -2685946807316472347L;
        private int templateId;
        private int modification;
        private int durability;

        public Prototype()
        {

        }

        public Prototype(int templateId, int modification, int durability)
        {
            this.templateId = templateId;
            this.modification = modification;
            this.durability = durability;
        }

        public int getTemplateId()
        {
            return templateId;
        }
        public void setTemplateId(int templateId)
        {
            this.templateId = templateId;
        }
        public int getModification()
        {
            return modification;
        }
        public void setModification(int modification)
        {
            this.modification = modification;
        }
        public int getDurability()
        {
            return durability;
        }
        public void setDurability(int durability)
        {
            this.durability = durability;
        }

        @Override
        public int hashCode()
        {
            return (this.templateId * 1000 + this.durability) | this.modification;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == null)
                return false;
            if (!(obj instanceof Prototype))
                return false;
            Prototype cObj = (Prototype)obj;

            return this.templateId == cObj.templateId &&
                    this.modification == cObj.modification &&
                    this.durability == cObj.durability;
        }
    }

    public static class Modification implements Valued
    {
        private int value;

        private ItemTotemicTypes totem;
        private ItemEssenceLevels essenceLevel;
        private ItemEssenceTypes essenceType;
        private ItemReinforcementTypes reinforcement;

        public Modification(int value)
        {
            this.value = value;
            this.totem = ItemTotemicTypes.parse(value & 0xFF);
            this.essenceLevel = ItemEssenceLevels.parse(value & 0xFF00);
            this.essenceType = ItemEssenceTypes.parse(value & 0xFF0000);
            this.reinforcement = ItemReinforcementTypes.parse(value & 0xFF000000);
        }

        public ItemTotemicTypes getTotem()
        {
            return totem;
        }
        public void setTotem(ItemTotemicTypes totem)
        {
            this.totem = totem;
            recalculateValue();
        }
        public ItemEssenceLevels getEssenceLevel()
        {
            return essenceLevel;
        }
        public void setEssenceLevel(ItemEssenceLevels essenceLevel)
        {
            this.essenceLevel = essenceLevel;
            recalculateValue();
        }
        public ItemEssenceTypes getEssenceType()
        {
            return essenceType;
        }
        public void setEssenceType(ItemEssenceTypes essenceType)
        {
            this.essenceType = essenceType;
            recalculateValue();
        }
        public ItemReinforcementTypes getReinforcement()
        {
            return reinforcement;
        }
        public void setReinforcement(ItemReinforcementTypes reinforcement)
        {
            this.reinforcement = reinforcement;
            recalculateValue();
        }

        private void recalculateValue()
        {
            this.value = (this.totem.getValue() << 24) +
                    (this.essenceLevel.getValue() << 16) +
                    (this.essenceType.getValue() << 8) +
                    this.reinforcement.getValue();
        }

        @Override
        public int getValue()
        {
            return this.value;
        }

        @Override
        public String toString()
        {
            return Integer.toString(this.value);
        }
    }

    public static int getMaxDurability(int templateId)
    {
        ItemTemplate template = DBStorage.ItemTemplateStore.get(templateId);
        if (template == null)
            return 0;
        return Item.getMaxDurability(template);
    }

    public static int getMaxDurability(ItemTemplate template)
    {
        if (template.getLevel() == 0)
            return 30;

        return template.getLevel() * 75 + 75;
    }

    public static int getWeight(int templateId)
    {
        return Item.getWeight(DBStorage.ItemTemplateStore.get(templateId));
    }

    public static int getWeight(ItemTemplate template)
    {
        if (template == null)
            return 0;

        switch (template.getItemClass())
        {
            case Ring:
                return 2;

            case Neck:
            case Hands:
            case Waist:
            case Foot:
                return 3;

            case Back:
            case Legs:
            case Component:
                return 4;

            case Head:
                return 5;

            case Shield:
                return 8;

            case Body:
            case Weapon:
                return 10;

            default:
                return 1;
        }
    }

    public final ItemTemplate Template;

    public final int MaxDurability;

    public final int Weight;

    private int count;

    private int durability;

    private Modification modification;

    private Map<ItemBonusParameters, Integer> bonusParameters;

    public Item(ItemTemplate template)
    {
        this.Template = template;
        this.modification = new Modification(0);
        this.MaxDurability = this.durability = Item.getMaxDurability(template);
        this.Weight = Item.getWeight(template);
        this.bonusParameters = new HashMap<ItemBonusParameters, Integer>();
        if (this.Template == null)
            this.count = 0;
        else
            this.count = 1;
    }

    public Item(ItemTemplate template, int count)
    {
        this(template);
        this.count = count;
    }

    public Item(int templateId)
    {
        this(DBStorage.ItemTemplateStore.get(templateId));
    }

    public Item(int templateId, int count)
    {
        this(DBStorage.ItemTemplateStore.get(templateId));
        this.count = count;
    }

    public Item(Prototype proto)
    {
        this(proto.getTemplateId());
        this.modification = new Modification(proto.getModification());
        this.durability = proto.getDurability();
    }

    public Item(Prototype proto, int count)
    {
        this(proto);
        this.count = count;
    }

    public int getCount()
    {
        return this.count;
    }

    public Prototype getPrototype()
    {
        return new Prototype(this.Template.getId(), this.modification.value, this.durability);
    }

    public boolean isEquipment()
    {
        if (this.Template == null)
            return false;
        switch (this.Template.getItemClass())
        {
            case Head:
            case Neck:
            case Back:
            case Hands:
            case Body:
            case Ring:
            case Weapon:
            case Shield:
            case Waist:
            case Legs:
            case Foot:
                return true;
            default:
                return false;
        }
    }

    public Modification getModification()
    {
        return this.modification;
    }

    /**
     * Gets the item current durability value.
     */
    public int getDurability()
    {
        return this.durability;
    }

    /**
     * Separates the item into two parts, returns the one that has the specified count
     * and applies the other part to this Item object.
     * @param count How much items to extract from this item stack.
     * @return New Item Object with the specified count.
     */
    public Item unstackItem(int count)
    {
        if (count < 1)
            return null;

        if (count > this.count)
            count = this.count;
        Item newItem = this.clone(count);
        this.count -= count;
        return newItem;
    }

    /**
     * Try to stack this item with a whole stack of another item.
     */
    public boolean tryStackItem(Item item)
    {
        return this.tryStackItem(item, item.count);
    }

    /**
     * Try to stack this item with another item with the specified count.
     */
    public boolean tryStackItem(Item item, int count)
    {
        if (!this.isStackableWith(item))
            return false;
        if (count > item.count)
            count = item.count;

        this.count += count;
        item.count -= count;
        return true;
    }

    public boolean isStackableWith(Item item)
    {
        // Equipment may not be stacked
        if (this.isEquipment())
            return false;

        // Only items with the same prototypes can be stacked
        return this.equals(item);
    }

    private void addBonusValue(ItemBonusParameters parameter, int value)
    {
        Integer oldValue = this.bonusParameters.get(parameter);
        if (oldValue == null)
            oldValue = 0;
        this.bonusParameters.put(parameter, oldValue + value);
    }

    public int getBonusValue(ItemBonusParameters parameter)
    {
        Integer value = this.bonusParameters.get(parameter);
        if (value == null)
            value = 0;
        return value;
    }

    public ServerPacket getPacketData()
    {
        if (this.Template == null)
            return null;

        ServerPacket packet = new ServerPacket(ServerOpcodes.ItemInfo);
        packet.add(this.Template.getId())
            .add(this.modification.getValue());

        if (this.Template.getDescription().isEmpty())
            packet.add(this.Template.getTitle());
        else
            packet.add(this.Template.getTitle() + "\r\n" + this.Template.getDescription());

        packet.add(this.Template.getImageId())
            .add(this.Template.getItemClass())
            .add(this.Template.getLevel())
            .add(this.Template.getCost())
            .add(this.Template.getCurrencyId())
            .add("1187244746") // Image (or even Item itself) date
            .add(this.durability)
            .add(this.Weight);

        for (Map.Entry<ItemBonusParameters, Integer> entry : this.bonusParameters.entrySet())
            packet.add(entry.getKey()).add(entry.getValue());

        return packet;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (!(obj instanceof Item))
            return false;

        return this.equals((Item)obj);
    }

    public boolean equals(Item item)
    {
        return equals(item.getPrototype());
    }

    public boolean equals(Prototype proto)
    {
        return proto.equals(this.getPrototype());
    }

    @Override
    public int hashCode()
    {
        return this.getPrototype().hashCode();
    }

    /**
     * Gets a complete copy of this Item class instance.
     */
    @Override
    protected Item clone()
    {
        return this.clone(this.count);
    }

    /**
     * Gets a complete copy of this Item class instance but with the specified count.
     * @param count Item count of a copy.
     * @return New Item as a clone of this Item
     */
    protected Item clone(int count)
    {
        // TODO: Clone item by initialize default item and assign all the field.
        // not doing recalculating these fields values (too efficient)
        Item newItem = new Item(this.Template);
        newItem.count = this.count;
        newItem.bonusParameters = this.bonusParameters;
        newItem.durability = this.durability;
        newItem.modification = this.modification;
        return newItem;
    }

    public void use(Player user)
    {
        // TODO: implement
    }
}

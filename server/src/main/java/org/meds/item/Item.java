package org.meds.item;

import java.util.HashMap;
import java.util.Map;

import org.meds.Locale;
import org.meds.Player;
import org.meds.data.domain.ItemTemplate;
import org.meds.database.DataStorage;
import org.meds.enums.*;
import org.meds.logging.Logging;
import org.meds.net.ServerCommands;
import org.meds.net.ServerPacket;
import org.meds.spell.Spell;
import org.meds.util.EnumFlags;

public class Item {

    public static int getMaxDurability(int templateId) {
        return Item.getMaxDurability(DataStorage.ItemTemplateRepository.get(templateId));
    }

    public static int getMaxDurability(ItemTemplate template) {
        if (template == null)
            return 0;

        if (template.getLevel() == 0)
            return 30;

        return template.getLevel() * 75 + 75;
    }

    public static int getWeight(int templateId) {
        return Item.getWeight(DataStorage.ItemTemplateRepository.get(templateId));
    }

    public static int getWeight(ItemTemplate template) {
        if (template == null) {
            return 0;
        }

        switch (template.getItemClass()) {
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

    public static boolean isEquipment(int templateId) {
        return isEquipment(DataStorage.ItemTemplateRepository.get(templateId));
    }

    public static boolean isEquipment(ItemTemplate template) {
        if (template == null) {
            return false;
        }

        switch (template.getItemClass()) {
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

    public static ServerPacket getItemInfo(int templateId, int modification, ServerPacket packet) {
        ItemPrototype prototype = new ItemPrototype(templateId, modification, 0);
        Item item = new Item(prototype);
        if (item.Template == null)
            return packet;

        packet.add(ServerCommands.ItemInfo)
                .add(item.Template.getId())
                .add(item.getModification())
                .add(item.getFullTitle())
                .add(item.Template.getImageId())
                .add(item.Template.getItemClass())
                .add(item.Template.getLevel())
                .add(item.Template.getCost())
                .add(item.Template.getCurrencyId())
                .add("1187244746") // Image (or even Item itself) date
                .add(getMaxDurability(item.Template))
                .add(getWeight(item.Template));

        for (Map.Entry<ItemBonusParameters, Integer> entry : item.bonusParameters.entrySet()) {
            packet.add(entry.getKey());
            packet.add(entry.getValue());
        }
        return packet;
    }

    public final ItemTemplate Template;

    public final int MaxDurability;

    public final int weight;

    private int count;

    private int durability;

    private ItemModification modification;

    private Map<ItemBonusParameters, Integer> bonusParameters;

    private EnumFlags<ItemFlags> flags;

    public Item(ItemTemplate template) {
        this.Template = template;
        this.MaxDurability = this.durability = Item.getMaxDurability(template);
        this.weight = Item.getWeight(template);
        if (this.Template == null) {
            this.bonusParameters = new HashMap<>();
            this.count = 0;
        } else {
            try {
                this.bonusParameters = ItemUtils.parseTemplateBonuses(template.getItemBonuses());
            } catch (ItemUtils.BonusParsingException e) {
                Logging.Warn.log("Item Template %d bonus parsing error: %s", template.getId(), e.getMessage());
                this.bonusParameters = new HashMap<>();
            }
            this.count = 1;
            this.flags = new EnumFlags<>(this.Template.getFlags());
        }
        this.modification = new ItemModification(this, 0);
    }

    public Item(ItemTemplate template, int count) {
        this(template);
        this.count = count;
    }

    public Item(int templateId) {
        this(DataStorage.ItemTemplateRepository.get(templateId));
    }

    public Item(int templateId, int count) {
        this(DataStorage.ItemTemplateRepository.get(templateId));
        this.count = count;
    }

    public Item(ItemPrototype prototype) {
        this(prototype.getTemplateId());
        this.modification = new ItemModification(this, prototype.getModification());
        this.durability = prototype.getDurability();
    }

    public Item(ItemPrototype prototype, int count) {
        this(prototype);
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public ItemPrototype getPrototype() {
        return new ItemPrototype(this.Template.getId(), this.modification.getValue(), this.durability);
    }

    public boolean isEquipment() {
        return isEquipment(this.Template);
    }

    public boolean isWeapon() {
        return this.Template.getItemClass() == ItemClasses.Weapon;
    }

    public ItemModification getModification() {
        return this.modification;
    }

    /**
     * Gets the item current durability value.
     */
    public int getDurability() {
        return this.durability;
    }

    public String getTitle() {
        String title = this.Template.getTitle();
        if (this.modification.getTotem() != ItemTotemicTypes.None) {
            title += Locale.getString(4) + Locale.getString(this.modification.getTotem().getTitleStringId());
        }
        return title;
    }

    public boolean hasFlag(ItemFlags flag) {
        return this.flags.has(flag);
    }

    /**
     * Gets the title and description.
     */
    public String getFullTitle() {
        String title = getTitle();
        if (!this.Template.getDescription().isEmpty()) {
            title += "\r\n" + this.Template.getDescription();
        }
        return title;
    }

    /**
     * Moves the whole stack of the specified source Item into this Item
     */
    public boolean transfer(Item source) {
        if (source == null)
            return false;
        return this.transfer(source, source.count);
    }

    /**
     * Moves the specified count of the specified source Item into this Item
     */
    public boolean transfer(Item source, int count) {
        if (!isStackableWith(source))
            return false;

        if (count > source.count)
            count = source.count;

        this.count += count;
        source.count -= count;
        return true;
    }

    /**
     * Separates the item into two parts, returns the one that has the specified count
     * and applies the other part to this Item object.
     *
     * @param count How much items to extract from this item stack.
     * @return New Item Object with the specified count.
     */
    public Item unstackItem(int count) {
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
    public boolean tryStackItem(Item item) {
        if (item == null)
            return false;
        return this.tryStackItem(item, item.count);
    }

    /**
     * Try to stack this item with another item with the specified count.
     */
    public boolean tryStackItem(Item item, int count) {
        if (!this.isStackableWith(item))
            return false;
        if (count > item.count)
            count = item.count;

        this.count += count;
        item.count -= count;
        return true;
    }

    public boolean isStackableWith(Item item) {
        // Equipment may not be stacked
        if (this.isEquipment())
            return false;

        // Only items with the same prototypes can be stacked
        return this.equals(item);
    }

    private void addBonusValue(ItemBonusParameters parameter, int value) {
        Integer oldValue = this.bonusParameters.get(parameter);
        if (oldValue == null)
            oldValue = 0;
        this.bonusParameters.put(parameter, oldValue + value);
    }

    public int getBonusValue(ItemBonusParameters parameter) {
        Integer value = this.bonusParameters.get(parameter);
        if (value == null)
            value = 0;
        return value;
    }

    public Map<ItemBonusParameters, Integer> getBonusParameters() {
        return this.bonusParameters;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Item))
            return false;

        return this.equals((Item) obj);
    }

    public boolean equals(Item item) {
        if (item == null)
            return false;
        return equals(item.getPrototype());
    }

    public boolean equals(ItemPrototype prototype) {
        if (prototype == null)
            return false;

        return this.Template.getId() == prototype.getTemplateId() &&
                this.modification.getValue() == prototype.getModification() &&
                this.durability == prototype.getDurability();
    }

    @Override
    public int hashCode() {
        return this.getPrototype().hashCode();
    }

    /**
     * Gets a complete copy of this Item class instance.
     */
    @Override
    protected Item clone() {
        return this.clone(this.count);
    }

    /**
     * Gets a complete copy of this Item class instance but with the specified count.
     *
     * @param count Item count of a copy.
     * @return New Item as a clone of this Item
     */
    protected Item clone(int count) {
        // TODO: Clone item by initialize default item and assign all the field.
        // not doing recalculating these fields values (too efficient)
        Item newItem = new Item(this.Template, count);
        newItem.bonusParameters = new HashMap<>(this.bonusParameters);
        newItem.durability = this.durability;
        newItem.modification = this.modification.clone();
        return newItem;
    }

    public void use(Player user) {
        Spell spell = null;
        switch (this.Template.getId()) {
            case 64: // Small Health Potion
                int amount = user.getParameters().value(Parameters.Health) - user.getHealth();
                // HP is full
                if (amount < 1)
                    return;
                // TODO: level checking
                user.setHealth(user.getParameters().value(Parameters.Health));
                if (user.getSession() != null)
                    user.getSession().sendServerMessage(500, this.Template.getTitle(), Integer.toString(amount));
                break;
            case 3581: // Small Mana Potion
                // TODO: Implement
                break;
            case 35196: // Steel Body
                spell = new Spell(16, user, 3, null, this);
                break;
            case 35197: // Bears Blood
                // Level from Item level???
                spell = new Spell(17, user, 3, null, this);
                break;
            case 35198: // Tigers Strength
                spell = new Spell(14, user, 3, null, this);
                break;
            case 35199: // Feline Grace
                spell = new Spell(18, user, 3, null, this);
                break;
            case 35200: // Wisdom of the Owl
                spell = new Spell(37, user, 3, null, this);
                break;
        }

        if (spell != null) {
            if (spell.cast())
                --this.count;
        }
    }
}

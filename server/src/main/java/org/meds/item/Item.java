package org.meds.item;

import org.meds.data.domain.ItemTemplate;
import org.meds.util.EnumFlags;

import java.util.HashMap;
import java.util.Map;

public class Item {

    private final ItemTemplate template;

    private int count;

    private int durability;

    private ItemModification modification;

    private Map<ItemBonusParameters, Integer> bonusParameters;

    private EnumFlags<ItemFlags> flags;

    Item(ItemTemplate template, int count, int durability, int modification, Map<ItemBonusParameters, Integer> bonuses) {
        this.template = template;
        this.durability = durability;
        this.count = count;
        this.bonusParameters = bonuses;
        this.flags = new EnumFlags<>(this.template.getFlags());
        this.modification = new ItemModification(this, modification);
    }

    public ItemTemplate getTemplate() {
        return this.template;
    }

    public int getCount() {
        return this.count;
    }

    public ItemPrototype getPrototype() {
        return new ItemPrototype(this.template.getId(), this.modification.getValue(), this.durability);
    }

    public boolean isEquipment() {
        return ItemUtils.isEquipment(this.template);
    }

    public boolean isWeapon() {
        return this.template.getItemClass() == ItemClasses.Weapon;
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

    public boolean hasFlag(ItemFlags flag) {
        return this.flags.has(flag);
    }

    /**
     * Moves the specified count of the specified source Item into this Item
     */
    public void transfer(Item source, int count) {
        if (count > source.count) {
            count = source.count;
        }

        this.count += count;
        source.count -= count;
    }

    /**
     * Separates the item into two parts, returns the one that has the specified count
     * and applies the other part to this Item object.
     *
     * @param count How much items to extract from this item stack.
     * @return New Item Object with the specified count.
     */
    public Item unstackItem(int count) {
        if (count < 1) {
            return null;
        }

        if (count > this.count) {
            count = this.count;
        }
        Item newItem = this.clone(count);
        this.count -= count;
        return newItem;
    }

    /**
     * Adds a whole stack of the specified item into this item.
     */
    public void stackItem(Item item) {
        if (item == null) {
            return;
        }
        this.stackItem(item, item.count);
    }

    /**
     * Adds the specified amount of the specified item into this item.
     */
    public void stackItem(Item item, int count) {
        if (count > item.count) {
            count = item.count;
        }

        this.count += count;
        item.count -= count;
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
        if (obj == null) {
            return false;
        }
        return obj instanceof Item && this.equals((Item) obj);

    }

    public boolean equals(Item item) {
        return item != null && equals(item.getPrototype());
    }

    public boolean equals(ItemPrototype prototype) {
        return prototype != null &&
                this.template.getId() == prototype.getTemplateId() &&
                this.modification.getValue() == prototype.getModification()
                && this.durability == prototype.getDurability();
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
        // Without values recalculation (it's too efficient)
        HashMap<ItemBonusParameters, Integer> bonuses = new HashMap<>(this.bonusParameters);
        Item newItem = new Item(this.template, count, durability, modification.getValue(), bonuses);
        return newItem;
    }

    /**
     * Uses the item if it is usable.
     * If the item is consumable, its count decrements
     */
    public void use() {
        // Check for non-consumable usable items (i.e. "Road to home")
        --this.count;
    }
}

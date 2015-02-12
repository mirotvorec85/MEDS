package org.meds;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.meds.database.DBStorage;
import org.meds.database.entity.ItemTemplate;
import org.meds.enums.*;
import org.meds.net.ServerCommands;
import org.meds.net.ServerPacket;
import org.meds.spell.Spell;
import org.meds.util.Random;
import org.meds.util.Valued;

public class Item {

    public static class Prototype implements Serializable, Cloneable {

        private static final long serialVersionUID = -2685946807316472347L;
        private int templateId;
        private int modification;
        private int durability;

        public Prototype() {

        }

        public Prototype(int templateId, int modification, int durability) {
            this.templateId = templateId;
            this.modification = modification;
            this.durability = durability;
        }

        public int getTemplateId()  {
            return templateId;
        }

        private void setTemplateId(int templateId) {
            this.templateId = templateId;
        }

        public int getModification() {
            return modification;
        }

        private void setModification(int modification) {
            this.modification = modification;
        }

        public int getDurability() {
            return durability;
        }

        private void setDurability(int durability) {
            this.durability = durability;
        }

        @Override
        public int hashCode() {
            return (this.templateId * 1000 + this.durability) | this.modification;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (obj instanceof Prototype)
                return this.equals((Prototype)obj);
            if (obj instanceof Item)
                return this.equals((Item)obj);

            return false;
        }

        public boolean equals(Prototype prototype) {
            if (prototype == null)
                return false;

            return this.templateId == prototype.templateId &&
                    this.modification == prototype.modification &&
                    this.durability == prototype.durability;
        }

        public boolean equals(Item item) {
            if (item == null)
                return false;

            return item.equals(this);
        }

        @Override
        public String toString() {
            return "Item.Prototype - ID: " + this.templateId +
                    "; Mod: " + this.modification + "; Dur: " + this.durability;
        }
    }

    public class Modification implements Valued {

        private int value;

        private ItemTotemicTypes totem;
        private ItemEssenceLevels essenceLevel;
        private ItemEssenceTypes essenceType;
        private ItemReinforcementTypes reinforcement;

        /**
         * Private constructor that is only used for cloning.
         */
        private Modification() {
            this.totem = ItemTotemicTypes.None;
            this.essenceLevel = ItemEssenceLevels.None;
            this.essenceType = ItemEssenceTypes.None;
            this.reinforcement = ItemReinforcementTypes.None;
        }

        public Modification(int value) {
            this();
            setTotem(ItemTotemicTypes.parse((value & 0xFF000000) >> 24));
            this.essenceLevel = ItemEssenceLevels.parse((value & 0xFF0000) >> 16);
            this.essenceType = ItemEssenceTypes.parse((value & 0xFF00) >> 8);
            this.reinforcement = ItemReinforcementTypes.parse(value & 0xFF);
        }

        public ItemTotemicTypes getTotem() {
            return totem;
        }

        private void setTotem(ItemTotemicTypes totem) {
            if (totem == null)
                totem = ItemTotemicTypes.None;

            this.totem = totem;
            recalculateValue();

            if (totem == ItemTotemicTypes.None)
                return;

            // Parameters Bonus
            ItemBonusParameters parameter;
            double protectionRatio;
            double armorRatio;
            switch (totem) {
                case Mammoth:
                    parameter = ItemBonusParameters.BonusConstitution;
                    protectionRatio = 1d;
                    armorRatio = 0.33d;
                    break;
                case Tiger:
                    parameter = ItemBonusParameters.BonusStrength;
                    protectionRatio = 1d;
                    armorRatio = 0.33d;
                    break;
                case Cat:
                    parameter = ItemBonusParameters.BonusDexterity;
                    protectionRatio = 1d;
                    armorRatio = 0.33d;
                    break;
                case Owl:
                    parameter = ItemBonusParameters.BonusIntelligence;
                    protectionRatio = 1d;
                    armorRatio = 0.33d;
                    break;
                case Bear:
                    parameter = ItemBonusParameters.BonusDamage;
                    protectionRatio = 1d;
                    armorRatio = 0.33d;
                    break;
                case Turtle:
                    parameter = ItemBonusParameters.BonusProtection;
                    protectionRatio = 1d;
                    armorRatio = 0.33d;
                    break;
                case Hawk:
                    parameter = ItemBonusParameters.BonusChanceToHit;
                    protectionRatio = 1d;
                    armorRatio = 0.33d;
                    break;
                case Monkey:
                    parameter = ItemBonusParameters.BonusArmour;
                    protectionRatio = 3d;
                    armorRatio = 1d;
                    break;
                case Octopus:
                    parameter = ItemBonusParameters.BonusChanceToCast;
                    protectionRatio = 1d;
                    armorRatio = 0.33d;
                    break;
                case Spider:
                    parameter = ItemBonusParameters.BonusMagicDamage;
                    protectionRatio = 1d;
                    armorRatio = 0.33d;
                    break;
                case Whale:
                    parameter = ItemBonusParameters.BonusHealth;
                    protectionRatio = 12d;
                    armorRatio = 4d;
                    break;
                case Dragon:
                    parameter = ItemBonusParameters.BonusMana;
                    protectionRatio = 9d;
                    armorRatio = 3d;
                    break;
                case Reptile:
                    parameter = ItemBonusParameters.BonusHealthRegeneration;
                    protectionRatio = 0.33d;
                    armorRatio = 0.11d;
                    break;
                case Ant:
                    parameter = ItemBonusParameters.BonusManaRegeneration;
                    protectionRatio = 0.33d;
                    armorRatio = 0.11d;
                    break;
                case Scorpion:
                    parameter = ItemBonusParameters.BonusFireResistance;
                    protectionRatio = 1d;
                    armorRatio = 0.33d;
                    break;
                case Penguin:
                    parameter = ItemBonusParameters.BonusFrostResistance;
                    protectionRatio = 1d;
                    armorRatio = 0.33d;
                    break;
                case Eel:
                    parameter = ItemBonusParameters.BonusLightningResistance;
                    protectionRatio = 1d;
                    armorRatio = 0.33d;
                    break;
                default:
                    return;
            }

            int value = 0;
            // Uses a half of given base protection and armour item parameters
            if (Item.this.bonusParameters.containsKey(ItemBonusParameters.BaseProtection)) {
                int protection = Item.this.bonusParameters.get(ItemBonusParameters.BaseProtection);
                protection /= 2;
                Item.this.bonusParameters.put(ItemBonusParameters.BonusProtection, protection);
                value += protection * protectionRatio;
            }
            if (Item.this.bonusParameters.containsKey(ItemBonusParameters.BaseArmour)) {
                int armour = Item.this.bonusParameters.get(ItemBonusParameters.BaseArmour);
                armour /= 2;
                Item.this.bonusParameters.put(ItemBonusParameters.BaseArmour, armour);
                value += armour * armorRatio;
            }

            Integer currentValue = Item.this.bonusParameters.get(parameter);
            if (currentValue == null) {
                currentValue = 0;
            }
            currentValue += value;
            Item.this.bonusParameters.put(parameter, currentValue);
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

        private void recalculateValue() {
            this.value = (this.totem.getValue() << 24) +
                    (this.essenceLevel.getValue() << 16) +
                    (this.essenceType.getValue() << 8) +
                    this.reinforcement.getValue();
        }

        public void generateTotemicType() {
            // Equipment only (but Weapon does not have totemic bonus)
            if (!Item.this.isEquipment() || Item.this.Template.getItemClass() == ItemClasses.Weapon)
                return;

            setTotem(ItemTotemicTypes.parse(
                    Random.nextInt(ItemTotemicTypes.Mammoth.getValue(), ItemTotemicTypes.Eel.getValue() + 1)));
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

        @Override
        public Modification clone()
        {
            Modification newModification = new Modification();
            newModification.value = this.value;
            newModification.essenceLevel = this.essenceLevel;
            newModification.essenceType = this.essenceType;
            newModification.reinforcement = this.reinforcement;
            newModification.totem = this.totem;
            return newModification;
        }
    }

    public static int getMaxDurability(int templateId) {
        return Item.getMaxDurability(DBStorage.ItemTemplateStore.get(templateId));
    }

    public static int getMaxDurability(ItemTemplate template){
        if (template == null)
            return 0;

        if (template.getLevel() == 0)
            return 30;

        return template.getLevel() * 75 + 75;
    }

    public static int getWeight(int templateId)
    {
        return Item.getWeight(DBStorage.ItemTemplateStore.get(templateId));
    }

    public static int getWeight(ItemTemplate template) {
        if (template == null)
            return 0;

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
        return isEquipment(DBStorage.ItemTemplateStore.get(templateId));
    }

    public static boolean isEquipment(ItemTemplate template) {
        if (template == null)
            return false;
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
        Prototype prototype = new Prototype(templateId, modification, 0);
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

    public final int Weight;

    private int count;

    private int durability;

    private Modification modification;

    private Map<ItemBonusParameters, Integer> bonusParameters;

    public Item(ItemTemplate template) {
        this.Template = template;
        this.MaxDurability = this.durability = Item.getMaxDurability(template);
        this.Weight = Item.getWeight(template);
        if (this.Template == null) {
            this.bonusParameters = new HashMap<>();
            this.count = 0;
        } else {
            this.bonusParameters = new HashMap<>(this.Template.getBonusParameters());
            this.count = 1;
        }
        this.modification = new Modification(0);
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

    public Item(Prototype prototype)
    {
        this(prototype.getTemplateId());
        this.modification = new Modification(prototype.getModification());
        this.durability = prototype.getDurability();
    }

    public Item(Prototype prototype, int count) {
        this(prototype);
        this.count = count;
    }

    public int getCount()
    {
        return this.count;
    }

    public Prototype getPrototype() {
        return new Prototype(this.Template.getId(), this.modification.value, this.durability);
    }

    public boolean isEquipment() {
        return isEquipment(this.Template);
    }

    public Modification getModification()
    {
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
    public boolean transfer(Item source)
    {
        if (source == null)
            return false;
        return this.transfer(source, source.count);
    }

    /**
     * Moves the specified count of the specified source Item into this Item
     */
    public boolean transfer(Item source, int count)
    {
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
        if (item == null)
            return false;
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

    public Map<ItemBonusParameters, Integer> getBonusParameters()
    {
        return Collections.unmodifiableMap(this.bonusParameters);
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
        if (item == null)
            return false;
        return equals(item.getPrototype());
    }

    public boolean equals(Prototype prototype) {
        return prototype.equals(this.getPrototype());
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
        Item newItem = new Item(this.Template, count);
        newItem.bonusParameters = new HashMap<>(this.bonusParameters);
        newItem.durability = this.durability;
        newItem.modification = this.modification.clone();
        return newItem;
    }

    public void use(Player user)
    {
        Spell spell = null;
        switch (this.Template.getId())
        {
            case 64: // Small Health Potion
                int amount = user.parameters.value(Parameters.Health) - user.getHealth();
                // HP is full
                if (amount < 1)
                    return;
                // TODO: level checking
                user.setHealth(user.parameters.value(Parameters.Health));
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

        if (spell != null)
        {
            if (spell.cast())
                --this.count;
        }
    }
}

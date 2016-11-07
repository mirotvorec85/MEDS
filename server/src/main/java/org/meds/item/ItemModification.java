package org.meds.item;

import org.meds.util.Valued;
import org.meds.util.Random;

import java.util.Map;

/**
 * Created by Romman on 05.11.2016.
 */
public class ItemModification implements Valued {

    private Item item;
    private int value;

    private ItemTotemicTypes totem;
    private ItemEssenceLevels essenceLevel;
    private ItemEssenceTypes essenceType;
    private ItemReinforcementTypes reinforcement;

    /**
     * Private constructor that is only used for cloning.
     */
    private ItemModification(Item item) {
        this.item = item;
        this.totem = ItemTotemicTypes.None;
        this.essenceLevel = ItemEssenceLevels.None;
        this.essenceType = ItemEssenceTypes.None;
        this.reinforcement = ItemReinforcementTypes.None;
    }

    public ItemModification(Item item, int value) {
        this(item);
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
        ItemBonusParameters parameter = totem.getParameter();
        // Protection gets the full ratio value
        // Armour gets a third part
        double protectionRatio = totem.getRatio();
        double armorRatio = totem.getRatio() / 3;
        int value = 0;

        Map<ItemBonusParameters, Integer> bonusParameters = item.getBonusParameters();
        // Uses a half of given base protection and armour item parameters
        if (bonusParameters.containsKey(ItemBonusParameters.BaseProtection)) {
            int protection = bonusParameters.get(ItemBonusParameters.BaseProtection);
            protection /= 2;
            bonusParameters.put(ItemBonusParameters.BonusProtection, protection);
            value += protection * protectionRatio;
        }
        if (bonusParameters.containsKey(ItemBonusParameters.BaseArmour)) {
            int armour = bonusParameters.get(ItemBonusParameters.BaseArmour);
            armour /= 2;
            bonusParameters.put(ItemBonusParameters.BaseArmour, armour);
            value += armour * armorRatio;
        }
        // The value should be positive (1 at least)
        if (value == 0) {
            value = 1;
        }

        Integer currentValue = bonusParameters.get(parameter);
        if (currentValue == null) {
            currentValue = 0;
        }
        currentValue += value;
        bonusParameters.put(parameter, currentValue);
    }

    public ItemEssenceLevels getEssenceLevel() {
        return essenceLevel;
    }

    public void setEssenceLevel(ItemEssenceLevels essenceLevel) {
        this.essenceLevel = essenceLevel;
        recalculateValue();
    }

    public ItemEssenceTypes getEssenceType() {
        return essenceType;
    }

    public void setEssenceType(ItemEssenceTypes essenceType) {
        this.essenceType = essenceType;
        recalculateValue();
    }

    public ItemReinforcementTypes getReinforcement() {
        return reinforcement;
    }

    public void setReinforcement(ItemReinforcementTypes reinforcement) {
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
        if (!item.isEquipment() || item.isWeapon())
            return;

        setTotem(ItemTotemicTypes.parse(
                Random.nextInt(ItemTotemicTypes.Mammoth.getValue(), ItemTotemicTypes.Eel.getValue() + 1)));
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }

    @Override
    public ItemModification clone() {
        ItemModification newModification = new ItemModification(this.item);
        newModification.value = this.value;
        newModification.essenceLevel = this.essenceLevel;
        newModification.essenceType = this.essenceType;
        newModification.reinforcement = this.reinforcement;
        newModification.totem = this.totem;
        return newModification;
    }
}

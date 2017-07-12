package org.meds.item;

import org.meds.data.domain.ItemTemplate;
import org.meds.net.ServerCommands;
import org.meds.net.ServerPacket;
import org.meds.util.SafeConvert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Romman
 */
public final class ItemUtils {

    private ItemUtils() {}

    public static int getMaxDurability(ItemTemplate template) {
        if (template == null) {
            return 0;
        }

        if (template.getLevel() == 0) {
            return 30;
        }

        return template.getLevel() * 75 + 75;
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

    public static boolean isEquipment(Item item) {
        return isEquipment(item.Template);
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

    public static boolean areStackable(Item itemA, Item itemB) {
        // Equipment may not be stacked
        if (isEquipment(itemA) || isEquipment(itemB)) {
            return false;
        }

        // Only items with the same prototypes can be stacked
        return itemA.equals(itemB);
    }

    public static class BonusParsingException extends Exception {

        private final String initialBonusString;

        public BonusParsingException(String initialBonusString, String message) {
            super(message);
            this.initialBonusString = initialBonusString;
        }

        @Override
        public String getMessage() {
            return super.getMessage() + " Initial bonus string: \"" + initialBonusString + "\".";
        }
    }

    // TODO: Move to a new class
    public Map<ItemBonusParameters, Integer> parseTemplateBonuses(String bonuses) throws BonusParsingException {
        String[] keyValues = bonuses.split(";");
        Map<ItemBonusParameters, Integer> bonusMap = new HashMap<>(keyValues.length);
        for (int i = 0; i < keyValues.length; ++i) {
            // Ignore empty strings
            // It may happen when the itemBonus string ends with ";"
            if (keyValues[i].length() == 0)
                continue;

            String[] keyValue = keyValues[i].split(":");
            if (keyValue.length != 2) {
                throw new BonusParsingException(bonuses, "Invalid bonus key-value pair \"" + keyValues[i] + "\".");
            }

            ItemBonusParameters parameter = ItemBonusParameters.parse(SafeConvert.toInt32(keyValue[0], -1));
            int value = SafeConvert.toInt32(keyValue[1], -1);
            if (parameter == null) {
                throw new BonusParsingException(bonuses, "Invalid ItemBonusParameter type:  \"" + keyValue[0] + "\".");
            }
            if (value <= 0) {
                throw new BonusParsingException(bonuses, "Invalid ItemBonusParameter value \"" + keyValue[1] + "\".");
            }
            bonusMap.put(parameter, value);
        }
        return bonusMap;
    }

    // TODO: possibly new component ItemInfoPacketFactory?
    public ServerPacket getItemInfo(int templateId, int modification, ServerPacket packet) {
        ItemPrototype prototype = new ItemPrototype(templateId, modification, 0);
        Item item = itemFactory.create(prototype);
        if (item.Template == null) {
            return packet;
        }

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

        // TODO: Spring fix build
//        for (Map.Entry<ItemBonusParameters, Integer> entry : item.bonusParameters.entrySet()) {
//            packet.add(entry.getKey());
//            packet.add(entry.getValue());
//        }
        return packet;
    }
}

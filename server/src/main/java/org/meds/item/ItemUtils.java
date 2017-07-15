package org.meds.item;

import org.meds.data.domain.ItemTemplate;

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
        return isEquipment(item.getTemplate());
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
}

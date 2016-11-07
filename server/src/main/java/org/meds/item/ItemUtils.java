package org.meds.item;

import org.meds.logging.Logging;
import org.meds.util.SafeConvert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Romman on 05.11.2016.
 */
public final class ItemUtils {

    private ItemUtils() { }

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

    public static Map<ItemBonusParameters, Integer> parseTemplateBonuses(String bonuses) throws BonusParsingException {
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
}

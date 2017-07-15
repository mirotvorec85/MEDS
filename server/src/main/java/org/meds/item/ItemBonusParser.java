package org.meds.item;

import org.meds.util.SafeConvert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Romman
 */
@Component
public class ItemBonusParser {

    /**
     * @throws ItemBonusParseException
     */
    public Map<ItemBonusParameters, Integer> parse(String bonuses) {
        String[] keyValues = bonuses.split(";");
        Map<ItemBonusParameters, Integer> bonusMap = new HashMap<>(keyValues.length);
        for (int i = 0; i < keyValues.length; ++i) {
            // Ignore empty strings
            // It may happen when the itemBonus string ends with ";"
            if (keyValues[i].length() == 0) {
                continue;
            }

            String[] keyValue = keyValues[i].split(":");
            if (keyValue.length != 2) {
                throw new ItemBonusParseException(bonuses, "Invalid bonus key-value pair \"" + keyValues[i] + "\".");
            }

            ItemBonusParameters parameter = ItemBonusParameters.parse(SafeConvert.toInt32(keyValue[0], -1));
            int value = SafeConvert.toInt32(keyValue[1], -1);
            if (parameter == null) {
                throw new ItemBonusParseException(bonuses, "Invalid ItemBonusParameter type:  \"" + keyValue[0] + "\".");
            }
            if (value <= 0) {
                throw new ItemBonusParseException(bonuses, "Invalid ItemBonusParameter value \"" + keyValue[1] + "\".");
            }
            bonusMap.put(parameter, value);
        }
        return bonusMap;
    }
}

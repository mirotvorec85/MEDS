package org.meds;

import org.meds.data.dao.DAOFactory;
import org.meds.data.domain.LocaleString;
import org.meds.logging.Logging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Locale {

    private static Map<Integer, String> strings;

    public static void load() {
        List<LocaleString> localeStrings = DAOFactory.getFactory().getWorldDAO().getLocaleStrings();
        strings = new HashMap<>(localeStrings.size());
        for (LocaleString string : localeStrings) {
            strings.put(string.getId(), string.getString());
        }
        Logging.Info.log("Loaded " + localeStrings.size() + " locale strings.");
    }

    public static String getString(int id) {
        String string = strings.get(id);
        if (string == null)
            return "";
        return string;
    }
}

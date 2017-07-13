package org.meds;

import org.meds.data.dao.DAOFactory;
import org.meds.data.domain.LocaleString;
import org.meds.logging.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Locale {

    @Autowired
    private DAOFactory daoFactory;

    private Map<Integer, String> strings;

    public void load() {
        List<LocaleString> localeStrings = daoFactory.getWorldDAO().getLocaleStrings();
        this.strings = new HashMap<>(localeStrings.size());
        for (LocaleString string : localeStrings) {
            this.strings.put(string.getId(), string.getString());
        }
        Logging.Info.log("Loaded " + localeStrings.size() + " locale strings.");
    }

    public String getString(int id) {
        String string = this.strings.get(id);
        if (string == null) {
            return "";
        }
        return string;
    }
}

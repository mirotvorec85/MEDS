package org.meds.item;

import org.meds.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Represents a component that construct item title String representation
 * based on its template name and current modification type and level
 * @author Romman.
 */
@Component
public class ItemTitleConstructor {

    @Autowired
    private Locale locale;

    /**
     * Returns item title with modification (localized)
     * @param item
     * @return
     */
    public String getTitle(Item item) {
        return getItemTitleBuild(item).toString();
    }

    /**
     * Returns item title with modification and description (localized).
     * Method is used on retrieving item info data
     * @param item
     * @return
     */
    public String getTitleAndDescription(Item item) {
        StringBuilder sb = getItemTitleBuild(item);
        if (!item.getTemplate().getDescription().isEmpty()) {
            sb.append("\r\n").append(item.getTemplate().getDescription());
        }
        return sb.toString();
    }

    private StringBuilder getItemTitleBuild(Item item) {
        StringBuilder sb = new StringBuilder(item.getTemplate().getTitle());
        if (item.getModification().getTotem() != ItemTotemicTypes.None) {
            int modificationTitleId = item.getModification().getTotem().getTitleStringId();
            sb.append(locale.getString(4)).append(locale.getString(modificationTitleId));
        }
        return sb;
    }
}

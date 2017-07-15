package org.meds.item;

import org.meds.data.domain.ItemTemplate;
import org.meds.database.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Romman
 */
@Component
public class ItemFactory {

    @Autowired
    private Repository<ItemTemplate> itemTemplateRepository;
    @Autowired
    private ItemBonusParser itemBonusParser;

    public Item create(ItemTemplate template, int count) {
        int durability = ItemUtils.getMaxDurability(template);
        return createInternal(template, count, durability, 0);
    }

    public Item create(int templateId, int count) {
        ItemTemplate template = itemTemplateRepository.get(templateId);
        return create(template, count);
    }

    public Item create(ItemPrototype prototype, int count) {
        ItemTemplate template = itemTemplateRepository.get(prototype.getTemplateId());
        return createInternal(template, count, prototype.getDurability(), prototype.getModification());
    }

    public Item create(ItemPrototype prototype) {
        return create(prototype, 1);
    }

    private Item createInternal(ItemTemplate template, int count, int durability, int modification) {
        if (template == null) {
            return null;
        }
        Map<ItemBonusParameters, Integer> bonuses = itemBonusParser.parse(template.getItemBonuses());
        return new Item(template, count, durability, modification, bonuses);
    }
}

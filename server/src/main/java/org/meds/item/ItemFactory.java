package org.meds.item;

import org.meds.data.domain.ItemTemplate;
import org.meds.database.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Romman
 */
@Component
public class ItemFactory {

    @Autowired
    private ItemUtils itemUtils;

    @Autowired
    private Repository<ItemTemplate> itemTemplateRepository;

    public Item create(ItemTemplate template, int count) {
        int durability = itemUtils.getMaxDurability(template);
        return new Item(template, count, durability, 0);
    }

    public Item create(int templateId, int count) {
        ItemTemplate template = itemTemplateRepository.get(templateId);
        return create(template, count);
    }

    public Item create(ItemPrototype prototype, int count) {
        ItemTemplate template = itemTemplateRepository.get(prototype.getTemplateId());
        return new Item(template, 1, prototype.getDurability(), prototype.getModification());
    }

    public Item create(ItemPrototype prototype) {
        return create(prototype, 1);
    }
}

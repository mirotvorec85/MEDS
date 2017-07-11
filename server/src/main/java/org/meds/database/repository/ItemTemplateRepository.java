package org.meds.database.repository;

import org.meds.data.domain.ItemTemplate;
import org.meds.database.MapRepository;
import org.meds.database.Repository;
import org.springframework.stereotype.Component;

@Component
public class ItemTemplateRepository extends MapRepository<ItemTemplate> implements Repository<ItemTemplate> {
}

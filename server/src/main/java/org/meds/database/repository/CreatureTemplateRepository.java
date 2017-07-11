package org.meds.database.repository;

import org.meds.data.domain.CreatureTemplate;
import org.meds.database.MapRepository;
import org.meds.database.Repository;
import org.springframework.stereotype.Component;

@Component
public class CreatureTemplateRepository extends MapRepository<CreatureTemplate> implements Repository<CreatureTemplate> {
}

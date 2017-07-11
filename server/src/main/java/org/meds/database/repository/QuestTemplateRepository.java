package org.meds.database.repository;

import org.meds.data.domain.QuestTemplate;
import org.meds.database.MapRepository;
import org.meds.database.Repository;
import org.springframework.stereotype.Component;

@Component
public class QuestTemplateRepository extends MapRepository<QuestTemplate> implements Repository<QuestTemplate> {
}

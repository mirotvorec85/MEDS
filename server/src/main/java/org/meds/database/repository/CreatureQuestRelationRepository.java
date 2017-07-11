package org.meds.database.repository;

import org.meds.data.domain.CreatureQuestRelation;
import org.meds.database.BiRepository;
import org.meds.database.MapBiRepository;
import org.springframework.stereotype.Component;

@Component
public class CreatureQuestRelationRepository extends MapBiRepository<CreatureQuestRelation>
        implements BiRepository<CreatureQuestRelation> {
}

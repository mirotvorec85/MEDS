package org.meds.database.repository;

import org.meds.data.domain.Skill;
import org.meds.database.MapRepository;
import org.meds.database.Repository;
import org.springframework.stereotype.Component;

@Component
public class SkillRepository extends MapRepository<Skill> implements Repository<Skill> {
}

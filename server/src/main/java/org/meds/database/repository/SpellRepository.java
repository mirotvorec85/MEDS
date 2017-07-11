package org.meds.database.repository;

import org.meds.data.domain.Spell;
import org.meds.database.MapRepository;
import org.meds.database.Repository;
import org.springframework.stereotype.Component;

@Component
public class SpellRepository extends MapRepository<Spell> implements Repository<Spell> {
}

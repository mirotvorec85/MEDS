package org.meds.database.repository;

import org.meds.data.domain.CreatureLoot;
import org.meds.database.BiRepository;
import org.meds.database.MapBiRepository;
import org.springframework.stereotype.Component;

@Component
public class CreatureLootRepository extends MapBiRepository<CreatureLoot> implements BiRepository<CreatureLoot> {
}

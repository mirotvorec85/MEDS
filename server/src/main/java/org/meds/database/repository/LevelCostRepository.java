package org.meds.database.repository;

import org.meds.data.domain.LevelCost;
import org.meds.database.MapRepository;
import org.meds.database.Repository;
import org.springframework.stereotype.Component;

@Component
public class LevelCostRepository extends MapRepository<LevelCost> implements Repository<LevelCost> {
}

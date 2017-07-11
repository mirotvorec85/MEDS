package org.meds.database.repository;

import org.meds.data.domain.Guild;
import org.meds.database.MapRepository;
import org.meds.database.Repository;
import org.springframework.stereotype.Component;

@Component
public class GuildRepository extends MapRepository<Guild> implements Repository<Guild> {
}

package org.meds.database.repository;

import org.meds.data.domain.GuildLesson;
import org.meds.database.BiRepository;
import org.meds.database.MapBiRepository;
import org.springframework.stereotype.Component;

@Component
public class GuildLessonRepository extends MapBiRepository<GuildLesson>
        implements BiRepository<GuildLesson> {
}

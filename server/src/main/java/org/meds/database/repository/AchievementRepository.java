package org.meds.database.repository;

import org.meds.data.domain.Achievement;
import org.meds.data.domain.AchievementCriterion;
import org.meds.database.MapRepository;
import org.meds.database.Repository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AchievementRepository extends MapRepository<Achievement> implements Repository<Achievement> {

    public void setData(List<Achievement> achievements, List<AchievementCriterion> criteria) {
        setData(achievements, Achievement::getId);

        // Populate achievement criteria
        for (AchievementCriterion criterion : criteria) {
            get(criterion.getAchievementId()).getCriteria().add(criterion);
        }
    }
}

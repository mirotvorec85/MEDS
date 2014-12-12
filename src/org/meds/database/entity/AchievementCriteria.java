package org.meds.database.entity;

import org.meds.database.DBStorage;
import org.meds.enums.AchievementCriteriaTypes;

import java.io.Serializable;

public class AchievementCriteria implements Serializable {

    private Achievement achievement;
    private int index;
    private AchievementCriteriaTypes type;
    private int requirement;

    public Achievement getAchievement() {
        return achievement;
    }

    public int getAchievementId() {
        return this.achievement.getId();
    }

    public void setAchievementId(int achievementId) {
        this.achievement = DBStorage.AchievementStore.get(achievementId);
        this.achievement.getCriterias().add(this);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public AchievementCriteriaTypes getType() {
        return type;
    }

    public int getCriteriaTypeId() {
        return this.type.getValue();
    }

    public void setCriteriaTypeId(int criteriaTypeId) {
        this.type = AchievementCriteriaTypes.parse(criteriaTypeId);
    }

    public int getRequirement() {
        return requirement;
    }

    public void setRequirement(int requirement) {
        this.requirement = requirement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AchievementCriteria that = (AchievementCriteria) o;

        if (this.achievement.getId() != that.achievement.getId() ||
                this.index != that.index) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return this.achievement.getId() + this.index;
    }
}

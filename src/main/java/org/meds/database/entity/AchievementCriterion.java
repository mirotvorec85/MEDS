package org.meds.database.entity;

import java.io.Serializable;

public class AchievementCriterion implements Serializable {

    private int achievementId;
    private int index;
    private int criteriaTypeId;
    private int requirement;

    public int getAchievementId() {
        return this.achievementId;
    }

    public void setAchievementId(int achievementId) {
        this.achievementId = achievementId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCriteriaTypeId() {
        return this.criteriaTypeId;
    }

    public void setCriteriaTypeId(int criteriaTypeId) {
        this.criteriaTypeId = criteriaTypeId;
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

        AchievementCriterion that = (AchievementCriterion) o;

        return this.achievementId == that.achievementId
                && this.index == that.index;
    }

    @Override
    public int hashCode() {
        return this.achievementId + this.index;
    }
}

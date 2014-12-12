package org.meds.database.entity;

import org.meds.Kingdom;
import org.meds.Region;
import org.meds.enums.AchievementCategories;

import java.util.HashSet;
import java.util.Set;

public class Achievement {

    private int id;
    private String title;
    private String description;
    private int count;
    private int points;
    private AchievementCategories category;
    private Set<AchievementCriteria> criterias;

    public Achievement() {
        this.criterias = new HashSet<AchievementCriteria>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public AchievementCategories getCategory() {
        return category;
    }

    public int getCategoryId() {
        return this.category.getValue();
    }

    public void setCategoryId(int categoryId) {
        this.category = AchievementCategories.parse(categoryId);
    }

    public Set<AchievementCriteria> getCriterias() {
        return criterias;
    }

    public void setCriterias(Set<AchievementCriteria> criterias) {
        this.criterias = criterias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Achievement that = (Achievement) o;

        if (this.id != that.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}

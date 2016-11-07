package org.meds.data.domain;

import java.io.Serializable;

public class CharacterAchievement implements Serializable {


    private static final long serialVersionUID = 1L;

    private int characterId;
    private int achievementId;
    private int progress;
    private int completeDate;

    public int getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(int achievementId) {
        this.achievementId = achievementId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(int completeDate) {
        this.completeDate = completeDate;
    }

    public boolean isCompleted() {
        return this.completeDate != 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CharacterAchievement that = (CharacterAchievement) o;

        return this.achievementId == that.achievementId
                && this.characterId == that.characterId;
    }

    @Override
    public int hashCode() {
        return this.characterId + this.achievementId;
    }
}

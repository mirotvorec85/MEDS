package org.meds.database.entity;

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
}

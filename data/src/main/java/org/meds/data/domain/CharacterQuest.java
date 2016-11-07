package org.meds.data.domain;


import java.io.Serializable;

@SuppressWarnings("unused")
public class CharacterQuest implements Serializable {

    private static final long serialVersionUID = 8565314324578762825L;

    private int characterId;
    private int questTemplateId;
    private int statusInteger;
    private int progress;
    private int timer;
    private boolean tracked;
    private int acceptDate;
    private int completeDate;

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getQuestTemplateId() {
        return questTemplateId;
    }

    public void setQuestTemplateId(int questTemplateId) {
        this.questTemplateId = questTemplateId;
    }

    public void setStatusInteger(int statusInteger) {
        this.statusInteger = statusInteger;
    }

    public int getStatusInteger() {
        return this.statusInteger;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public boolean isTracked() {
        return tracked;
    }

    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }

    public int getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(int acceptDate) {
        this.acceptDate = acceptDate;
    }

    public int getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(int completeDate) {
        this.completeDate = completeDate;
    }

    @Override
    public int hashCode()
    {
        return this.characterId * 1000 + this.questTemplateId;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (!(obj instanceof CharacterQuest))
            return false;
        CharacterQuest cObj = (CharacterQuest)obj;

        return this.characterId == cObj.characterId && this.questTemplateId == cObj.questTemplateId;
    }
}

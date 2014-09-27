package meds.database.entity;

import java.io.Serializable;

public class CreatureQuestRelation implements Serializable
{
    private static final long serialVersionUID = -758484490637054857L;

    private int creatureTemplateId;
    private int questTemplateId;
    private int relation;

    public int getCreatureTemplateId()
    {
        return creatureTemplateId;
    }
    public void setCreatureTemplateId(int creatureTemplateId)
    {
        this.creatureTemplateId = creatureTemplateId;
    }
    public int getQuestTemplateId()
    {
        return questTemplateId;
    }
    public void setQuestTemplateId(int questTemplateId)
    {
        this.questTemplateId = questTemplateId;
    }
    public int getRelation()
    {
        return relation;
    }
    public void setRelation(int relation)
    {
        this.relation = relation;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof CreatureQuestRelation))
            return false;
        CreatureQuestRelation cObj = (CreatureQuestRelation)obj;

        return this.creatureTemplateId == cObj.creatureTemplateId && this.questTemplateId == cObj.questTemplateId;
    }

    @Override
    public int hashCode()
    {
        return this.creatureTemplateId * 1000000 + this.questTemplateId;
    }
}

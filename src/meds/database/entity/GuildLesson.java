 package meds.database.entity;

import java.io.Serializable;

import meds.util.Valued;

public class GuildLesson implements Serializable
{
    public enum ImprovementTypes implements Valued
    {
        None(0),
        Parameter(1),
        Spell(2),
        Skill(3);

        public static ImprovementTypes parse(int value)
        {
            switch (value)
            {
                case 0: return None;
                case 1: return Parameter;
                case 2: return Spell;
                case 3: return Skill;
                default: return null;
            }
        }

        private final int value;

        private ImprovementTypes(int value)
        {
            this.value = value;
        }

        @Override
        public int getValue()
        {
            return this.value;
        }

        @Override
        public String toString()
        {
            return Integer.toString(this.value);
        }
    }

    static final long serialVersionUID = 12345567890L;

    private int guildId;
    private int level;
    private String description;
    private ImprovementTypes improvementType1;
    private int id1;
    private int count1;
    private ImprovementTypes improvementType2;
    private int id2;
    private int count2;

    public int getGuildId()
    {
        return guildId;
    }
    public void setGuildId(int guildId)
    {
        this.guildId = guildId;
    }
    public int getLevel()
    {
        return level;
    }
    public void setLevel(int level)
    {
        this.level = level;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public int getImprovementType1Integer()
    {
        return improvementType1.value;
    }
    public ImprovementTypes getImprovementType1()
    {
        return this.improvementType1;
    }
    public void setImprovementType1Integer(int improvementType1)
    {
        this.improvementType1 = ImprovementTypes.parse(improvementType1);
    }
    public int getId1()
    {
        return id1;
    }
    public void setId1(int id1)
    {
        this.id1 = id1;
    }
    public int getCount1()
    {
        return count1;
    }
    public void setCount1(int count1)
    {
        this.count1 = count1;
    }
    public int getImprovementType2Integer()
    {
        return improvementType2.value;
    }
    public ImprovementTypes getImprovementType2()
    {
        return this.improvementType2;
    }
    public void setImprovementType2Integer(int improvementType2)
    {
        this.improvementType2 = ImprovementTypes.parse(improvementType2);
    }
    public int getId2()
    {
        return id2;
    }
    public void setId2(int id2)
    {
        this.id2 = id2;
    }
    public int getCount2()
    {
        return count2;
    }
    public void setCount2(int count2)
    {
        this.count2 = count2;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (!(obj instanceof GuildLesson))
            return false;
        GuildLesson lesson = (GuildLesson)obj;
        return this.guildId == lesson.guildId && this.level == lesson.level;
    }

    @Override
    public int hashCode()
    {
        return this.guildId * 100 + this.level;
    }
}

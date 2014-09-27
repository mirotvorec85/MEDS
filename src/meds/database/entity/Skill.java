package meds.database.entity;

public class Skill
{
    private int id;
    private String name;

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public int hashCode()
    {
        return this.id;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (!(obj instanceof Skill))
            return false;
        Skill cObj = (Skill)obj;
        return this.id == cObj.id;
    }
}

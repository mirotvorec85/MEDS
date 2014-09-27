package meds;

public class Kingdom
{
    private int id;
    private String name;
    private int continentId;

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setContinentId(int continentId)
    {
        this.continentId = continentId;
    }

    public int getContinentId()
    {
        return this.continentId;
    }
}

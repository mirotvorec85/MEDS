package org.meds.map;

import java.util.ArrayList;
import java.util.List;

public class Kingdom
{
    private int id;
    private String name;
    private int continentId;

    private List<Region> regions;

    public Kingdom()
    {
        this.regions = new ArrayList<Region>();
    }

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

    public void addRegion(Region region)
    {
        this.regions.add(region);
    }
}

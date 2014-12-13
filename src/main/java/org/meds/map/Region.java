package org.meds.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.meds.net.ServerOpcodes;
import org.meds.net.ServerPacket;
import org.meds.util.Random;

public class Region
{
    private int id;
    private String name;
    private int kingdomId;
    private boolean road;
    private int minLevel;
    private int maxLevel;

    private Kingdom kingdom;
    /**
     * List of all the location the belong to this region.
     */
    private List<Location> locations = new ArrayList<Location>();
    private List<Location> locationsView = Collections.unmodifiableList(locations);
    /**
     * List of only non-special locations (i.e exclude shops, guilds, stars etc.; locations where creature are allowed to be)
     */
    private List<Location> ordinaryLocations = new ArrayList<Location>();

    private ServerPacket locationListData;

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
    public int getKingdomId()
    {
        return kingdomId;
    }
    public Kingdom getKingdom()
    {
        return this.kingdom;
    }
    public void setKingdomId(int kingdomId)
    {
        this.kingdomId = kingdomId;
        this.kingdom = Map.getInstance().getKingdom(kingdomId);
        if (this.kingdom == null)
        {
            throw new IllegalArgumentException(String.format("Region %d references to a non-existing kingdom %d", this.id, kingdomId));
        }
    }
    public boolean isRoad()
    {
        return this.road;
    }
    public void setRoad(boolean road)
    {
        this.road = road;
    }
    public int getMinLevel()
    {
        return minLevel;
    }
    public void setMinLevel(int minLevel)
    {
        this.minLevel = minLevel;
    }
    public int getMaxLevel()
    {
        return maxLevel;
    }
    public void setMaxLevel(int maxLevel)
    {
        this.maxLevel = maxLevel;
    }

    /**
     * Adds the specified location to this region.
     * @param location A location instance to add.
     */
    public void addLocation(Location location)
    {
        this.locations.add(location);
        if (!location.isSafeZone())
            this.ordinaryLocations.add(location);
    }

    public List<Location> getLocations()
    {
        return this.locationsView;
    }

    public Location getRandomLocation()
    {
        return this.getRandomLocation(true);
    }

    public Location getRandomLocation(boolean includeSpecial)
    {
        if (includeSpecial)
            return this.locations.get(Random.nextInt(0, this.locations.size()));

        return this.ordinaryLocations.get(Random.nextInt(0, this.ordinaryLocations.size()));
    }

    public ServerPacket getLocationListData()
    {
        if (this.locationListData == null)
        {
            this.locationListData = new ServerPacket(ServerOpcodes.RegionLocations)
                .add(this.id);
            for (Location location : this.locations)
                this.locationListData.add(location.getId());
        }

        return this.locationListData;
    }
}

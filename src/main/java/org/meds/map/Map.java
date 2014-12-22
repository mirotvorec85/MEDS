package org.meds.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.meds.Group;
import org.meds.Player;
import org.meds.Unit;
import org.meds.database.Hibernate;
import org.meds.enums.MovementDirections;
import org.meds.logging.Logging;

import org.hibernate.Session;
import org.meds.net.ServerOpcodes;
import org.meds.net.ServerPacket;

public class Map
{
    private static Map instance;

    public static Map getInstance()
    {
        if (Map.instance == null)
        {
            Map.instance = new Map();
        }

        return Map.instance;
    }

    private HashMap<Integer, Kingdom> kingdoms;
    private HashMap<Integer, Region> regions;
    private HashMap<Integer, Location> locations;

    private HashMap<Integer, Shop> shops;

    private HashMap<Unit, MovementDirections> unitMovement;

    private int corpseGuidCounter;

    private HashSet<Location> updatedLocations;

    private Map()
    {
        this.kingdoms = new HashMap<>();
        this.regions = new HashMap<>();
        this.locations = new HashMap<>();
        this.updatedLocations = new HashSet<>();
        this.shops = new HashMap<>();
        this.unitMovement = new HashMap<>();

        this.corpseGuidCounter = 1;
    }

    public Location getLocation(int locationId)
    {
        return this.locations.get(locationId);
    }

    public Region getRegion(int regionId)
    {
        return this.regions.get(regionId);
    }

    public Kingdom getKingdom(int kingdomId)
    {
        return this.kingdoms.get(kingdomId);
    }

    public Shop getShop(int shopId)
    {
        return this.shops.get(shopId);
    }

    @SuppressWarnings("unchecked")
    public void load()
    {
        Session session = Hibernate.getSessionFactory().openSession();
        List<Kingdom> kingdoms = session.createCriteria(Kingdom.class).list();
        for (Kingdom kingdom : kingdoms)
        {
            this.kingdoms.put(kingdom.getId(), kingdom);
        }
        Logging.Info.log("Loaded " + this.kingdoms.size() + " kingdoms");

        List<org.meds.database.entity.Region> regionEntries = session.createCriteria(org.meds.database.entity.Region.class).list();
        for (org.meds.database.entity.Region entry : regionEntries)
        {
            Region region = new Region(entry);
            this.regions.put(region.getId(), region);
            region.getKingdom().addRegion(region);
        }
        Logging.Info.log("Loaded " + this.regions.size() + " regions");

        List<Location> locations = session.createCriteria(Location.class).list();
        for (Location location : locations)
        {
            this.locations.put(location.getId(), location);
            location.getRegion().addLocation(location);
        }
        Logging.Info.log("Loaded " + this.locations.size() + " locations");

        // Filter duplicate values, that are the result of left outer join
        Set<Shop> shops = new HashSet<Shop>(session.createCriteria(Shop.class).list());
        for (Shop shop : shops)
        {
            shop.load();
            this.shops.put(shop.getId(), shop);
        }
        Logging.Info.log("Loaded " + this.shops.size() + " shops");

        session.close();
    }

    public void addLocationUpdate(Location location)
    {
        this.updatedLocations.add(location);
    }

    public void registerMovement(Unit unit, MovementDirections direction)
    {
        this.unitMovement.put(unit, direction);
    }

    public int getNextCorpseGuid()
    {
        return this.corpseGuidCounter++;
    }

    public void update(int time)
    {
        // Move all moving units
        synchronized (this.unitMovement)
        {
            for (java.util.Map.Entry<Unit, MovementDirections> entry : this.unitMovement.entrySet())
            {
                Unit mover = entry.getKey();

                // Current position doesn't exist
                if (mover.getPosition() == null)
                    return;

                // While fighting cancel the moving by putting at the same location
                if (mover.isInCombat())
                    mover.setPosition(mover.getPosition());

                if (!mover.canMove())
                    continue;

                if (mover.isPlayer()) {
                    Player player = (Player) mover;
                    // In a group but not a leader
                    if (player.getGroup() != null && player.getGroup().getLeader() != player)
                        continue;
                }

                // Does neighbour location exist?
                Location location = mover.getPosition().getNeighbourLocation(entry.getValue());
                if (location == null)
                    continue;

                Location prevLocation = mover.getPosition();
                // Move the unit
                if (mover.isPlayer()) {
                    Player player = (Player) mover;
                    Group group = player.getGroup();
                    // Move the whole group to this location
                    if (group != null) {
                        for (Player member : group) {
                            member.setPosition(location);
                        }
                    } else {
                        player.setPosition(location);
                    }
                } else {
                    mover.setPosition(location);
                }

                ServerPacket packet = new ServerPacket(ServerOpcodes.ServerMessage);
                switch (entry.getValue())
                {
                    case Up:
                        packet.add(450);
                        break;
                    case Down:
                        packet.add(451);
                        break;
                    case North:
                        packet.add(452);
                        break;
                    case South:
                        packet.add(453);
                        break;
                    case West:
                        packet.add(454);
                        break;
                    case East:
                        packet.add(455);
                        break;
                    default:
                        break;
                }
                packet.add(mover.getName());
                prevLocation.addData(mover, packet);
            }

            this.unitMovement.clear();
        }

        synchronized (this.updatedLocations)
        {
            for (Location location : this.updatedLocations)
                location.update(time);
            this.updatedLocations.clear();
        }
    }
}

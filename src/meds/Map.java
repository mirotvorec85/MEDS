package meds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import meds.database.Hibernate;
import meds.enums.MovementDirections;
import meds.logging.Logging;

import org.hibernate.Session;

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
        this.kingdoms = new HashMap<Integer, Kingdom>();
        this.regions = new HashMap<Integer, Region>();
        this.locations = new HashMap<Integer, Location>();
        this.updatedLocations = new HashSet<Location>();
        this.shops = new HashMap<Integer, Shop>();
        this.unitMovement = new HashMap<Unit, MovementDirections>();

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

        List<Region> regions = session.createCriteria(Region.class).list();
        for (Region region : regions)
        {
            this.regions.put(region.getId(), region);
        }
        Logging.Info.log("Loaded " + this.regions.size() + " regions");

        List<Location> locations = session.createCriteria(Location.class).list();
        for (Location location : locations)
        {
            this.locations.put(location.getId(), location);
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

                // Does neighbour location exist?
                Location location = mover.getPosition().getNeighbourLocation(entry.getValue());
                if (location == null)
                    continue;

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

                mover.getPosition().addData(mover, packet);

                mover.setPosition(location);
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

package org.meds.map;

import java.util.*;

import org.meds.*;
import org.meds.Item.Prototype;
import org.meds.enums.MovementDirections;
import org.meds.enums.Parameters;
import org.meds.enums.SpecialLocationTypes;
import org.meds.net.ServerOpcodes;
import org.meds.net.ServerPacket;
import org.meds.util.Random;

public class Location
{
    private int id;
    private String title;
    private int topId;
    private int bottomId;
    private int northId;
    private int southId;
    private int westId;
    private int eastId;
    private int xCoord;
    private int yCoord;
    private int zCoord;
    private int regionId;
    private SpecialLocationTypes specialLocationType;
    private boolean safeZone;
    private int keeperType;
    private String keeperName;
    private int specialLocationId;
    private int pictureId;
    private boolean square;
    private boolean safeRegion;
    private int pictureTime;
    private int keeperTime;

    private Set<Unit> units;
    private Set<Unit> unitsView;
    private java.util.Map<Integer, Corpse> corpses;
    private java.util.Map<Prototype, Item> items;
    private Region region;

    private ServerPacket locationData;
    private ServerPacket locationInfoData;
    private ServerPacket neighborsInfoData;

    /**
     * Indicating whether the location will be updated at the next tick. Use a Setter for setting a value for this field!
     */
    private boolean updatable;

    /**
     * Reference to a unit which visual update cause location update request
     */
    private Unit updatedUnit;

    public Location()
    {
        this.units = new HashSet<Unit>();
        this.unitsView = Collections.unmodifiableSet(this.units);
        this.corpses = new HashMap<Integer, Corpse>();
        this.items = new HashMap<Prototype, Item>();
    }

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public int getTopId()
    {
        return topId;
    }
    public void setTopId(int topId)
    {
        this.topId = topId;
    }
    public int getBottomId()
    {
        return bottomId;
    }
    public void setBottomId(int bottomId)
    {
        this.bottomId = bottomId;
    }
    public int getNorthId()
    {
        return northId;
    }
    public void setNorthId(int northId)
    {
        this.northId = northId;
    }
    public int getSouthId()
    {
        return southId;
    }
    public void setSouthId(int southId)
    {
        this.southId = southId;
    }
    public int getWestId()
    {
        return westId;
    }
    public void setWestId(int westId)
    {
        this.westId = westId;
    }
    public int getEastId()
    {
        return eastId;
    }
    public void setEastId(int eastId)
    {
        this.eastId = eastId;
    }
    public int getxCoord()
    {
        return xCoord;
    }
    public void setxCoord(int xCoord)
    {
        this.xCoord = xCoord;
    }
    public int getyCoord()
    {
        return yCoord;
    }
    public void setyCoord(int yCoord)
    {
        this.yCoord = yCoord;
    }
    public int getzCoord()
    {
        return zCoord;
    }
    public void setzCoord(int zCoord)
    {
        this.zCoord = zCoord;
    }
    public int getRegionId()
    {
        return regionId;
    }
    public void setRegionId(int regionId)
    {
        this.regionId = regionId;
        this.region = Map.getInstance().getRegion(regionId);
    }
    public Region getRegion()
    {
        return this.region;
    }
    public int getSpecialLocationTypeInt()
    {
        return specialLocationType.getValue();
    }
    public void setSpecialLocationTypeInt(int specialLocationType)
    {
        this.specialLocationType = SpecialLocationTypes.parse(specialLocationType);
    }
    public SpecialLocationTypes getSpecialLocationType()
    {
        return this.specialLocationType;
    }
    public boolean isSafeZone()
    {
        return safeZone;
    }
    public void setSafeZone(boolean safeZone)
    {
        this.safeZone = safeZone;
    }
    public int getKeeperType()
    {
        return keeperType;
    }
    public void setKeeperType(int keeperType)
    {
        this.keeperType = keeperType;
    }
    public String getKeeperName()
    {
        return keeperName;
    }
    public void setKeeperName(String keeperName)
    {
        this.keeperName = keeperName;
    }
    public int getSpecialLocationId()
    {
        return specialLocationId;
    }
    public void setSpecialLocationId(int specialLocationId)
    {
        this.specialLocationId = specialLocationId;
    }
    public int getPictureId()
    {
        return pictureId;
    }
    public void setPictureId(int pictureId)
    {
        this.pictureId = pictureId;
    }
    public boolean isSquare()
    {
        return square;
    }
    public void setSquare(boolean square)
    {
        this.square = square;
    }
    public boolean isSafeRegion()
    {
        return safeRegion;
    }
    public void setSafeRegion(boolean safeRegion)
    {
        this.safeRegion = safeRegion;
    }
    public int getPictureTime()
    {
        return pictureTime;
    }
    public void setPictureTime(int pictureTime)
    {
        this.pictureTime = pictureTime;
    }
    public int getKeeperTime()
    {
        return keeperTime;
    }
    public void setKeeperTime(int keeperTime)
    {
        this.keeperTime = keeperTime;
    }

    private void setUpdatable(boolean updatable)
    {
        if (this.updatable == updatable)
            return;
        this.updatable = updatable;
        if (updatable)
            Map.getInstance().addLocationUpdate(this);
    }

    public Location getRandomNeighbour()
    {
        return this.getRandomNeighbour(true, false);
    }

    public Location getRandomNeighbour(boolean includeSpecial)
    {
        return this.getRandomNeighbour(includeSpecial, false);
    }

    public Location getRandomNeighbour(boolean includeSpecial, boolean stayInRegion)
    {
        return getNeighbourLocation(getRandomDirection(includeSpecial, stayInRegion));
    }

    public MovementDirections getRandomDirection()
    {
        return this.getRandomDirection(true, false);
    }

    public MovementDirections getRandomDirection(boolean includeSpecial)
    {
        return this.getRandomDirection(includeSpecial, false);
    }

    public MovementDirections getRandomDirection(boolean includeSpecial, boolean stayInRegion)
    {
        int startIndex = Random.nextInt(MovementDirections.Up.getValue(), MovementDirections.West.getValue() + 1);
        // Increase or Decrease the index
        int bypassDirection = Random.nextInt(0, 2) == 0 ? -1 : 1;
        int index = startIndex;
        Location location;
        do
        {
            location = getNeighbourLocation(MovementDirections.parse(index));
            // Neighbour exists and it is not a special location.
            if (location != null && (includeSpecial || !location.safeZone) && (!stayInRegion || location.region == this.region))
                return MovementDirections.parse(index);
            index += bypassDirection;
            // Keep boundaries
            if (index < 0)
                index = 5;
            else if (index > 5)
                index = 0;
        } while (index != startIndex);

        return MovementDirections.None;
    }

    public Location getNeighbourLocation(MovementDirections direction)
    {
        int neighbourId;
        switch (direction)
        {
            case Up:
                neighbourId = this.topId;
                break;
            case Down:
                neighbourId = this.bottomId;
                break;
            case North:
                neighbourId = this.northId;
                break;
            case South:
                neighbourId = this.southId;
                break;
            case West:
                neighbourId = this.westId;
                break;
            case East:
                neighbourId = this.eastId;
                break;
            default:
                return null;
        }

        return Map.getInstance().getLocation(neighbourId);
    }


    /**
     * Gets the shorter location info with ServerOpcodes.LocationInfo
     */
    public ServerPacket getInfoData()
    {
        if (this.locationInfoData == null)
        {
            this.locationInfoData =  new ServerPacket(ServerOpcodes.LocationInfo)
                .add(this.id)
                .add(this.title)
                .add(this.topId)
                .add(this.bottomId)
                .add(this.northId)
                .add(this.southId)
                .add(this.westId)
                .add(this.eastId)
                .add(this.xCoord)
                .add(this.yCoord)
                .add(this.zCoord)
                .add(this.specialLocationType)
                .add(this.square ? "1" : "0")
                .add(this.region.getName())
                .add(this.region.getKingdom().getName())
                .add("Continent")
                .add(this.regionId)
                .add("0"); // TODO: Determine the source of this value.
        }
        return this.locationInfoData;
    }

    public ServerPacket getNeighborsInfoData()
    {
        if (this.neighborsInfoData == null)
        {
            this.neighborsInfoData = new ServerPacket();
            Location neighbor;

            for (MovementDirections direction : MovementDirections.values())
            {
                if ((neighbor = getNeighbourLocation(direction)) != null)
                {
                    this.neighborsInfoData.add(neighbor.getInfoData());
                }
            }
        }
        return this.neighborsInfoData;
    }

    public ServerPacket getData()
    {
        if (this.locationData == null)
        {
            this.locationData = new ServerPacket(ServerOpcodes.Location)
                .add(this.id)
                .add(this.title)
                .add(this.topId)
                .add(this.bottomId)
                .add(this.northId)
                .add(this.southId)
                .add(this.westId)
                .add(this.eastId)
                .add(this.xCoord)
                .add(this.yCoord)
                .add(this.zCoord)
                .add("Continent")
                .add(this.region.getKingdom().getName())
                .add(this.region.getName())
                .add(this.specialLocationType)
                .add(this.safeZone ? "true" : "false")
                .add(this.keeperType)
                .add(this.keeperName)
                .add(this.specialLocationId)
                .add(this.pictureId)
                .add(this.square ? "1" : "0")
                .add(this.safeRegion ? "1" : "0")
                .add(this.pictureTime)
                .add(this.keeperTime)
                .add(this.regionId)
                .add("0"); // TODO: Determine the source of this value.
        }
        return this.locationData;
    }

    /**
     * Gets a value indicating whether this location doesn't contain any units.
     */
    public boolean isEmpty()
    {
        return this.units.isEmpty();
    }

    public Set<Unit> getUnits()
    {
        return this.unitsView;
    }

    public void addCorpse(Corpse corpse)
    {
        this.corpses.put(corpse.getGuid(), corpse);
        addData(getCorpseData());
    }

    /**
     * Gets a Corpse instance with the specified GUID value.
     * @param corpseGuid Corpse GUID to find.
     * @return Reference to a Corpse class instance.
     */
    public Corpse getCorpse(int corpseGuid)
    {
        return this.corpses.get(corpseGuid);
    }

    public void addItem(Item item)
    {
        Item _item = this.items.get(item.getPrototype());
        if (_item != null)
        {
            _item.tryStackItem(item);
        }
        else
            this.items.put(item.getPrototype(), item);
        addData(getCorpseData());
    }

    public Item getItem(Prototype proto)
    {
        return this.items.get(proto);
    }

    public void removeItem(Item item)
    {
        if (this.items.remove(item.getPrototype()) != null)
            addData(getCorpseData());
    }

    public void removeCorpse(Corpse corpse)
    {
        if (this.corpses.remove(corpse.getGuid()) != null)
            addData(getCorpseData());
    }

    public void unitEntered(Unit unit)
    {
        this.units.add(unit);
        if (unit.getUnitType() == UnitTypes.Player)
        {
            Player player = (Player)unit;
            if (player.getSession() != null)
            {
                player.getSession().addData(getData());
                player.getSession().addData(getNeighborsInfoData());
                player.getSession().addData(getCorpseData());
            }
        }

        setUpdatable(true);
    }

    public void unitLeft(Unit unit)
    {
        if (!this.units.remove(unit))
            return;
        setUpdatable(true);
    }

    public void unitVisualChanged(Unit unit)
    {
        this.setUpdatable(true);
        if (!this.updatable)
        {
            this.updatedUnit = unit;
        }
        // Will be updated already
        else
        {
            // updatable == true AND updatedUnit == null means that pss opcode should be sent to all units
            if (this.updatedUnit == null)
                return;

            // Exclude double unit changing as the same unit
            if (this.updatedUnit != unit)
                this.updatedUnit = null;
        }
    }

    /**
     * Adds the specified packet data to all players at this location.
     */
    public void addData(ServerPacket packet)
    {
        this.send(null, null, packet, true);
    }

    /**
     * Adds the specified packet data to all players at this location
     * except the specified unit (but this unit should be a Player class instance).
     */
    public void addData(Unit exception, ServerPacket packet)
    {
        this.send(exception, null, packet, true);
    }

    /**
     * Adds the specified packet data to all players at this location
     * except two specified units (but these units should be a Player class instances).
     */
    public void addData(Unit exception1, Unit exception2, ServerPacket packet)
    {
        this.send(exception1, exception2, packet, true);
    }

    /**
     * Sends a packet buffer of all players at this location.
     */
    public void send()
    {
        for (Unit unit : this.units)
        {
            if (unit.getUnitType() == UnitTypes.Player)
            {
                Player player = (Player)unit;

                if (player.getSession() == null)
                    continue;
                player.getSession().send();
            }
        }
    }

    /**
     * Sends the specified packet data to all players at this location
     * except the specified unit (but this unit should be a Player class instance).
     */
    public void send(Unit exception, ServerPacket packet)
    {
        this.send(exception, null, packet, false);
    }

    /**
     * Sends specified packet data to all players at this location
     * except two specified units (but these units should be a Player class instances).
     */
    public void send(Unit exception1, Unit exception2, ServerPacket packet)
    {
        this.send(exception1, exception2, packet, false);
    }

    private void send(Unit exception1, Unit exception2, ServerPacket packet, boolean add)
    {
        for (Unit unit : this.units)
            if (unit.getUnitType() == UnitTypes.Player)
            {
                Player pl = (Player)unit;
                // Except Player
                if (pl == exception1 || pl == exception2)
                    continue;

                if (pl.getSession() == null)
                    continue;
                if (add)
                    pl.getSession().addData(packet);
                else
                    pl.getSession().send(packet);
            }
    }

    public ServerPacket getCorpseData()
    {
        ServerPacket packet = new ServerPacket(ServerOpcodes.CorpseList);
        packet.add(this.corpses.size() + this.items.size());
        // Corpses
        for (Corpse corpse : this.corpses.values())
        {
            packet.add(corpse.getGuid())
                .add(corpse.getOwner().getUnitType() == UnitTypes.Player ? "user" : "npc")
                .add(corpse.getOwner().getName());
        }
        for (Item item : this.items.values())
        {
            packet.add(item.Template.getId())
                .add(item.getModification().getValue())
                .add(item.getDurability())
                .add(item.getCount());
        }
        return packet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Location location = (Location) o;

        return this.id == location.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    public void update(int time)
    {
        this.updatable = false;

        if (this.units.size() == 0)
        {
            this.updatedUnit = null;
            return;
        }

        for (Unit unit : this.units)
        {
            // The next part is for players only
            if (unit.getUnitType() != UnitTypes.Player)
                continue;

            Player player = (Player)unit;

            // Send new Units list
            // At least 1 unit should be changed
            // and this unit should not be an updatable unit
            if (this.updatedUnit == player)
                continue;
            if (player.getSession() == null)
                continue;
            ServerPacket pss = new ServerPacket(ServerOpcodes.PositionUnitList);
            pss.add(this.units.size() - 1); // exclude itself

            for (Unit _unit : this.units)
            {
                if (_unit == unit)
                    continue;

                pss.add(_unit.getName());
                pss.add(_unit.getGuid());
                pss.add(_unit.getAvatar());
                pss.add((int)(73d *_unit.getHealth() / _unit.getParameters().value(Parameters.Health)));
                pss.add(_unit.getLevel());
                // TODO: next values is not magic numbers
                pss.add("0").add("0").add("0").add("0");
                pss.add(_unit.getTarget() == null ? 0 : _unit.getTarget().getGuid());
                pss.add("1212413397");
                pss.add((int)(73d *_unit.getMana() / _unit.getParameters().value(Parameters.Mana)));
                pss.add("0").add("0").add("0").add("0");
                pss.add("1").add("0").add("0").add("0");
            }

            player.getSession().addData(pss);
        }


        this.updatedUnit = null;
    }
}

package org.meds.map;

import org.meds.*;
import org.meds.enums.MovementDirections;
import org.meds.enums.Parameters;
import org.meds.enums.SpecialLocationTypes;
import org.meds.item.Item;
import org.meds.item.ItemPrototype;
import org.meds.item.ItemUtils;
import org.meds.net.ServerCommands;
import org.meds.net.ServerPacket;
import org.meds.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component // Possibly it shouldn't be a bean
@Scope("prototype")
public class Location {

    @Autowired
    private MapManager mapManager;

    private org.meds.data.domain.Location entry;

    private Set<Unit> units;
    private Set<Unit> unitsView;
    private java.util.Map<Integer, Corpse> corpses;
    private java.util.Map<ItemPrototype, Item> items;
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

    public Location(org.meds.data.domain.Location entry, Region region) {
        this.entry = entry;
        this.region = region;
        this.units = new HashSet<>();
        this.unitsView = Collections.unmodifiableSet(this.units);
        this.corpses = new HashMap<>();
        this.items = new HashMap<>();
    }

    public int getId() {
        return this.entry.getId();
    }

    public Region getRegion() {
        return region;
    }

    public boolean isSafeZone() {
        return this.entry.isSafeZone();
    }

    public SpecialLocationTypes getSpecialLocationType() {
        return this.entry.getSpecialLocationType();
    }

    public int getSpecialLocationId() {
        return this.entry.getSpecialLocationId();
    }

    public String getTitle() {
        return this.entry.getTitle();
    }

    private void setUpdatable(boolean updatable) {
        if (this.updatable == updatable) {
            return;
        }
        this.updatable = updatable;
        if (updatable) {
            mapManager.addLocationUpdate(this);
        }
    }

    public Location getRandomNeighbour() {
        return this.getRandomNeighbour(true, false);
    }

    public Location getRandomNeighbour(boolean includeSpecial) {
        return this.getRandomNeighbour(includeSpecial, false);
    }

    public Location getRandomNeighbour(boolean includeSpecial, boolean stayInRegion) {
        return getNeighbourLocation(getRandomDirection(includeSpecial, stayInRegion));
    }

    public MovementDirections getRandomDirection() {
        return this.getRandomDirection(true, false);
    }

    public MovementDirections getRandomDirection(boolean includeSpecial) {
        return this.getRandomDirection(includeSpecial, false);
    }

    public MovementDirections getRandomDirection(boolean includeSpecial, boolean stayInRegion) {
        int startIndex = Random.nextInt(MovementDirections.Up.getValue(), MovementDirections.West.getValue() + 1);
        // Increase or Decrease the index
        int bypassDirection = Random.nextInt(0, 2) == 0 ? -1 : 1;
        int index = startIndex;
        Location location;
        do {
            location = getNeighbourLocation(MovementDirections.parse(index));
            // Neighbour exists and it is not a special location.
            if (location != null && (includeSpecial || !location.entry.isSafeZone()) &&
                    (!stayInRegion || location.region == this.region)) {
                return MovementDirections.parse(index);
            }
            index += bypassDirection;
            // Keep boundaries
            if (index < 0) {
                index = 5;
            } else if (index > 5) {
                index = 0;
            }
        } while (index != startIndex);

        return MovementDirections.None;
    }

    public Location getNeighbourLocation(MovementDirections direction) {
        int neighbourId;
        switch (direction) {
            case Up:
                neighbourId = entry.getTopId();
                break;
            case Down:
                neighbourId = entry.getBottomId();
                break;
            case North:
                neighbourId = entry.getNorthId();
                break;
            case South:
                neighbourId = entry.getSouthId();
                break;
            case West:
                neighbourId = entry.getWestId();
                break;
            case East:
                neighbourId = entry.getEastId();
                break;
            default:
                return null;
        }

        return mapManager.getLocation(neighbourId);
    }


    /**
     * Gets the shorter location info with ServerCommands.LocationInfo
     */
    public ServerPacket getInfoData() {
        if (this.locationInfoData == null) {
            this.locationInfoData =  new ServerPacket(ServerCommands.LocationInfo)
                .add(this.entry.getId())
                .add(this.entry.getTitle())
                .add(this.entry.getTopId())
                .add(this.entry.getBottomId())
                .add(this.entry.getNorthId())
                .add(this.entry.getSouthId())
                .add(this.entry.getWestId())
                .add(this.entry.getEastId())
                .add(this.entry.getxCoord())
                .add(this.entry.getyCoord())
                .add(this.entry.getzCoord())
                .add(this.entry.getSpecialLocationType())
                .add(this.entry.isSquare() ? "1" : "0")
                .add(this.region.getName())
                .add(this.region.getKingdom().getEntry().getName())
                .add("Continent")
                .add(this.entry.getRegionId())
                .add("0"); // TODO: Determine the source of this value.
        }
        return this.locationInfoData;
    }

    public ServerPacket getNeighborsInfoData() {
        if (this.neighborsInfoData == null) {
            this.neighborsInfoData = new ServerPacket();
            Location neighbor;

            for (MovementDirections direction : MovementDirections.values()) {
                if ((neighbor = getNeighbourLocation(direction)) != null) {
                    this.neighborsInfoData.add(neighbor.getInfoData());
                }
            }
        }
        return this.neighborsInfoData;
    }

    public ServerPacket getData() {
        if (this.locationData == null) {
            this.locationData = new ServerPacket(ServerCommands.Location)
                .add(this.entry.getId())
                .add(this.entry.getTitle())
                .add(this.entry.getTopId())
                .add(this.entry.getBottomId())
                .add(this.entry.getNorthId())
                .add(this.entry.getSouthId())
                .add(this.entry.getWestId())
                .add(this.entry.getEastId())
                .add(this.entry.getxCoord())
                .add(this.entry.getyCoord())
                .add(this.entry.getzCoord())
                .add("Continent") // TODO: Implement continents
                .add(this.region.getKingdom().getEntry().getName())
                .add(this.region.getName())
                .add(this.entry.getSpecialLocationType())
                .add(this.entry.isSafeZone() ? "true" : "false")
                .add(this.entry.getKeeperType())
                .add(this.entry.getKeeperName())
                .add(this.entry.getSpecialLocationId())
                .add(this.entry.getPictureId())
                .add(this.entry.isSquare() ? "1" : "0")
                .add(this.entry.isSafeRegion() ? "1" : "0")
                .add(this.entry.getPictureTime())
                .add(this.entry.getKeeperTime())
                .add(this.entry.getRegionId())
                .add("0"); // TODO: Determine the source of this value.
        }
        return this.locationData;
    }

    /**
     * Gets a value indicating whether this location doesn't contain any units.
     */
    public boolean isEmpty() {
        return this.units.isEmpty();
    }

    public Set<Unit> getUnits() {
        return this.unitsView;
    }

    public void addCorpse(Corpse corpse) {
        this.corpses.put(corpse.getId(), corpse);
        send(getCorpseData());
    }

    /**
     * Gets a Corpse instance with the specified ID value.
     * @param corpseId Corpse ID to find.
     * @return Reference to a Corpse class instance.
     */
    public Corpse getCorpse(int corpseId) {
        return this.corpses.get(corpseId);
    }

    public void addItem(Item item) {
        Item _item = this.items.get(item.getPrototype());
        if (_item != null && ItemUtils.areStackable(_item, item)) {
            _item.stackItem(item);
        } else {
            this.items.put(item.getPrototype(), item);
        }
        // Synchronize Items/Corpses data
        send(getCorpseData());
    }

    public Item getItem(ItemPrototype proto) {
        return this.items.get(proto);
    }

    public void removeItem(Item item) {
        if (this.items.remove(item.getPrototype()) != null) {
            send(getCorpseData());
        }
    }

    public void removeCorpse(Corpse corpse) {
        if (this.corpses.remove(corpse.getId()) != null) {
            send(getCorpseData());
        }
    }

    public void unitEntered(Unit unit) {
        synchronized (this.units) {
            this.units.add(unit);
        }
        if (unit.getUnitType() == UnitTypes.Player) {
            Player player = (Player)unit;
            if (player.getSession() != null) {
                player.getSession().send(getData());
                player.getSession().send(getNeighborsInfoData());
                player.getSession().send(getCorpseData());
            }
        }

        setUpdatable(true);
    }

    public void unitLeft(Unit unit) {
        synchronized (this.units) {
            if (!this.units.remove(unit)) {
                return;
            }
        }
        setUpdatable(true);
    }

    public void unitVisualChanged(Unit unit) {
        this.setUpdatable(true);
        if (!this.updatable) {
            this.updatedUnit = unit;
        }
        // Will be updated already
        else {
            // updatable == true AND updatedUnit == null means that "pss" command should be sent to all units
            if (this.updatedUnit == null) {
                return;
            }

            // Exclude double unit changing as the same unit
            if (this.updatedUnit != unit) {
                this.updatedUnit = null;
            }
        }
    }

    /**
     * Sends the specified packet data to all players at this location
     */
    public void send(ServerPacket packet) {
        this.send(null, null, packet);
    }

    /**
     * Sends the specified packet data to all players at this location
     * except the specified unit (but this unit should be a Player class instance).
     */
    public void send(Unit exception, ServerPacket packet) {
        this.send(exception, null, packet);
    }

    public void send(Unit exception1, Unit exception2, ServerPacket packet) {
        for (Unit unit : this.units) {
            if (unit.getUnitType() == UnitTypes.Player) {
                Player pl = (Player) unit;
                // Except Player
                if (pl == exception1 || pl == exception2) {
                    continue;
                }

                if (pl.getSession() == null) {
                    continue;
                }

                pl.getSession().send(packet);
            }
        }
    }

    public ServerPacket getCorpseData() {
        ServerPacket packet = new ServerPacket(ServerCommands.CorpseList);
        packet.add(this.corpses.size() + this.items.size());
        // Corpses
        for (Corpse corpse : this.corpses.values()) {
            packet.add(corpse.getId())
                .add(corpse.getOwner().getUnitType() == UnitTypes.Player ? "user" : "npc")
                .add(corpse.getOwner().getName());
        }
        for (Item item : this.items.values()) {
            packet.add(item.getTemplate().getId())
                .add(item.getModification().getValue())
                .add(item.getDurability())
                .add(item.getCount());
        }
        return packet;
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>(this.units.size());
        synchronized (this.units) {
            for (Unit unit : this.units) {
                if (unit.getUnitType() == UnitTypes.Player) {
                    players.add((Player) unit);
                }
            }
        }
        return players;
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

        return this.entry.getId() == location.entry.getId();
    }

    @Override
    public int hashCode() {
        return this.entry.hashCode();
    }

    public void update(int time) {
        this.updatable = false;

        if (this.units.size() == 0) {
            this.updatedUnit = null;
            return;
        }

        synchronized (this.units) {
            for (Unit unit : this.units) {
                // The next part is for players only
                if (unit.getUnitType() != UnitTypes.Player) continue;

                Player player = (Player) unit;

                // Send new Units list
                // At least 1 unit should be changed
                // and this unit should not be an updatable unit
                if (this.updatedUnit == player) continue;
                if (player.getSession() == null) continue;
                ServerPacket pss = new ServerPacket(ServerCommands.PositionUnitList);
                pss.add(this.units.size() - 1); // exclude itself

                for (Unit _unit : this.units) {
                    if (_unit == unit) continue;

                    pss.add(_unit.getName());
                    pss.add(_unit.getId());
                    pss.add(_unit.getAvatar());
                    pss.add((int) (73d * _unit.getHealth() / _unit.getParameters().value(Parameters.Health)));
                    pss.add(_unit.getLevel());
                    pss.add(_unit.getReligion());
                    pss.add(_unit.getReligLevel());
                    if (!_unit.isPlayer() || ((Player) _unit).getGroup() == null) {
                        pss.add("0"); // Is a Group Leader
                        pss.add("0"); // Group leader ID
                    } else {
                        Group group = ((Player) _unit).getGroup();
                        pss.add(group.getLeader() == _unit ? "1" : "0");
                        pss.add(group.getLeader().getId());
                    }
                    pss.add(_unit.getTarget() == null ? 0 : _unit.getTarget().getId());
                    pss.add("1212413397"); // Avatar Time?
                    pss.add((int) (73d * _unit.getMana() / _unit.getParameters().value(Parameters.Mana)));
                    pss.add("0"); // Clan ID
                    pss.add("0"); // Clan Status
                    pss.add("0"); // is hidden
                    pss.add("0"); // is Out of law
                    pss.add("1"); // Gender
                    pss.add("0"); // Title
                    pss.add("0"); // is a Pet
                    // Boss Type
                    if (_unit.getUnitType() == UnitTypes.Creature) {
                        pss.add(((Creature) _unit).getBossType());
                    } else {
                        pss.add("0");
                    }
                }

                player.getSession().send(pss);
            }
        }

        this.updatedUnit = null;
    }
}

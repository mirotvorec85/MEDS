package org.meds;

import java.util.HashSet;
import java.util.Set;

import org.meds.logging.Logging;
import org.meds.map.Location;
import org.meds.map.Map;

public class Corpse
{
    private int guid;
    private Set<Item> loot;
    private int gold;
    private Unit owner;
    private Location position;

    public Corpse(Unit owner)
    {
        Logging.Debug.log("Create corpse for " + owner.getName());
        this.owner = owner;
        this.guid = Map.getInstance().getNextCorpseGuid();
        setPosition(owner.getPosition());
        this.loot = new HashSet<>();
    }

    public void fillWithLoot(Set<Item> items, int gold)
    {
        this.loot = items;
        this.gold = gold;
    }

    public Set<Item> getItems()
    {
        return this.loot;
    }

    public Location getPosition()
    {
        return this.position;
    }

    private void setPosition(Location location)
    {
        if (location == null && this.position != null)
            this.position.removeCorpse(this);
        else if (location != null)
            location.addCorpse(this);
        this.position = location;
    }

    public int getGold()
    {
        return this.gold;
    }

    public Unit getOwner()
    {
        return this.owner;
    }

    public int getGuid()
    {
        return this.guid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Corpse corpse = (Corpse) o;

        return this.guid == corpse.guid;
    }

    @Override
    public int hashCode() {
        return this.guid;
    }
}

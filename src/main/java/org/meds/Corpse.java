package org.meds;

import java.util.HashSet;
import java.util.Set;

import org.meds.logging.Logging;

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
        this.loot = new HashSet<Item>();
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
}

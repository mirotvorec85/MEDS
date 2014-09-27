package meds;

import java.util.HashSet;
import java.util.Set;

import meds.logging.Logging;

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
        this.position = this.owner.getPosition();
        this.guid = Map.getInstance().getNextCorpseGuid();
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

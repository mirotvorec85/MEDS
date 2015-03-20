package org.meds;

import java.util.*;

import org.meds.database.DBStorage;
import org.meds.database.entity.CreatureLoot;
import org.meds.database.entity.CreatureTemplate;
import org.meds.enums.CreatureFlags;
import org.meds.enums.CreatureTypes;
import org.meds.enums.MovementDirections;
import org.meds.enums.Parameters;
import org.meds.logging.Logging;
import org.meds.map.Location;
import org.meds.util.Random;

public class Creature extends Unit
{
    /**
     * Creature Respawn time after death. In milliseconds.
     */
    public final static int RespawnTime = 60000; // TODO: Determine the exact values of respawn timer

    private Set<Item> loot;
    private int cashGold;

    private int respawnTimer;

    private Location spawnLocation;

    private int templateId;
    private CreatureTemplate template;
    private int locationId;
    private int autoSpell;

    private String fullName;

    private int maxGoldValue;
    private int minGoldValue;

    private Map<Integer, Integer> spells;
    private Map<Integer, Integer> skills;

    public Creature()
    {
        super();
        this.unitType = UnitTypes.Creature;
        this.spells = new HashMap<>();
        this.skills = new HashMap<>();
        this.loot = new HashSet<>();
    }

    @SuppressWarnings("unused")
    private void setGuid(int guid)
    {
        this.guid = guid;
    }

    public int getTemplateId()
    {
        return this.templateId;
    }

    public CreatureTemplate getTemplate()
    {
        return this.template;
    }

    @SuppressWarnings("unused")
    private void setTemplateId(int templateId)
    {
        this.templateId = templateId;
    }
    public int getLocationId()
    {
        return this.locationId;
    }

    @SuppressWarnings("unused")
    private void setLocationId(Integer locationId)
    {
        if (locationId == null)
            this.locationId = 0;
        else
        {
            this.locationId = locationId;
            this.spawnLocation = org.meds.map.Map.getInstance().getLocation(locationId);
        }
    }

    @Override
    public String getName() {
        return this.fullName;
    }

    @Override
    public int getLevel()
    {
        return this.template.getLevel();
    }

    @Override
    public int getReligLevel()
    {
        return 0;
    }

    @Override
    public int getAvatar()
    {
        return this.template.getAvatarId();
    }

    @Override
    public int getAutoSpell()
    {
        return this.autoSpell;
    }

    @Override
    public int getSkillLevel(int skillId)
    {
        Integer level = this.skills.get(skillId);
        if (level == null)
            return 0;
        return level;
    }

    @Override
    public int getSpellLevel(int spellId)
    {
        Integer level = this.spells.get(spellId);
        if (level == null)
            return 0;
        return level;
    }

    public int getCashGold() {
        return this.cashGold;
    }

    public int getMaxGoldValue() {
        return this.maxGoldValue;
    }

    public int getMinGoldValue() {
        return this.minGoldValue;
    }

    public boolean locationBound()
    {
        return this.spawnLocation != null;
    }

    @Override
    public int create()
    {
        this.template = DBStorage.CreatureTemplateStore.get(this.templateId);

        if (this.template == null)
            return 0;

        /*
         * CREATURE STATS:
         *
         * Base stats (Constitution, Strength, Dexterity, Intelligence):
         * 8 + Level / 6 => Floor
         *
         * Health:
         * Level * Level / 4 => Floor (Under 70 Level)
         * Level * Level / 3 => Floor (Above 70 Level)
         *
         * Mana: ???
         * Level * Level * Level * 0.000105 + Level * Level * 0.025 + Level * 0.3 + 40
         *
         * Health Regeneration: ???
         * Level * Level *0.012 + Level * 0.1 + 4 => Floor (Under 70 Level)
         * Level * Level *0.016 + Level * 0.1 + 4 => Floor (Under 70 Level)
         *
         * Mana Regeneration ????
         * Level * Level * Level * 0.000013 + Level * Level * 0.002 + Level * 0.1 + 7
         *
         * MinDamage:
         * Level * Level * 0.015  + Level - 12 => Floor
         *
         * MaxDamage:
         * Level * Level * 0.03 + Level * 12 - 12 => Floor
         *
         * Magic Damage:
         * Level * Level * 0.0226 + Level + 1 => Floor
         *
         * Protection:
         * Level * Level * 0.001 + Level / 22 - 11
         *
         * Armour:
         *   Level * Level * 0.03 + 3 * Level => Floor
         * - Religious (Sun, Moon, Order, Chaos)
         *   x 1
         * - Monk, Mauler, Barbarian, Swordsman, Vindicator, Assassin, Duelist
         *   x 2
         * - Elemental (Cold, Fire, Lightning)
         *   x 0.5
         *
         * Chance To Hit:
         *   Level * Level * 0.0075 + Level * 0.75 => Ceiling
         *
         * Chance To Cast:
         *   Level * Level * 0.0075 + Level * 0.75 => Ceiling
         * - Swordsman, Mauler, Barbarian
         *   x 0.5
         *
         * Resistance
         *   Level * Level * 0.03 + Level * 3
         * - Elemental
         *   x 2
         * - Vindicator, Monk, Assassin, Duelist
         *   x 0.5
         *
         * */

        int level = getLevel();

        this.parameters.base().value(Parameters.Constitution, 8 + level / 6);
        this.parameters.base().value(Parameters.Strength, 8 + level / 6);
        this.parameters.base().value(Parameters.Dexterity, 8 + level / 6);
        this.parameters.base().value(Parameters.Intelligence, 8 + level / 6);


        this.parameters.guild().value(Parameters.Health, level * level / 4); // level:95 = level /3
        this.parameters.guild().value(Parameters.Mana, (int)(level * level * level * 0.000105 + level * level * 0.025 + level * 0.3 + 40));
        this.parameters.guild().value(Parameters.HealthRegeneration, (int)(level * level  * 0.012 + level * 0.1 + 4));
        this.parameters.guild().value(Parameters.ManaRegeneration, (int)(level * level * level * 0.000013 + level * level * 0.002 + level * 0.1 + 7));
        this.parameters.guild().value(Parameters.MinDamage, level * level / 67 + level - 12);
        this.parameters.guild().value(Parameters.MaxDamage, level * level / 34 + 2 * level - 12);
        this.parameters.guild().value(Parameters.MagicDamage, (int)(level * level * 0.0226 + level + 1));
        this.parameters.guild().value(Parameters.Armour, (int)(level * level * 0.03 + level * 3)); // Fighters *2, Elemental *0.5);
        this.parameters.guild().value(Parameters.Protection, (int)(level * level * 0.001 + level / 22d) - 11);
        this.parameters.guild().value(Parameters.ChanceToHit, (int)(level * level * 0.0075 + level * 0.75));
        this.parameters.guild().value(Parameters.ChanceToCast, (int)(level * level * 0.0075 + level * 0.75));
        this.parameters.guild().value(Parameters.AllResistance, (int)(level * level * 0.03 + level * 3));
        // TODO: estimate and add the other parameters coeffs

        this.health = this.parameters.value(Parameters.Health);
        this.mana = this.parameters.value(Parameters.Mana);

        this.minGoldValue = this.getLevel() / 2; // 50% of the creature level
        this.maxGoldValue = (this.getLevel() + 1) * 3 / 2; // 150% of the creature level

        // TODO: additional parameters, spells and skills according to the creature current type

        this.constructName();

        World.getInstance().unitCreated(this);

        return this.guid;
    }

    /**
     * Spawn the creature at its location(bound or random) with random loot and money account.
     */
    public void spawn()
    {
        // Spawn from death
        if (this.deathState == DeathStates.Dead)
        {
            this.health = this.parameters.value(Parameters.Health);
            this.mana = this.parameters.value(Parameters.Mana);
            this.deathState = DeathStates.Alive;
        }

        Location location;
        if (this.locationBound())
        {
            location = this.spawnLocation;
        }
        else
        {
            // Find random location at creature's region
            location = org.meds.map.Map.getInstance().getRegion(this.template.getRegionId()).getRandomLocation(false);
        }

        if (location == null)
        {
            Logging.Error.log("%s (Entry=%d) was not spawned. Location not found or not specified.", toString(), this.templateId);
            return;
        }

        this.setPosition(location);
        // Add Loot
        this.loot.clear();
        Map<Integer, CreatureLoot> loot = DBStorage.CreatureLootStore.get(this.templateId);
        if (loot != null) {
            for(CreatureLoot creatureLoot : loot.values()) {
                if (Random.nextDouble() * 100 > creatureLoot.getChance())
                    continue;
                Item item = new Item(creatureLoot.getItemTemplateId(), creatureLoot.getCount());
                item.getModification().generateTotemicType();
                this.loot.add(item);
            }
        }

        // Money
        if (!this.template.hasFlag(CreatureFlags.Beast))
            this.cashGold = Random.nextInt(this.minGoldValue, this.maxGoldValue);
    }

    @Override
    public Corpse die()
    {
        Corpse corpse;
        this.deathState = DeathStates.Dead;
        this.respawnTimer = RespawnTime;
        if (this.cashGold == 0 && this.loot.size() == 0)
            corpse = null;
        else
        {
            corpse = new Corpse(this);
            corpse.fillWithLoot(this.loot, this.cashGold);
        }

        this.setPosition(null);
        return corpse;
    }

    private void constructName() {
        this.fullName = this.template.getName();
        CreatureTypes type = World.getInstance().getCreatureType(this.templateId);
        if (type != CreatureTypes.Normal)
            this.fullName += "(" + Locale.getString(type.getTitleStringId()) + ")";
        if (this.template.hasFlag(CreatureFlags.Unique)) {
            this.fullName += "(" + Locale.getString(33) + ")";
        }
    }

    public Iterator<Item> getLootIterator() {
        return this.loot.iterator();
    }

    @Override
    public void update(int time)
    {
        super.update(time);

        switch (this.deathState)
        {
            case Alive:
                if (this.template.hasFlag(CreatureFlags.LocationBind))
                    break;
                if (this.isInCombat())
                    break;
                // 10% chance for motion
                if (Random.nextDouble() < 0.05)
                {
                    MovementDirections direction = this.position.getRandomDirection(false, true);
                    if (direction != MovementDirections.None)
                        org.meds.map.Map.getInstance().registerMovement(this, direction);
                }
                break;
            case Dead:
                if (this.respawnTimer < 0)
                {
                    spawn();
                }
                else
                    this.respawnTimer -= time;
                break;
        }
    }
}

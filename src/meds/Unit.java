package meds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import meds.database.DBStorage;
import meds.database.entity.Spell;
import meds.enums.ClanMemberStatuses;
import meds.enums.Parameters;
import meds.enums.PlayerSettings;
import meds.enums.Races;
import meds.enums.SpecialLocationTypes;
import meds.enums.SpellTypes;
import meds.logging.Logging;
import meds.spell.Aura;
import meds.spell.Aura.States;
import meds.util.KeyValuePair;
import meds.util.Random;

public abstract class Unit
{
    public interface TargetDiedListener
    {
        public void unitTargetDied(Unit unit);
    }

    public class LevelInfo
    {
        private int exp;
        private int religExp;
        private int level;
        private int unk4;
        private int religLevel;

        private int guildLevel;

        public LevelInfo(int level, int exp, int religLevel, int religExp)
        {
            this.level = level;
            this.exp = exp;
            this.religExp = religExp;
            this.religLevel = religLevel;
        }

        private void setExp(int value)
        {
            this.exp = value;
            if (Unit.this.unitType != UnitTypes.Player)
                return;
            Player player = (Player)Unit.this;
            int nextLvlExp = DBStorage.LevelCostStore.get(this.level + 1).getExperience();

            // Is Level Up
            if (nextLvlExp <= this.exp)
            {
                this.exp -= nextLvlExp;
                ++this.level;
                // Send messages
                player.onVisualChanged();
                player.onDisplayChanged();
                if (player.getSession() != null)
                    player.getSession().addServerMessage(497). // You gain a new level
                        addServerMessage(492). // You can learn new lesson in guilds
                        addData(this.getPacketData()).addData(player.getParametersData());
            }
            else
            {
                if (player.getSession() != null)
                    player.getSession().addData(this.getPacketData(true));
            }

        }
        public void addExp(int value)
        {
            this.setExp(this.exp + value);
        }
        public int getExp()
        {
            return this.exp;
        }
        public int getReligExp()
        {
            return this.religExp;
        }
        public int getLevel()
        {
            return this.level;
        }
        public int getReligLevel()
        {
            return this.religLevel;
        }
        public int getGuildLevel()
        {
            return this.guildLevel;
        }

        public ServerPacket getPacketData()
        {
            return this.getPacketData(false);
        }

        public ServerPacket getPacketData(boolean experienceOnly)
        {
            ServerPacket packet = new ServerPacket(ServerOpcodes.Experience);
            packet.add(this.exp);
            packet.add(this.religExp );

            if(!experienceOnly)
            {
                packet.add(this.level);
                packet.add(this.unk4);
                packet.add(this.religLevel);
            }

            return packet;
        }
    }

    protected int guid;
    protected Races race;

    protected UnitTypes unitType;

    protected LevelInfo level;

    protected int clanId;
    protected ClanMemberStatuses clanMemberStatus;

    protected UnitParameters parameters;

    protected int health;
    protected int mana;
    protected int healthManaRegenTact;

    protected KeyValuePair<Spell, Unit> combatSpell;
    protected KeyValuePair<Spell, Unit> effectSpell;

    protected Map<Integer, Aura> auras;

    protected DeathStates deathState;

    protected Map<Integer, Integer> skills;
    protected Map<Integer, Integer> spells;

    protected Unit target;

    /**
     * Current battle of the Unit. Do not set this value manually! Use setter.
     */
    protected Battle battle;

    protected Location position;

    private Set<TargetDiedListener> targetDiedListeners;

    public Unit()
    {
        this.guid = 0;
        this.unitType = UnitTypes.Unit;
        this.race = Races.Generic;
        this.parameters = new UnitParameters(this);
        this.deathState = DeathStates.Alive;
        this.clanId = 0;
        this.clanMemberStatus = ClanMemberStatuses.Private;
        this.combatSpell = null;
        this.effectSpell = null;
        this.auras = new HashMap<Integer, Aura>();
        this.skills = new HashMap<Integer, Integer>();
        this.spells = new HashMap<Integer, Integer>();
        this.level = new LevelInfo(0, 0, 0, 0);
        this.targetDiedListeners = new HashSet<Unit.TargetDiedListener>();
    }

    public void addTargetDiedListener(TargetDiedListener listener)
    {
        this.targetDiedListeners.add(listener);
    }

    public void removeTargetDiedListener(TargetDiedListener listener)
    {
        this.targetDiedListeners.remove(listener);
    }

    public int getGuid()
    {
        return this.guid;
    }

    public abstract int getAutoSpell();

    public abstract String getName();

    public abstract int getAvatar();

    public abstract int getSpellLevel(int spellId);

    public abstract int getSkillLevel(int skillId);

    public UnitTypes getUnitType()
    {
        return this.unitType;
    }

    public Races getRace()
    {
        return this.race;
    }


    public Location getPosition()
    {
        return this.position;
    }

    public void setPosition(Location location)
    {
        Location prevLocation = this.position;
        this.position = location;

        if (prevLocation != null)
            prevLocation.unitLeft(this);
        if (location != null)
            location.unitEntered(this);
        else
            Logging.Debug.log("Unit %s has new NULL position", this.getName());
    }

    public UnitParameters getParameters()
    {
        return this.parameters;
    }

    public int getHealth()
    {
        return this.health;
    }

    public void setHealth(int health)
    {
        this.health = health;
        if (this.isPlayer() && ((Player)this).getSession() != null)
            ((Player)this).getSession().addData(this.getHealthManaData());
        onVisualChanged();
    }

    public void changeHealth(int diff)
    {
        this.setHealth(this.health + diff);
    }

    public int getMana()
    {
        return this.mana;
    }

    public void setMana(int mana)
    {
        this.mana = mana;
        if (this.isPlayer() && ((Player)this).getSession() != null)
            ((Player)this).getSession().addData(this.getHealthManaData());
        onVisualChanged();
    }

    public void changeMana(int diff)
    {
        this.setMana(this.mana + diff);
    }

    public void setHealthMana(int health, int mana)
    {
        this.health = health;
        this.mana = mana;
        if (this.isPlayer() && ((Player)this).getSession() != null)
            ((Player)this).getSession().addData(this.getHealthManaData());
        onVisualChanged();
    }

    public Unit getTarget()
    {
        return this.target;
    }

    public void setTarget(Unit target)
    {
        // Target can be at unit location only
        if (target != null && target.position != this.position)
            target = null;

        // Set Target
        if (target != null)
        {
            // Unit is not in battle currently
            if (this.battle == null)
            {
                // Target is not in battle
                // Create new battle
                if (target.battle == null)
                {
                    this.setBattle(new Battle());
                }
                // Assign target's battle
                else
                    this.setBattle(target.battle);
            }
        }
        // Remove target == leave battle
        else
            this.setBattle(null);

        this.target = target;
    }

    public Battle getBattle()
    {
        return this.battle;
    }

    public void setBattle(Battle battle)
    {
        if (battle == this.battle)
            return;
        if (this.battle != null)
        {
            this.battle.leaveBattle(this);
        }
        if (battle != null)
        {
            battle.enterBattle(this);
        }

        this.battle = battle;
    }

    public int getClanId()
    {
        return this.clanId;
    }

    public ClanMemberStatuses getClanMemberStatus()
    {
        return this.clanMemberStatus;
    }

    public int create()
    {
        return this.guid;
    }

    public LevelInfo getLevel()
    {
        return this.level;
    }

    public boolean isPlayer()
    {
        return this.unitType == UnitTypes.Player;
    }

    public boolean isAlive()
    {
        return this.deathState == DeathStates.Alive;
    }

    public boolean isInCombat()
    {
        return this.battle != null;
    }

    public boolean canMove()
    {
        // TODO: Stan, overloading and other checks
        return this.battle == null;
    }

    public ServerPacket getHealthManaData()
    {
        return new ServerPacket(ServerOpcodes.Health)
        .add(this.health)
        .add(this.mana)
        .add("0");
    }

    /**
     * Applies Protection parameter for reducing incoming damage.
     * @param cleanDamage An initial damage before protection affection.
     * @return The result Damage.
     */
    public int calculateDamageReduction(int cleanDamage)
    {
        int actualDamage = cleanDamage - this.parameters.value(Parameters.Protection);
        return actualDamage < 0 ? 0 : actualDamage;
    }

    public void runAway()
    {
        if (this.battle == null)
            return;

        // Send message "You panic and try to run away"
        meds.Session session;
        if (this.isPlayer() && (session = ((Player)this).getSession()) != null)
        {
            session.addServerMessage(254);
        }
        this.battle.runAway(this);
    }

    public void doBattleAttack()
    {
        if (this.target == null)
            return;

         // Target starts battle with this unit
        if (this.target.getTarget() == null && !this.target.isInCombat())
            this.target.setTarget(this);

        /* Magic */
        if (this.getAutoSpell() != 0)
        {
            int level;
            if ((level = this.getSpellLevel(this.getAutoSpell())) != 0)
            {
                new meds.spell.Spell(this.getAutoSpell(), this, level, this.target).cast();

                // This spell killed a target
                if (this.target == null || !this.target.isAlive())
                {
                    return;
                }
            }
        }

        /* Physical Hit */
        Logging.Debug.log("Unit \"%s\" random damage from %d up to %d", this.getName(), this.parameters.value(Parameters.Damage), this.parameters.value(Parameters.MaxDamage));
        int initialDamage = Random.nextInt(this.parameters.value(Parameters.Damage), this.parameters.value(Parameters.MaxDamage));

        Damage damage = new Damage();
        damage.setInitialDamage(initialDamage);
        damage.IsAutoAttack = true;
        this.dealDamage(this.target, damage);
    }

    public void dealDamage(Unit victim, Damage damage)
    {
        // Auto-attack
        if (damage.IsAutoAttack)
        {
            damage.FinalDamage = victim.calculateDamageReduction(damage.FinalDamage);

            // Chance to Hit
            double hitChance = this.parameters.value(Parameters.ChanceToHit) * 3d / (this.parameters.value(Parameters.ChanceToHit) * 3 + this.target.parameters.value(Parameters.Armour));
            boolean isHit = Random.nextDouble() <= hitChance;

            if (!isHit)
            {
                this.addServerMessage(damage.MessageDealerMiss, victim.getName());
                victim.addServerMessage(damage.MessageVictimMiss, getName());

                this.position.send(this, victim, new ServerPacket(ServerOpcodes.ServerMessage).add(damage.MessagePositionMiss).add(this.getName()).add(victim.getName()));

                return;
            }
        }

        Logging.Debug.log("\"%s\"(%d) deals %d to \"%s\"(%d). Victim's health: %d", this.getName(), this.getGuid(), damage.FinalDamage, victim.getName(), victim.getGuid(), victim.getHealth());

        // No damage
        if (damage.FinalDamage <= 0)
        {
            addServerMessage(damage.MessageDealerNoDamage, victim.getName());
            victim.addServerMessage(damage.MessageVictimNoDamage, getName());

            this.position.send(this, victim, new ServerPacket(ServerOpcodes.ServerMessage).add(damage.MessagePositionNoDamage).add(this.getName()).add(victim.getName()));

            return;
        }

        // Killing blow
        if (victim.getHealth() <= damage.getInitialDamage())
        {
            damage.IsFatal = true;
            addServerMessage(damage.MessageDealerKillingBlow, Integer.toString(damage.FinalDamage), victim.getName());
            victim.addServerMessage(damage.MessageVictimKillingBlow, this.getName(), Integer.toString(damage.FinalDamage));

            this.position.send(this, victim, new ServerPacket(ServerOpcodes.ServerMessage).add(damage.MessagePositionKillingBlow).add(this.getName()).add(damage.FinalDamage).add(victim.getName()));

            Corpse corpse = victim.die();

            /* TODO: KilledUnit event
            if (this.KilledUnit != null)
                this.KilledUnit(this, new KilledUnitEventArgs(victim));
                */
            this.setTarget(null);

            // The killer is a Player
            if (this.unitType == UnitTypes.Player)
            {
                Player player = (Player)this;

                // Reward exp
                int minExp = this.getLevel().getLevel() > 50 ? 0 : 1;
                int exp = victim.getLevel().getLevel() * 2 - getLevel().getLevel();
                if (exp < 0)
                    exp = minExp;
                if (victim.getLevel().getLevel() > getLevel().getLevel())
                    exp *= 8;

                // Cannot get exp more than need per level + 1
                int nextLevelEpx = DBStorage.LevelCostStore.get(getLevel().getLevel() + 1).getExperience();
                if (exp > nextLevelEpx + 1)
                    exp = nextLevelEpx + 1;

                if (exp > 0)
                {
                    getLevel().addExp(exp);
                    player.getSession().addServerMessage(1038, Integer.toString(exp)); // You gain experience
                }

                // AutoLoot
                if (corpse != null && player.hasSettings(PlayerSettings.AutoLoot))
                    player.lootCorpse(corpse);
            }
        }
        // Ordinary hit.
        else
        {
            victim.changeHealth( -damage.FinalDamage);

            addServerMessage(damage.MessageDealerDamage, victim.getName(), Integer.toString(damage.FinalDamage));
            victim.addServerMessage(damage.MessageVictimDamage, getName(), Integer.toString(damage.FinalDamage));
            this.position.send(this, victim, new ServerPacket(ServerOpcodes.ServerMessage).add(damage.MessagePositionDamage).add(this.getName()).add(victim.getName()).add(damage.FinalDamage));
        }
    }

    public abstract Corpse die();

    public void useMagic(int spellId, int targetGuid)
    {
        Spell entry = DBStorage.SpellStore.get(spellId);
        if (entry == null)
            return;

        Unit target = World.getInstance().getUnit(targetGuid);
        if (target == null)
            target = this;

        if (entry.getType() == SpellTypes.Combat)
            this.combatSpell = new KeyValuePair<Spell, Unit>(entry, target);
        else
            this.effectSpell = new KeyValuePair<Spell, Unit>(entry, target);
    }

    public boolean addAura(Aura aura)
    {
        if (this.auras.containsKey(aura.getSpellEntity().getId()))
        {
            // TODO: remove aura without any message
            // but cancel bonus parameters
        }

        this.auras.put(aura.getSpellEntity().getId(), aura);
        aura.setState(Aura.States.Active);

        // Send aura data;
        if (this.isPlayer() && ((Player)this).getSession() != null)
            ((Player)this).getSession().addData(aura.getPacketData());
        return true;
    }

    /**
     * Marks an aura with AuraStates.Removed. The aura will be removed from an aura list at the next Unit.Update
     */
    public void removeAura(int spellId)
    {
        Aura aura = this.auras.get(spellId);
        if (aura == null)
            return;

        aura.setState(Aura.States.Removed);
    }

    public Aura getAura(int spellId)
    {
        return this.auras.get(spellId);
    }

    public boolean hasAura(int spellId)
    {
        return this.auras.containsKey(spellId);
    }


    protected void onVisualChanged()
    {
        if (this.position != null)
            this.position.unitVisualChanged(this);
    }

    public void addServerMessage(int messageId, String... args) { }

    public void update(int time)
    {
         // Handle unit's auras
        synchronized (this.auras)
        {
            if (this.auras.size() > 0)
            {
                List<Aura> removedAuras = new ArrayList<Aura>(this.auras.size());
                // Update auras
                for (Aura aura : this.auras.values())
                {
                    aura.update(time);
                    if (aura.getState() == States.Removed)
                        removedAuras.add(aura);
                }

                if (removedAuras.size() > 0)
                    for (Aura aura : removedAuras)
                        this.auras.remove(aura.getSpellEntity().getId());
            }
        }

        // Next action might be with alive units only
        if (this.deathState == DeathStates.Dead)
            return;

        if (this.effectSpell != null && this.effectSpell.getKey() != null)
        {
            Integer spellLevel = this.getSpellLevel(this.effectSpell.getKey().getId());
            if (spellLevel != null)
            {
                new meds.spell.Spell(this.effectSpell.getKey(), this, spellLevel).cast();
            }

            this.effectSpell = new KeyValuePair<Spell, Unit>(null, null);
        }

        // HealthMana regeneration
        if (this.healthManaRegenTact >= 4)
        {
            if (getHealth() < this.parameters.value(Parameters.Health) || getMana() < this.parameters.value(Parameters.Mana))
            {
                int healthRegen = isInCombat() ? this.parameters.value(Parameters.HealthRegeneration) / 2 : this.parameters.value(Parameters.HealthRegeneration);
                int manaRegen = isInCombat() ? this.parameters.value(Parameters.ManaRegeneration) / 2 : this.parameters.value(Parameters.ManaRegeneration);
                // Triple bonus at star
                if (this.position.getSpecialLocationType() == SpecialLocationTypes.Star)
                {
                    healthRegen *= 3;
                    manaRegen *= 3;
                }
                // Relaxing doubles the regeneration
                if (isPlayer() && ((Player)this).getRelax())
                {
                    healthRegen *= 2;
                    manaRegen *= 2;
                }

                int newHealth = this.getHealth() + healthRegen;
                int newMana = this.getMana() + manaRegen;

                if (newHealth > this.parameters.value(Parameters.Health))
                    newHealth = this.parameters.value(Parameters.Health);

                if (this.getMana() + manaRegen > this.parameters.value(Parameters.Mana))
                    newMana = this.parameters.value(Parameters.Mana);

                this.setHealthMana(newHealth, newMana);
            }
            healthManaRegenTact = 0;
        }
        else
            ++healthManaRegenTact;
    }

    @Override
    public int hashCode()
    {
        return this.guid;
    }
}

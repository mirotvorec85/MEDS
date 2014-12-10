package org.meds.spell;

import java.util.HashMap;
import java.util.Map;

import org.meds.Player;
import org.meds.ServerOpcodes;
import org.meds.ServerPacket;
import org.meds.Unit;
import org.meds.logging.Logging;

public class Aura
{
    public enum States
    {
        /**
         * An aura just has been created and is yet not applied to a target.
         */
        Created,
        /**
         * Unit is under effect of an aura
         */
        Active,
        /**
         * Still active but remained 1 minute.
         */
        Ending,
        /**
         * An aura no longer affects a target
         */
        Removed,
    }

    protected static Map<Integer, Class<? extends Aura>> customAuraClasses = new HashMap<Integer, Class<? extends Aura>>();

    static
    {
        customAuraClasses.put(14, AuraBuff.class); // Tigers Strength
        customAuraClasses.put(16, AuraBuff.class); // Steel Body
        customAuraClasses.put(17, AuraBuff.class); // Feline Grace
        customAuraClasses.put(18, AuraBuff.class); // Bears Blood
        customAuraClasses.put(36, AuraShield.class); // Layered Defense
        customAuraClasses.put(37, AuraBuff.class); // Wisdom of the Owl
        customAuraClasses.put(1000, AuraRelax.class); // Relax
        customAuraClasses.put(1141, AuraShield.class); // Heroic Shield
    }

    protected int level;
    protected int remainingTime;
    protected Unit owner;
    protected Player ownerPlayer;
    protected org.meds.database.entity.Spell spellEntry;
    protected States state;
    protected boolean minuteLeft;
    protected boolean isPermanent;

    /**
     * Creates a new instance of an Aura class.
     * @param entry Entry of the related spell.
     * @param owner A unit who is target of the aura
     * @param level Level of the aura.
     * @param duration Duration in milliseconds. If duration is negative the the aura is permanent.
     * @return A new Aura instance for this Spell
     */
    public static Aura createAura(org.meds.database.entity.Spell entry, Unit owner, int level, int duration)
    {
        Aura aura = null;
        Class<? extends Aura> auraClass = Aura.customAuraClasses.get(entry.getId());
        // Custom aura Class

        if (auraClass != null)
        {
            try
            {
                aura = auraClass.newInstance();
            }
            catch (InstantiationException | IllegalAccessException e)
            {
                Logging.Error.log("Can not instantinate the Aura class " + auraClass.getName() + " for spell ID " + entry.getId() + " Error: " + e.getMessage());
            }
        }
        // Generic (this) aura class
        else
        {
            aura = new Aura();
        }

        aura.spellEntry = entry;
        aura.owner = owner;
        if (owner.isPlayer())
            aura.ownerPlayer = (Player)owner;
        aura.level = level;
        aura.remainingTime = duration;
        aura.isPermanent = duration < 0;
        aura.state = States.Created;
        aura.minuteLeft = false;

        return aura;
    }

    protected Aura()
    {

    }

    public org.meds.database.entity.Spell getSpellEntity()
    {
        return this.spellEntry;
    }

    public int getLevel()
    {
        return this.level;
    }

    protected void setLevel(int level)
    {
        this.level = level;
        if (this.ownerPlayer != null && this.ownerPlayer.getSession() != null)
            this.ownerPlayer.getSession().addData(getPacketData());
    }

    public int getRemainingTime()
    {
        return this.remainingTime;
    }

    public States getState()
    {
        return this.state;
    }
    public void setState(States state)
    {
        switch (state)
        {
            case Created:
                break;
            case Active:
                applyAura();
                break;
            case Ending:
                if (this.owner.isPlayer())
                {
                    Player player = (Player)this.owner;
                    if (player.getSession() != null)
                        player.getSession().addServerMessage(1529, this.spellEntry.getName());
                }
                break;
            case Removed:
                removeAura();
                break;
        }

        this.state = state;
    }

    public boolean permanent()
    {
        return this.isPermanent;
    }

    protected void applyAura()
    {

    }

    public void refresh(int level, int time)
    {
        this.level = level;
        this.remainingTime = time;
        this.minuteLeft = false;
        setState(States.Active);
    }

    protected void removeAura()
    {
        // If a player - send result
        if (this.ownerPlayer != null && this.ownerPlayer.getSession() != null)
        {
            this.ownerPlayer.getSession().addData(new ServerPacket(ServerOpcodes.DeleteAura).add(this.spellEntry.getId()));
        }
    }

    public ServerPacket getPacketData()
    {
        return new ServerPacket(ServerOpcodes.Aura)
            .add(this.spellEntry.getId())
            .add(this.level)
            .add(this.isPermanent ? "-1" : this.remainingTime / 1000);
    }

    public void forceRemove()
    {

    }

    public void update(int time)
    {
        if (this.isPermanent)
            return;

        // Only active auras are updatable
        if (this.state == States.Created || this.state == States.Removed)
            return;

        this.remainingTime -= time;

        if (this.remainingTime < 0)
            this.setState(States.Removed);
        else if (this.remainingTime < 60000 && this.state == States.Active)
        {
            this.setState(States.Ending);
        }
    }
}

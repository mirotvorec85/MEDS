package meds;

public class Damage
{
    public enum ReductionTypes
    {
        Absorption,
    }

    public interface AffectionHandler
    {
        boolean handle(Damage damage);
    }


    /**
     * Calculated(clean) damage
     */
    private int initialDamage;

    private Unit target;

    public int FinalDamage;
    public int Absorbed;
    public boolean IsAutoAttack;
    public boolean IsFatal;
    /**
     * A reference to the Spell object that causes this damage.
     */
    public meds.spell.Spell Spell;

    public int MessageDealerMiss = 33;
    public int MessageDealerNoDamage = 45;
    public int MessageDealerDamage = 21;
    public int MessageDealerKillingBlow = 24;

    public int MessageVictimMiss = 34;
    public int MessageVictimNoDamage = 46;
    public int MessageVictimDamage = 22;
    public int MessageVictimKillingBlow = 25;

    public int MessagePositionMiss = 35;
    public int MessagePositionNoDamage = 47;
    public int MessagePositionDamage = 23;
    public int MessagePositionKillingBlow = 26;

    public Damage(int initialDamage, Unit target)
    {
        this.initialDamage = this.FinalDamage = initialDamage;
        this.target = target;
    }

    public int getInitialDamage()
    {
        return this.initialDamage;
    }

    public int getRealDamage()
    {
        return FinalDamage - Absorbed;
    }

    public Unit getTarget()
    {
        return target;
    }
}

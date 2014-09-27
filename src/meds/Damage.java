package meds;

public class Damage
{
    /**
     * Calculated(clean) damage
     */
    private int initialDamage;

    public int FinalDamage;
    public boolean IsAutoAttack;
    public boolean IsFatal;
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

    public int getInitialDamage()
    {
        return this.initialDamage;
    }

    public void setInitialDamage(int initialDamage)
    {
        this.initialDamage = initialDamage;
        this.FinalDamage = initialDamage;
    }
}

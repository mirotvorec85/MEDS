package meds.spell;

import meds.ServerOpcodes;
import meds.ServerPacket;
import meds.enums.Parameters;

public class AuraRelax extends Aura
{
    private int healthRegenBonus = 0;
    private int manaRegenBonus = 0;

    @Override
    protected void applyAura()
    {
        if (this.ownerPlayer != null && this.ownerPlayer.getSession() != null)
            this.ownerPlayer.getSession().addServerMessage(1051).addData(new ServerPacket(ServerOpcodes.RelaxOn));
        this.healthRegenBonus = this.owner.getParameters().base().value(Parameters.HealthRegeneration);
        this.manaRegenBonus = this.owner.getParameters().base().value(Parameters.ManaRegeneration);
        this.owner.getParameters().magic().change(Parameters.HealthRegeneration, this.healthRegenBonus);
        this.owner.getParameters().magic().change(Parameters.ManaRegeneration, this.manaRegenBonus);
        super.applyAura();
    }

    @Override
    protected void removeAura()
    {
        if (this.ownerPlayer != null && this.ownerPlayer.getSession() != null)
            this.ownerPlayer.getSession().addServerMessage(303).addData(new ServerPacket(ServerOpcodes.RelaxOff));
        this.owner.getParameters().magic().change(Parameters.HealthRegeneration, -this.healthRegenBonus);
        this.owner.getParameters().magic().change(Parameters.ManaRegeneration, -this.manaRegenBonus);
        super.removeAura();
    }
}

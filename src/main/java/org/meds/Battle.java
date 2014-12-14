package org.meds;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.meds.enums.BattleStates;
import org.meds.logging.Logging;
import org.meds.net.ServerOpcodes;
import org.meds.net.ServerPacket;
import org.meds.util.Random;

public class Battle
{
    /**
     * A list of units who left this battle at the current battle update tick.
     */
    private Set<Unit> leftUnits;
    /**
     * A list of units who are fleeing at the current battle update tick.
     */
    private Set<Unit> runUnits;
    private Set<Unit> participants;

    public Battle()
    {
        this.leftUnits = new LinkedHashSet<>();
        this.runUnits = new HashSet<>();
        this.participants = new HashSet<>();
        World.getInstance().addBattle(this);
    }

    /**
     * Involve a new participant into the battle.
     * @param unit A new Unit participant.
     */
    public void enterBattle(Unit unit)
    {
        synchronized (this.participants)
        {
            if (!this.participants.contains(unit))
            {
                this.participants.add(unit);
            }
            Logging.Debug.log(String.format("Unit \"%s\"(%d) entered battle.", unit.getName(), unit.getGuid()));
            sendBattleState(unit, BattleStates.EnterBattle);
            // This unit may be left already
            // but another participant has involved him again.
            this.leftUnits.remove(unit);
        }
    }

    public void leaveBattle(Unit unit)
    {
        Logging.Debug.log(String.format("Unit \"%s\"(%d) left battle.", unit.getName(), unit.getGuid()));
        sendBattleState(unit, BattleStates.NoBattle);
        // Add to the list of left units
        // These units will be remove from the battle after the next Battle.Update cycle.
        this.leftUnits.add(unit);
    }

    public void runAway(Unit unit)
    {
        // Unit is not a participant or this unit is fleeing already
        if (!this.participants.contains(unit) || this.runUnits.contains(unit))
            return;
        this.runUnits.add(unit);
    }

    public void onTargetDied(Unit unit)
    {
        // Send to attacker BattleStates.TargetDied.
        sendBattleState(unit, BattleStates.TargetDead);

        // Setting attacker's target to NULL will call Battle.LeaveBattle
        // where the attacker will be marked as a leftUnit
        unit.setTarget(null);
    }

    public void onDied(Unit unit)
    {
        sendBattleState(unit, BattleStates.Death);
        // Setting attacker's target to NULL will call Battle.LeaveBattle
        // where the attacker will be marked as a leftUnit
        unit.setTarget(null);
    }

    private void sendBattleState(Unit unit, BattleStates state)
    {
        if (!unit.isPlayer())
            return;
        Player player = (Player)unit;
        if (player.getSession() != null)
            player.getSession().addData(new ServerPacket(ServerOpcodes.BattleState).add(state));
    }

    public void update(int time)
    {
        // The battle has no participants from the start or
        // all the participants were left => battle is over.
        if (this.participants.size() == 0)
        {
            World.getInstance().removeBattle(this);
            return;
        }

        // Do the attack of every participant
        for (Unit attacker : this.participants)
        {
            Unit target = attacker.getTarget();

            // An attacker should not be in leftUnit list
            // For ex., a previuos participant has killed this unit
            // And both are still in the list
            if (this.leftUnits.contains(attacker))
                continue;

            // Attacker has no target
            if (target == null)
            {
                leaveBattle(attacker);
                continue;
            }

            // Target is at another location
            if (target.getPosition() != attacker.getPosition())
            {
                attacker.setTarget(null);
                continue;
            }

            // the attacker is fleeing
            if (this.runUnits.contains(attacker))
            {
                Player player = attacker.isPlayer() ? (Player)attacker : null;
                if (player != null && player.getSession() == null)
                    player = null;
                // TODO: Found out chance calculation
                double chanceToFlee = 0.5d;
                if (Random.nextDouble() < chanceToFlee)
                {
                    this.runUnits.remove(attacker);

                    sendBattleState(attacker, BattleStates.Runaway);
                    // Set target to NULL and leave the battle
                    attacker.setTarget(null);
                    // Send run away result message
                    if (player != null)
                        player.getSession().addServerMessage(39, target.getName());
                    // Relocate target
                    attacker.setPosition(attacker.getPosition().getRandomNeighbour(attacker.isPlayer(), attacker.isPlayer()));
                }
                // Send Fail message
                else if (player != null)
                    player.getSession().addServerMessage(42, target.getName());
                // TODO: solve permanent runaway state problem.
            }
            else
            {
                attacker.doBattleAttack();
            }
        }

        // Clear the leftUnits
        synchronized (this.leftUnits)
        {
            for (Unit unit : this.leftUnits)
            {
                this.participants.remove(unit);
            }
            this.leftUnits.clear();
        }

        // For all participants who still is in battle
        // Send BattleState.Battle
        synchronized (this.participants)
        {
            for (Unit unit : this.participants)
                sendBattleState(unit, BattleStates.Battle);
        }
    }
}

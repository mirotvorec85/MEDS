package org.meds;

import java.util.HashMap;
import java.util.Map;

import org.meds.enums.ItemBonusParameters;
import org.meds.enums.Parameters;
import org.meds.enums.Races;

public class UnitParameters
{
    public class Parameter
    {
        protected Map<Parameters, Integer> values;

        public Parameter()
        {
            this.values = new HashMap<Parameters, Integer>();
            clear();
        }

        /**
         * Gets a value of the specified parameter.
         */
        public int value(Parameters parameter)
        {
            return this.values.get(parameter);
        }

        /**
         * Sets a value of the specified parameter.
         */
        public void value(Parameters parameter, int value)
        {
            Integer prevValue = this.values.get(parameter);
            this.values.put(parameter, value);
            if (prevValue != null)
            {
                if (prevValue.intValue() != value)
                {
                    UnitParameters.this.onParameterChanged(parameter);
                }
            }
        }

        public void change(Parameters parameter, int difference)
        {
            this.value(parameter, this.values.get(parameter) + difference);
        }

        public void clear()
        {
            for (Parameters parameter : Parameters.values())
            {
                this.value(parameter, 0);
            }
        }
    }

    public class EquipmentParameter extends Parameter
    {
        public int value(ItemBonusParameters parameter)
        {
            return value(this.convertFromItemParameters(parameter));
        }

        public void value(ItemBonusParameters parameter, int value)
        {
            value(convertFromItemParameters(parameter), value);
        }

        public void change(ItemBonusParameters parameter, int difference)
        {
            change(convertFromItemParameters(parameter), difference);
        }

        protected Parameters convertFromItemParameters(ItemBonusParameters parameter)
        {
            switch (parameter)
            {
                case BonusConstitution:
                    return Parameters.Constitution;
                case BonusStrength:
                    return Parameters.Strength;
                case BonusDexterity:
                    return Parameters.Dexterity;
                case BaseMinDamage:
                    return Parameters.MinDamage;
                case BaseMaxDamage:
                    return Parameters.MaxDamage;
                case BonusDamage:
                    return Parameters.Damage;
                case BaseProtection:
                case BonusProtection:
                    return Parameters.Protection;
                case BaseArmour:
                case BonusArmour:
                    return Parameters.Armour;
                case BaseChanceToHit:
                case BonusChanceToHit:
                    return Parameters.ChanceToHit;
                case BonusChanceToCast:
                    return Parameters.ChanceToCast;
                case BonusMagicDamage:
                    return Parameters.MagicDamage;
                case BonusHealth:
                    return Parameters.Health;
                case BonusMana:
                    return Parameters.Mana;
                case BonusHealthRegeneration:
                    return Parameters.HealthRegeneration;
                case BonusManaRegeneration:
                    return Parameters.ManaRegeneration;
                case BonusFireResistance:
                    return Parameters.FireResistance;
                case BonusFrostResistance:
                    return Parameters.FrostResistance;
                case BonusLightningResistance:
                    return Parameters.LightningResistance;
                default:
                    return Parameters.None;
            }
        }
    }

    private Parameter baseParameter;
    private Parameter guildParameter;
    private EquipmentParameter equipmentParameter;
    private Parameter magicParameter;

    private Map<Parameters, Integer> total;
    private Unit unit;

    public UnitParameters(Unit unit)
    {
        this.baseParameter = new Parameter();
        this.guildParameter = new Parameter();
        this.equipmentParameter = new EquipmentParameter();
        this.magicParameter = new Parameter();
        this.total = new HashMap<Parameters, Integer>();
        // Clear Total values
        for (Parameters parameter : Parameters.values())
        {
            this.total.put(parameter, 0);
        }
        this.unit = unit;

        // TODO: add LevelChanged event to recalculate the parameter
    }

    /**
     * Gets a total value of the specified parameter.
     */
    public int value(Parameters parameter)
    {
        return this.total.get(parameter);
    }

    /**
     * Sets a total value of the specified parameter.
     */
    private void value(Parameters parameter, int value)
    {
        this.total.put(parameter, value);
    }

    public Parameter base()
    {
        return this.baseParameter;
    }

    public Parameter guild()
    {
        return this.guildParameter;
    }

    public EquipmentParameter equipment()
    {
        return this.equipmentParameter;
    }

    public Parameter magic()
    {
        return this.magicParameter;
    }

    private void onParameterChanged(Parameters parameter)
    {
        this.value(parameter, getTotalSumm(parameter));
        int modifier = 1;
        double modifierD = 1d;

        switch (parameter)
        {
            case Constitution:
                recalculateBase(Parameters.Protection);
                recalculateBase(Parameters.Armour);
                recalculateBase(Parameters.Health);
                recalculateBase(Parameters.Mana);
                recalculateBase(Parameters.HealthRegeneration);
                recalculateBase(Parameters.ManaRegeneration);
                break;
            case Strength:
                recalculateBase(Parameters.Damage);
                recalculateBase(Parameters.Health);
                recalculateBase(Parameters.HealthRegeneration);
                recalculateBase(Parameters.AllResistance);
                recalculateBase(Parameters.HealthRegeneration);
                break;
            case Dexterity:
                recalculateBase(Parameters.Damage);
                recalculateBase(Parameters.ChanceToHit);
                recalculateBase(Parameters.Armour);
                break;
            case Intelligence:
                recalculateBase(Parameters.ChanceToCast);
                recalculateBase(Parameters.MagicDamage);
                recalculateBase(Parameters.Mana);
                recalculateBase(Parameters.ManaRegeneration);
                recalculateBase(Parameters.AllResistance);
                break;

            case Damage:
            case MaxDamage:
            case MinDamage:
                // All these parameters are depending on each other
                // So need recalculate all of them

                if (this.unit.getRace() == Races.Orc)
                    modifierD = 5d / 4;

                // if MinDamage value has been changed, its total value calculated at the top of this method
                this.value(Parameters.Damage, (int)((getTotalSumm(Parameters.Damage) + this.value(Parameters.MinDamage)) * modifierD));
                this.value(Parameters.MaxDamage, (int)((getTotalSumm(Parameters.MaxDamage) + getTotalSumm(Parameters.Damage)) * modifierD));

                /*
                switch (parameter)
                {
                    case Parameters.Damage:
                        this.total[parameter] += this.total[Parameters.MinDamage];
                        this.total[parameter] *= modifier;
                        break;
                    case Parameters.MaxDamage:
                        this.total[parameter] += GetTotalSumm(Parameters.Damage);
                        this.total[parameter] *= modifier;
                        break;
                    case Parameters.MinDamage:
                        this.total[Parameters.Damage] = GetTotalSumm(Parameters.Damage) + this.total[Parameters.MinDamage];
                        this.total[Parameters.MaxDamage] = GetTotalSumm(Parameters.Damage) + GetTotalSumm(Parameters.Damage);
                        this.total[Parameters.Damage] *= modifier;
                        this.total[Parameters.MaxDamage] *= modifier;
                        break;
                }
                */
                break;
            case ChanceToHit:
                if (this.unit.getRace() == Races.Elf)
                    modifier = 3;
                this.value(Parameters.ChanceToHit,  this.value(Parameters.ChanceToHit) * modifier);
                break;
            case Armour:
                if (this.unit.getRace() == Races.Elf || this.unit.getRace() == Races.Dwarf)
                    modifier = 3;
                this.value(Parameters.Armour,  this.value(Parameters.Armour) * modifier);
                break;
            case ChanceToCast:
                modifier = 1;
                if (this.unit.getRace() == Races.Human)
                    modifier = 3;
                else if (this.unit.getRace() == Races.Drow)
                    modifier = 2;
                this.value(Parameters.ChanceToCast,  this.value(Parameters.ChanceToCast) * modifier);
                break;
            case FireResistance:
            case FrostResistance:
            case LightningResistance:
            case AllResistance:
                modifier = 1;
                if (this.unit.getRace() == Races.Orc || this.unit.getRace() == Races.Dwarf)
                    modifier = 3;
                this.value(Parameters.AllResistance, getTotalSumm(Parameters.AllResistance));
                this.value(Parameters.FireResistance, getTotalSumm(Parameters.FireResistance) + this.value(Parameters.AllResistance));
                this.value(Parameters.FrostResistance, getTotalSumm(Parameters.FrostResistance) + this.value(Parameters.AllResistance));
                this.value(Parameters.LightningResistance, getTotalSumm(Parameters.LightningResistance) + this.value(Parameters.AllResistance));
                this.value(Parameters.AllResistance,  this.value(Parameters.AllResistance) * modifier);
                this.value(Parameters.FireResistance,  this.value(Parameters.FireResistance) * modifier);
                this.value(Parameters.FrostResistance,  this.value(Parameters.FrostResistance) * modifier);
                this.value(Parameters.LightningResistance,  this.value(Parameters.LightningResistance) * modifier);
                break;
            // Protection
            // Magic Damage
            // Health
            // Mana
            // Health Regeneration
            // Mana Regeneration
            default:
                break;
        }

    }

    public void recalculate()
    {
        recalculateBase(Parameters.Constitution);
        recalculateBase(Parameters.Strength);
        recalculateBase(Parameters.Dexterity);
        recalculateBase(Parameters.Intelligence);
    }

    private void recalculateBase(Parameters parameter)
    {
        int bonus = 0;
        switch (parameter)
        {
            case Damage:
                if (this.unit.getRace() == Races.Orc)
                    bonus = this.unit.getLevel() * 6;
                base().value(Parameters.Damage, bonus + this.value(Parameters.Dexterity) + this.value(Parameters.Strength));
                break;
            case Protection:
                base().value(Parameters.Protection, this.value(Parameters.Constitution));
                break;
            case ChanceToHit:
                if (this.unit.getRace() == Races.Elf)
                    bonus = this.unit.getLevel() / 2;
                base().value(Parameters.ChanceToHit, bonus + this.value(Parameters.Dexterity));
                break;
            case Armour:
                if (this.unit.getRace() == Races.Dwarf || this.unit.getRace() == Races.Elf)
                    bonus = this.unit.getLevel() * 3 / 2; // or * 1.5
                base().value(Parameters.Armour, bonus + this.value(Parameters.Dexterity) + this.value(Parameters.Constitution) / 3);
                break;
            case ChanceToCast:
                if (this.unit.getRace() == Races.Human || this.unit.getRace() == Races.Drow)
                    bonus = this.unit.getLevel() / 2;
                base().value(Parameters.ChanceToCast, bonus + this.value(Parameters.Intelligence) / 2);
                break;
            case MagicDamage:
                base().value(Parameters.MagicDamage, this.value(Parameters.Intelligence) / 2);
                break;
            case Health:
                base().value(Parameters.Health, this.value(Parameters.Constitution) * 8 + this.value(Parameters.Strength) * 4);
                break;
            case Mana:
                base().value(Parameters.Mana, this.value(Parameters.Constitution) + this.value(Parameters.Intelligence) * 4);
                break;
            case HealthRegeneration:
                base().value(Parameters.HealthRegeneration, this.value(Parameters.Constitution) + this.value(Parameters.Strength) / 4);
                break;
            case ManaRegeneration:
                base().value(Parameters.ManaRegeneration, (int)(this.value(Parameters.Constitution) /* * 0.05*/ / 20d + this.value(Parameters.Intelligence) /* * 0.2*/ / 5d));
                break;
            case AllResistance:
            case FireResistance:
            case FrostResistance:
            case LightningResistance:
                if (this.unit.getRace() == Races.Dwarf || this.unit.getRace() == Races.Orc)
                    bonus = this.unit.getLevel() * 3 / 2; // or * 1.5
                int value = this.value(Parameters.Strength) * 3 / 10 + this.value(Parameters.Intelligence) / 10;
                base().value(Parameters.FireResistance, value);
                base().value(Parameters.FrostResistance, value);
                base().value(Parameters.LightningResistance, value);
                break;
            default:
                break;
        }
    }

    private int getTotalSumm(Parameters parameter)
    {
        return this.baseParameter.value(parameter) + this.guildParameter.value(parameter) + this.equipmentParameter.value(parameter) + this.magicParameter.value(parameter);
    }
}

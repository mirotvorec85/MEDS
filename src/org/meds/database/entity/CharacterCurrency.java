package org.meds.database.entity;

import java.io.Serializable;

import org.meds.database.DBStorage;

public class CharacterCurrency implements Serializable
{
    private static final long serialVersionUID = -6216284843138605L;

    private int characterId;
    private int currencyId;
    private int amount;

    private Currency currency;

    public CharacterCurrency() { }

    public CharacterCurrency(int characterId, int currencyId, int amount)
    {
        this.characterId = characterId;
        setCharacterId(characterId);
        this.amount = amount;
    }

    public int getCharacterId()
    {
        return characterId;
    }

    public void setCharacterId(int characterId)
    {
        this.characterId = characterId;
    }

    public int getCurrencyId()
    {
        return currencyId;
    }

    public void setCurrencyId(int currencyId)
    {
        this.currencyId = currencyId;
        this.currency = DBStorage.CurrencyStore.get(currencyId);
    }

    public Currency getCurrency()
    {
        return this.currency;
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof CharacterCurrency))
            return false;
        CharacterCurrency cObj = (CharacterCurrency)obj;

        return this.characterId == cObj.characterId && this.currencyId == cObj.currencyId;
    }

    @Override
    public int hashCode()
    {
        return this.characterId * 1000 + this.currencyId;
    }

}

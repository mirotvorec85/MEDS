package org.meds.data.domain;

import java.io.Serializable;

public class CharacterCurrency implements Serializable {

    private static final long serialVersionUID = -6216284843138605L;

    private int characterId;
    private int currencyId;
    private int amount;

    public CharacterCurrency() { }

    public CharacterCurrency(int characterId, int currencyId) {
        setCharacterId(characterId);
        setCurrencyId(currencyId);
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CharacterCurrency that = (CharacterCurrency) o;

        return this.characterId == that.characterId
                && this.currencyId == that.currencyId;
    }

    @Override
    public int hashCode() {
        return this.characterId * 1000 + this.currencyId;
    }

}

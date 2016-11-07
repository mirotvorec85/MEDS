package org.meds.data.domain;

import org.meds.enums.ShopTypes;

import java.util.Set;

public class Shop {

    private int id;
    private ShopTypes type;
    private int currencyId;
    private Set<ShopItem> shopItems;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTypeInteger() {
        return type.getValue();
    }

    public void setTypeInteger(int type) {
        this.type = ShopTypes.parse(type);
    }

    public ShopTypes getType() {
        return this.type;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public void setItems(Set<ShopItem> items) {
        this.shopItems = items;
    }

    public Set<ShopItem> getItems() {
        return this.shopItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Shop shop = (Shop) o;

        return this.id == shop.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}

package org.meds.data.domain;

import org.meds.item.ItemClasses;

public class ItemTemplate {
    private int id;
    private String title;
    private String description;
    private int imageId;
    private ItemClasses itemClass;
    private int level;
    private int cost;
    private int currencyId;
    private int subClass;
    private int flags;
    private String itemBonuses;

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public int getImageId() {
        return imageId;
    }

    private void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public ItemClasses getItemClass() {
        return this.itemClass;
    }

    public void setItemClass(ItemClasses itemClass) {
        this.itemClass = itemClass;
    }

    public int getItemClassInteger() {
        return this.itemClass.getValue();
    }

    public void setItemClassInteger(int itemClass) {
        this.itemClass = ItemClasses.parse(itemClass);
    }

    public int getLevel() {
        return level;
    }

    private void setLevel(int level) {
        this.level = level;
    }

    public int getCost() {
        return cost;
    }

    private void setCost(int cost) {
        this.cost = cost;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    private void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public int getSubClass() {
        return subClass;
    }

    private void setSubClass(int subClass) {
        this.subClass = subClass;
    }

    public int getFlags() {
        return flags;
    }

    private void setFlags(int flags) {
        this.flags = flags;
    }

    public String getItemBonuses() {
        return itemBonuses;
    }

    private void setItemBonuses(String itemBonuses) {
        this.itemBonuses = itemBonuses;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof ItemTemplate))
            return false;
        ItemTemplate cObj = (ItemTemplate) obj;
        return this.id == cObj.id;
    }
}

package meds.database.entity;

import meds.enums.ItemClasses;
import meds.enums.ItemFlags;
import meds.util.EnumFlags;

public class ItemTemplate
{
    private int id;
    private String title;
    private String description;
    private int imageId;
    private ItemClasses itemClass;
    private int level;
    private int cost;
    private int currencyId;
    private int subClass;
    private EnumFlags<ItemFlags> flags;
    private String itemBonuses;

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public int getImageId()
    {
        return imageId;
    }
    public void setImageId(int imageId)
    {
        this.imageId = imageId;
    }
    public int getItemClassInteger()
    {
        return itemClass.getValue();
    }
    public void setItemClassInteger(int itemClass)
    {
        this.itemClass = ItemClasses.parse(itemClass);
    }
    public ItemClasses getItemClass()
    {
        return this.itemClass;
    }
    public int getLevel()
    {
        return level;
    }
    public void setLevel(int level)
    {
        this.level = level;
    }
    public int getCost()
    {
        return cost;
    }
    public void setCost(int cost)
    {
        this.cost = cost;
    }
    public int getCurrencyId()
    {
        return currencyId;
    }
    public void setCurrencyId(int currencyId)
    {
        this.currencyId = currencyId;
    }
    public int getSubClass()
    {
        return subClass;
    }
    public void setSubClass(int subClass)
    {
        this.subClass = subClass;
    }
    public int getFlags()
    {
        return flags.getFlags();
    }
    public void setFlags(int flags)
    {
        this.flags = new EnumFlags<ItemFlags>(flags);
    }
    public boolean hasFlag(ItemFlags flag)
    {
        return this.flags.has(flag);
    }
    public String getItemBonuses()
    {
        return itemBonuses;
    }
    public void setItemBonuses(String itemBonuses)
    {
        this.itemBonuses = itemBonuses;
    }

    @Override
    public int hashCode()
    {
        return this.id;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (!(obj instanceof ItemTemplate))
            return false;
        ItemTemplate cObj = (ItemTemplate)obj;
        return this.id == cObj.id;
    }
}

package meds.database.entity;

import java.io.Serializable;

public class ShopItem implements Serializable
{
    private static final long serialVersionUID = 4947432592066357239L;

    private int shopId;
    private int itemTemplateId;
    private int count;

    public int getShopId()
    {
        return shopId;
    }
    public void setShopId(int shopId)
    {
        this.shopId = shopId;
    }
    public int getItemTemplateId()
    {
        return itemTemplateId;
    }
    public void setItemTemplateId(int itemTemplateId)
    {
        this.itemTemplateId = itemTemplateId;
    }
    public int getCount()
    {
        return count;
    }
    public void setCount(int count)
    {
        this.count = count;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof ShopItem))
            return false;
        ShopItem cObj = (ShopItem)obj;
        return this.shopId == cObj.shopId && this.itemTemplateId == cObj.itemTemplateId;
    }

    @Override
    public int hashCode()
    {
        return this.shopId * 1000000 + this.itemTemplateId;
    }
}

package meds.database.entity;

public class NewMessage
{
    private int id;
    private int typeId;
    private String message;

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public int getTypeId()
    {
        return typeId;
    }
    public void setTypeId(int typeId)
    {
        this.typeId = typeId;
    }
    public String getMessage()
    {
        return message;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }
}

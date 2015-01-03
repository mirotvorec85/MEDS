package org.meds.database.entity;

import java.util.Map;

import org.meds.net.ServerCommands;
import org.meds.net.ServerPacket;
import org.meds.database.DBStorage;

public class Guild
{
    private int id;
    private String name;
    private int prevId;
    private int nextId;

    private Map<Integer, GuildLesson> lessons;
    private ServerPacket lessonsData;

    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public int getPrevId()
    {
        return prevId;
    }
    public void setPrevId(int prevId)
    {
        this.prevId = prevId;
    }
    public int getNextId()
    {
        return nextId;
    }
    public void setNextId(int nextId)
    {
        this.nextId = nextId;
    }

    public Map<Integer, GuildLesson> getLessons()
    {
        if (this.lessons == null)
        {
            this.lessons = DBStorage.GuildLessonStore.get(this.id);
        }
        return this.lessons;
    }

    public ServerPacket getLessonsData()
    {
        if (this.lessonsData == null)
        {
            this.lessonsData = new ServerPacket(ServerCommands.GuildLessonsInfo)
                .add(this.id)
                .add(this.name);
            if (this.getLessons() != null)
            {
                for (int i = 1; i <= this.getLessons().size(); ++i)
                {
                    this.lessonsData.add(this.getLessons().get(i).getDescription());
                }
            }
        }
        return this.lessonsData;
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
        if (!(obj instanceof Guild))
            return false;
        Guild cObj = (Guild)obj;
        return this.id == cObj.id;
    }
}

package org.meds.database.entity;

import org.meds.Locale;
import org.meds.net.ServerCommands;
import org.meds.net.ServerPacket;
import org.meds.database.DBStorage;
import org.meds.enums.QuestTypes;
import org.meds.logging.Logging;

@SuppressWarnings("unused")
public class QuestTemplate
{
    private int id;
    private QuestTypes type;
    private String title;
    private String description;
    private String beginText;
    private String endText;
    private int level;
    private int nextQuestId;
    private int prevQuestId;
    private int sourceItemId;
    private int sourceItemCount;
    private int requiredCount;
    private int requiredCreatureId;
    private int requiredItemId;
    private int time;
    private int rewardExp;
    private int rewardGold;
    private int rewardItem1Id;
    private int rewardItem1Count;
    private int rewardItem2Id;
    private int rewardItem2Count;
    private int tracking;

    private ItemTemplate sourceItemTemplate;
    private CreatureTemplate requiredCreatureTemplate;
    private ItemTemplate requiredItemTemplate;
    private ItemTemplate rewardItem1Template;
    private ItemTemplate rewardItem2Template;

    public int getId()
    {
        return id;
    }
    private void setId(int id)
    {
        this.id = id;
    }
    public int getTypeInteger()
    {
        return this.type.getValue();
    }
    public QuestTypes getType()
    {
        return this.type;
    }
    private void setTypeInteger(int type)
    {
        this.type = QuestTypes.parse(type);
    }
    public String getTitle()
    {
        return title;
    }
    private void setTitle(String title)
    {
        this.title = title;
    }
    public String getDescription()
    {
        return description;
    }
    private void setDescription(String description)
    {
        this.description = description;
    }
    public String getBeginText()
    {
        return beginText;
    }
    private void setBeginText(String beginText)
    {
        this.beginText = beginText;
    }
    public String getEndText()
    {
        return endText;
    }
    private void setEndText(String endText)
    {
        this.endText = endText;
    }
    public int getLevel()
    {
        return level;
    }
    private void setLevel(int level)
    {
        this.level = level;
    }
    public Integer getNextQuestId()
    {
        if (this.nextQuestId == 0)
            return null;
        return this.nextQuestId;
    }
    private void setNextQuestId(Integer nextQuestId)
    {
        if (nextQuestId == null)
            this.nextQuestId = 0;
        else
            this.nextQuestId = nextQuestId;
    }
    public Integer getPrevQuestId()
    {
        if (this.prevQuestId == 0)
            return null;
        return this.prevQuestId;
    }
    private void setPrevQuestId(Integer prevQuestId)
    {
        if (prevQuestId == null)
            this.prevQuestId = 0;
        else
            this.prevQuestId = prevQuestId;
    }
    public Integer getSourceItemId()
    {
        if (this.sourceItemId == 0)
            return null;
        return this.sourceItemId;
    }
    private void setSourceItemId(Integer sourceItemId)
    {
        if (sourceItemId == null)
            this.sourceItemId = 0;
        else
            this.sourceItemId = sourceItemId;
    }
    public int getSourceItemCount()
    {
        return sourceItemCount;
    }
    private void setSourceItemCount(int sourceItemCount)
    {
        this.sourceItemCount = sourceItemCount;
    }
    public int getRequiredCount()
    {
        return requiredCount;
    }
    private void setRequiredCount(int requiredCount)
    {
        this.requiredCount = requiredCount;
    }
    public int getRequiredCreatureId()
    {
        return requiredCreatureId;
    }
    private void setRequiredCreatureId(Integer requiredCreatureId)
    {
        if (requiredCreatureId == null)
            this.requiredCreatureId = 0;
        else
            this.requiredCreatureId = requiredCreatureId;
    }
    public int getRequiredItemId()
    {
        return requiredItemId;
    }
    private void setRequiredItemId(Integer requiredItemId)
    {
        if (requiredItemId == null)
            this.requiredItemId = 0;
        else
            this.requiredItemId = requiredItemId;
    }
    public int getTime()
    {
        return time;
    }
    private void setTime(int time)
    {
        this.time = time;
    }
    public int getRewardExp()
    {
        return rewardExp;
    }
    private void setRewardExp(int rewardExp)
    {
        this.rewardExp = rewardExp;
    }
    public int getRewardGold()
    {
        return rewardGold;
    }
    private void setRewardGold(int rewardGold)
    {
        this.rewardGold = rewardGold;
    }
    public int getRewardItem1Id()
    {
        return rewardItem1Id;
    }
    public ItemTemplate getRewardItem1Template()
    {
        return this.rewardItem1Template;
    }
    private void setRewardItem1Id(Integer rewardItem1Id)
    {
        if (rewardItem1Id == null)
            this.rewardItem1Id = 0;
        else
        {
            this.rewardItem1Id = rewardItem1Id;
            this.rewardItem1Template = DBStorage.ItemTemplateStore.get(rewardItem1Id);
            if (this.rewardItem1Template == null)
                Logging.Warn.log("QuestTemplate " + this.id + " has non-existed ItemReward1 " + rewardItem1Id);
        }
    }
    public int getRewardItem1Count()
    {
        return rewardItem1Count;
    }
    private void setRewardItem1Count(int rewardItem1Count)
    {
        this.rewardItem1Count = rewardItem1Count;
    }
    public int getRewardItem2Id()
    {
        return rewardItem2Id;
    }
    public ItemTemplate getRewardItem2Template()
    {
        return this.rewardItem2Template;
    }
    private void setRewardItem2Id(Integer rewardItem2Id)
    {
        if (rewardItem2Id == null)
            this.rewardItem2Id = 0;
        else
        {
            this.rewardItem2Id = rewardItem2Id;
            this.rewardItem2Template = DBStorage.ItemTemplateStore.get(rewardItem2Id);
            if (this.rewardItem2Template == null)
                Logging.Warn.log("QuestTemplate " + this.id + " has non-existed ItemReward2 " + rewardItem2Id);
        }
    }
    public int getRewardItem2Count()
    {
        return rewardItem2Count;
    }
    private void setRewardItem2Count(int rewardItem2Count)
    {
        this.rewardItem2Count = rewardItem2Count;
    }
    public int getTracking()
    {
        return tracking;
    }
    private void setTracking(int tracking)
    {
        this.tracking = tracking;
    }

    public ServerPacket getQuestInfoData()
    {
        return this.getQuestInfoData(false);
    }

    // TODO: Remove this method from this class
    public ServerPacket getQuestInfoData(boolean isForAccept)
    {
        ServerPacket packet = new ServerPacket(isForAccept ? ServerCommands.QuestInfoForAccept : ServerCommands.QuestInfo);

        packet.add(this.id)
            .add(this.title)
            .add(this.type)
            .add(this.description)
            .add(""); // Always empty

        switch (this.type)
        {
            case Kill:
                CreatureTemplate creatureTemplate = DBStorage.CreatureTemplateStore.get(this.requiredCreatureId);
                if (creatureTemplate == null)
                {
                    packet.add("").add("").add("");
                }
                else
                {
                    packet.add(this.requiredCount)
                        .add(creatureTemplate.getName())
                        .add("");
                }
                break;
            default:
                packet.add("").add("").add("");
                break;
        }
        int rewardCount = 0;

        if (this.rewardExp != 0)
        {
            packet.add(this.rewardExp + Locale.getString(1));
            ++rewardCount;
        }
        if (this.rewardGold != 0)
        {
            packet.add(this.rewardGold + Locale.getString(2));
            ++rewardCount;
        }

        ItemTemplate itemTemplate;
        if (this.rewardItem1Count != 0 && this.rewardItem1Id != 0)
        {
            itemTemplate = DBStorage.ItemTemplateStore.get(this.rewardItem1Id);
            if (itemTemplate != null)
            {
                packet.add(this.rewardItem1Count + " x " + itemTemplate.getTitle());
                ++rewardCount;
            }
        }
        if (this.rewardItem2Count != 0 && this.rewardItem2Id != 0)
        {
            itemTemplate = DBStorage.ItemTemplateStore.get(this.rewardItem2Id);
            if (itemTemplate != null)
            {
                packet.add(this.rewardItem2Count + " x " + itemTemplate.getTitle());
                ++rewardCount;
            }
        }

        while (rewardCount != 3)
        {
            packet.add("");
            ++rewardCount;
        }

        packet.add(0); // ??? Tutorial?

        return packet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestTemplate that = (QuestTemplate) o;

        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}

package org.meds;

import org.meds.data.domain.CharacterQuest;
import org.meds.data.domain.CreatureTemplate;
import org.meds.data.domain.ItemTemplate;
import org.meds.data.domain.QuestTemplate;
import org.meds.database.DataStorage;
import org.meds.enums.Currencies;
import org.meds.enums.QuestStatuses;
import org.meds.enums.QuestTypes;
import org.meds.net.ServerCommands;
import org.meds.net.ServerPacket;

import java.util.Date;

public class Quest {

    private class KillingBlowHandler implements Unit.KillingBlowListener {

        @Override
        public void handleEvent(Unit.DamageEvent e) {

            Quest quest = Quest.this;

            if (e.getVictim().getUnitType() != UnitTypes.Creature)
                return;

            if (((Creature)e.getVictim()).getTemplate().getTemplateId() != quest.questTemplate.getRequiredCreatureId())
                return;

            quest.setProgress(quest.getProgress() + 1);

            if (quest.player.getSession() != null) {
                quest.player.getSession().send(quest.getUpdateQuestData());
                quest.player.getSession().send(new ServerPacket(ServerCommands.ServerMessage).add(337).add(quest.questTemplate.getTitle()).add(quest.getProgress()).add(quest.questTemplate.getRequiredCount()));
            }

            if (quest.getProgress() == quest.questTemplate.getRequiredCount()) {
                quest.goalAchieved();
            }
        }
    }

    private static final long serialVersionUID = 1L;

    private CharacterQuest characterQuest;
    private Player player;
    private QuestTemplate questTemplate;

    private boolean isGoalAchieved;

    private Unit.KillingBlowListener killingBlowHandler;

    public Quest(Player player, QuestTemplate template, CharacterQuest quest) {
        this.player = player;
        this.questTemplate = template;
        this.characterQuest = quest;
    }

    public QuestTemplate getQuestTemplate() {
        return this.questTemplate;
    }

    public QuestStatuses getStatus(){
        return QuestStatuses.parse(this.characterQuest.getStatusInteger());
    }

    private void setStatus(QuestStatuses status){
        this.characterQuest.setStatusInteger(status.getValue());
    }

    public int getProgress() {
        return this.characterQuest.getProgress();
    }

    private void setProgress(int progress) {
        this.characterQuest.setProgress(progress);
    }

    public int getTimer() {
        return this.characterQuest.getTimer();
    }

    public boolean isTracked() {
        return this.characterQuest.isTracked();
    }

    public int getAcceptDate() {
        return this.characterQuest.getAcceptDate();
    }

    public int getCompleteDate() {
        return this.characterQuest.getCompleteDate();
    }

    @Override
    public int hashCode() {
        return this.player.hashCode() + this.questTemplate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof Quest))
            return false;
        Quest cObj = (Quest)obj;

        return this.player.equals(cObj.player) && this.questTemplate.equals(cObj.questTemplate);
    }

    public boolean isAccepted() {
        return this.characterQuest.getAcceptDate() != 0;
    }

    public boolean isGoalAchieved() {
        return this.isGoalAchieved;
    }

    public static ServerPacket getQuestInfoData(QuestTemplate template) {
        return getQuestInfoData(template, false);
    }

    public static ServerPacket getQuestInfoData(QuestTemplate template, boolean isForAccept) {
        ServerPacket packet = new ServerPacket(isForAccept ? ServerCommands.QuestInfoForAccept : ServerCommands.QuestInfo);

        packet.add(template.getId())
                .add(template.getTitle())
                .add(template.getType())
                .add(template.getDescription())
                .add(""); // Always empty

        switch (template.getType()) {
            case Kill:
                CreatureTemplate creatureTemplate = DataStorage.CreatureTemplateRepository.get(template.getRequiredCreatureId());
                if (creatureTemplate == null) {
                    packet.add("").add("").add("");
                } else {
                    packet.add(template.getRequiredCount())
                            .add(creatureTemplate.getName())
                            .add("");
                }
                break;
            default:
                packet.add("").add("").add("");
                break;
        }
        int rewardCount = 0;

        if (template.getRewardExp() != 0) {
            packet.add(template.getRewardExp() + Locale.getString(1));
            ++rewardCount;
        }
        if (template.getRewardGold() != 0) {
            packet.add(template.getRewardGold() + Locale.getString(2));
            ++rewardCount;
        }

        ItemTemplate itemTemplate;
        if (template.getRewardItem1Count() != 0 && template.getRewardItem1Id() != 0) {
            itemTemplate = DataStorage.ItemTemplateRepository.get(template.getRewardItem1Id());
            if (itemTemplate != null) {
                packet.add(template.getRewardItem1Count() + " x " + itemTemplate.getTitle());
                ++rewardCount;
            }
        }
        if (template.getRewardItem2Count() != 0 && template.getRewardItem2Id() != 0) {
            itemTemplate = DataStorage.ItemTemplateRepository.get(template.getRewardItem2Id());
            if (itemTemplate != null) {
                packet.add(template.getRewardItem2Count() + " x " + itemTemplate.getTitle());
                ++rewardCount;
            }
        }

        while (rewardCount != 3) {
            packet.add("");
            ++rewardCount;
        }

        packet.add(0); // ??? Tutorial?

        return packet;
    }

    public ServerPacket getQuestData() {
        ServerPacket packet = new ServerPacket(ServerCommands.QuestListInfo);
        packet.add(this.questTemplate.getId())
                .add(this.questTemplate.getType())
                .add(this.questTemplate.getTitle())
                .add(this.getProgress())
                .add(this.questTemplate.getRequiredCount())
                .add(this.questTemplate.getTime() == 0 ? "" : this.getTimer())
                .add(this.characterQuest.getStatusInteger())
                .add(this.isTracked() ? "1" : "0")
                .add(this.questTemplate.getTracking());

        return packet;
    }

    public ServerPacket getUpdateQuestData() {
        ServerPacket packet = new ServerPacket(ServerCommands.UpdateQuest);
        packet.add(this.questTemplate.getId())
                .add(this.getProgress())
                .add(this.questTemplate.getTime() == 0 ? "" : this.getTimer())
                .add(this.getStatus())
                .add(this.isTracked() ? "1" : "0")
                .add(this.questTemplate.getTracking());

        return packet;
    }

    public void accept() {
        // May by already accepted
        // For example, an active quest loaded from DB
        if (!isAccepted()) {
            this.characterQuest.setAcceptDate((int)(new Date().getTime() / 1000));
            this.setStatus(QuestStatuses.Taken);
        }

        if (player.getSession() != null)
            player.getSession().send(getUpdateQuestData());

        activateHandlers();
    }

    public void complete() {
        // Send Final Text
        if (this.player.getSession() != null) {
            this.player.getSession().send(new ServerPacket(ServerCommands.QuestFinalText).add(this.questTemplate.getTitle()).add(this.questTemplate.getEndText()));
        }

        // Change quest status
        this.setStatus(QuestStatuses.Completed);
        this.characterQuest.setCompleteDate((int)(new Date().getTime() / 1000));
        if (this.player.getSession() != null) {
            this.player.getSession().send(getUpdateQuestData());
        }

        // Reward
        if (this.questTemplate.getRewardGold() != 0) {
            if (this.player.getSession() != null) {
                ServerPacket packet = new ServerPacket(ServerCommands.ServerMessage)
                        .add(1096)
                        .add(this.questTemplate.getRewardGold())
                        .add(DataStorage.CurrencyRepository.get(Currencies.Gold.getValue()).getTitle());
                this.player.getSession().send(packet);
            }
            this.player.changeCurrency(Currencies.Gold, this.questTemplate.getRewardGold());
        }
    }

    private void goalAchieved() {
        this.isGoalAchieved = true;
        deactivateHandlers();
    }

    private void activateHandlers() {
        if (this.questTemplate.getType() == QuestTypes.Kill) {
            if (this.killingBlowHandler == null) {
                this.killingBlowHandler = new KillingBlowHandler();
            }

            this.player.addKillingBlowListener(this.killingBlowHandler);
        }
    }

    private void deactivateHandlers() {
        this.player.removeKillingBlowListener(this.killingBlowHandler);
    }
}

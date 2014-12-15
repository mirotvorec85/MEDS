package org.meds.database.entity;

import org.meds.Creature;
import org.meds.Player;
import org.meds.Unit;
import org.meds.UnitTypes;
import org.meds.database.DBStorage;
import org.meds.enums.Currencies;
import org.meds.enums.QuestStatuses;
import org.meds.enums.QuestTypes;
import org.meds.net.ServerOpcodes;
import org.meds.net.ServerPacket;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("unused")
public class CharacterQuest implements Serializable
{
    private class KillingBlowHandler implements Unit.KillingBlowListener {

        @Override
        public void handleEvent(Unit.DamageEvent e) {

            CharacterQuest quest = CharacterQuest.this;

            if (e.getVictim().getUnitType() != UnitTypes.Creature)
                return;

            if (((Creature)e.getVictim()).getTemplate().getTemplateId() != quest.questTemplate.getRequiredCreatureId())
                return;

            quest.progress++;

            if (quest.player.getSession() != null) {
                quest.player.getSession().addData(quest.getUpdateQuestData());
                quest.player.getSession().addData(
                        new ServerPacket(ServerOpcodes.ServerMessage)
                        .add(337)
                        .add(quest.questTemplate.getTitle())
                        .add(quest.progress)
                        .add(quest.questTemplate.getRequiredCount()));
            }

            if (quest.progress == quest.questTemplate.getRequiredCount()) {
                quest.goalAchieved();
            }
        }
    }

    private static final long serialVersionUID = 8565314324578762825L;

    private int characterId;
    private QuestTemplate questTemplate;
    private int questTemplateId;
    private QuestStatuses status;
    private int progress;
    private int timer;
    private boolean tracked;
    private int acceptDate;
    private int completeDate;

    private boolean isGoalAchieved;

    private Player player;

    private Unit.KillingBlowListener killingBlowHandler;

    public CharacterQuest() { }

    public CharacterQuest(QuestTemplate template) {
        this.questTemplate = template;
        this.questTemplateId = template.getId();
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public int getQuestTemplateId() {
        return questTemplateId;
    }

    private void setQuestTemplateId(int questTemplateId) {
        this.questTemplateId = questTemplateId;
        this.questTemplate = DBStorage.QuestTemplateStore.get(questTemplateId);
    }

    public QuestTemplate getQuestTemplate() {
        return this.questTemplate;
    }

    public QuestStatuses getStatus(){
        return status;
    }

    private void setStatus(QuestStatuses status){
        this.status = status;
    }

    private void setStatusInteger(int statusInteger) {
        this.status = QuestStatuses.parse(statusInteger);
    }

    public int getStatusInteger() {
        return this.status.getValue();
    }

    public int getProgress() {
        return progress;
    }

    private void setProgress(int progress) {
        this.progress = progress;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public boolean isTracked() {
        return tracked;
    }

    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }

    public int getAcceptDate() {
        return acceptDate;
    }

    private void setAcceptDate(int acceptDate) {
        this.acceptDate = acceptDate;
    }

    public int getCompleteDate() {
        return completeDate;
    }

    private void setCompleteDate(int completeDate) {
        this.completeDate = completeDate;
    }

    @Override
    public int hashCode()
    {
        return this.characterId * 1000 + this.questTemplateId;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (!(obj instanceof CharacterQuest))
            return false;
        CharacterQuest cObj = (CharacterQuest)obj;

        return this.characterId == cObj.characterId && this.questTemplateId == cObj.questTemplateId;
    }

    public boolean isAccepted() {
        return this.acceptDate != 0;
    }

    public boolean isGoalAchieved() {
        return this.isGoalAchieved;
    }

    public ServerPacket getQuestData() {
        ServerPacket packet = new ServerPacket(ServerOpcodes.QuestListInfo);
        packet.add(this.questTemplateId)
                .add(this.questTemplate.getType())
                .add(this.questTemplate.getTitle())
                .add(this.progress)
                .add(this.questTemplate.getRequiredCount())
                .add(this.questTemplate.getTime() == 0 ? "" : this.timer)
                .add(this.status)
                .add(this.tracked ? "1" : "0")
                .add(this.questTemplate.getTracking());

        return packet;
    }

    public ServerPacket getUpdateQuestData() {
        ServerPacket packet = new ServerPacket(ServerOpcodes.UpdateQuest);
        packet.add(this.questTemplateId)
                .add(this.progress)
                .add(this.questTemplate.getTime() == 0 ? "" : this.timer)
                .add(this.status)
                .add(this.tracked ? "1" : "0")
                .add(this.questTemplate.getTracking());

        return packet;
    }

    public void accept(Player player) {
        // May by already accepted
        // For example, an active quest loaded from DB
        if (!isAccepted()) {
            this.acceptDate = (int)(new Date().getTime() / 1000);
            this.setStatus(QuestStatuses.Taken);
        }

        this.player = player;
        if (characterId == 0) {
            this.characterId = player.getGuid();
        }

        if (player.getSession() != null)
            player.getSession().addData(getUpdateQuestData());

        activateHandlers();
    }

    public void complete() {
        // Send Final Text
        if (this.player.getSession() != null) {
            this.player.getSession().addData(
                    new ServerPacket(ServerOpcodes.QuestFinalText)
                    .add(this.questTemplate.getTitle())
                    .add(this.questTemplate.getEndText()));
        }

        // Change quest status
        this.setStatus(QuestStatuses.Completed);
        this.setCompleteDate((int)(new Date().getTime() / 1000));
        if (this.player.getSession() != null) {
            this.player.getSession().addData(getUpdateQuestData());
        }

        // Reward
        if (this.questTemplate.getRewardGold() != 0)
        {
            if (this.player.getSession() != null) {
                this.player.getSession().addData(
                        new ServerPacket(ServerOpcodes.ServerMessage)
                                .add(1096)
                                .add(this.questTemplate.getRewardGold())
                                .add(DBStorage.CurrencyStore.get(Currencies.Gold.getValue()).getTitle()));
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

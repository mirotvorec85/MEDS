package org.meds.net.handlers;

import org.meds.Quest;
import org.meds.enums.QuestStatuses;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.QuestListFilter)
public class QuestListFilterCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;

    @Override
    public int getMinDataLength() {
        return 1;
    }

    @Override
    public void handle(ClientCommandData data) {
        boolean isHideCompleted = data.getInt(0) == 1;
        Iterator<Quest> iterator = sessionContext.getPlayer().getQuestIterator();
        while (iterator.hasNext()) {
            Quest quest = iterator.next();

            if (!quest.isAccepted()) {
                continue;
            }
            if (isHideCompleted && quest.getStatus() == QuestStatuses.Completed) {
                continue;
            }
            sessionContext.getSession().send(quest.getQuestData());
        }
    }
}

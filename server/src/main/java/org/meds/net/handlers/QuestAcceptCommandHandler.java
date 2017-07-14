package org.meds.net.handlers;

import org.meds.Quest;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.QuestAccept)
public class QuestAcceptCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;

    @Override
    public int getMinDataLength() {
        return 1;
    }

    @Override
    public void handle(ClientCommandData data) {
        int questId = data.getInt(0);
        Quest quest = sessionContext.getPlayer().getQuest(questId);
        // This quest hasn't been previously requested to accept.
        if (quest == null) {
            return;
        }

        quest.accept();
    }
}

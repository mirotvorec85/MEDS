package org.meds.net.handlers;

import org.meds.QuestInfoPacketFactory;
import org.meds.data.domain.QuestTemplate;
import org.meds.database.Repository;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.GetQuestInfo)
public class GetQuestInfoCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;
    @Autowired
    private Repository<QuestTemplate> questTemplateRepository;
    @Autowired
    private QuestInfoPacketFactory questInfoPacketFactory;

    @Override
    public int getMinDataLength() {
        return 1;
    }

    @Override
    public void handle(ClientCommandData data) {
        int questId = data.getInt(0);
        QuestTemplate template = questTemplateRepository.get(questId);
        if (template != null) {
            sessionContext.getSession().send(questInfoPacketFactory.create(template));
        }
    }
}

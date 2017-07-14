package org.meds.net.handlers;

import org.meds.data.domain.Guild;
import org.meds.database.Repository;
import org.meds.enums.SpecialLocationTypes;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.GuildLearn)
public class GuildLearnCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;
    @Autowired
    private Repository<Guild> guildRepository;

    @Override
    public int getMinDataLength() {
        return 1;
    }

    @Override
    public void handle(ClientCommandData data) {
        // Inside a Guild location only
        if (sessionContext.getPlayer().getPosition().getSpecialLocationType() != SpecialLocationTypes.MagicSchool)
            return;

        sessionContext.getPlayer().learnGuildLesson(guildRepository.get(data.getInt(0)));
    }
}

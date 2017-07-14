package org.meds.net.handlers;

import org.meds.database.DataStorage;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.ServerPacket;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.GuildLessonsInfo)
public class GuildLessonsInfoCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;
    @Autowired
    private DataStorage dataStorage;

    @Override
    public int getMinDataLength() {
        return 1;
    }

    @Override
    public void handle(ClientCommandData data) {
        ServerPacket lessonData = dataStorage.getGuildLessonInfo(data.getInt(0, -1));
        if (lessonData != null) {
            sessionContext.getSession().send(lessonData);
        }
    }
}

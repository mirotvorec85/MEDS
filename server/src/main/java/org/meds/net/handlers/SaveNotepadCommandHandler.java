package org.meds.net.handlers;

import org.meds.logging.Logging;
import org.meds.net.ClientCommandData;
import org.meds.net.ClientCommandTypes;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.SaveNotepad)
public class SaveNotepadCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;

    @Override
    public int getMinDataLength() {
        return 1;
    }

    @Override
    public void handle(ClientCommandData data) {
        // Decode from URL string
        try {
            // Cp1251 Encoding because there can be cyrillic signs
            String notes = java.net.URLDecoder.decode(data.getString(0), "Cp1251");
            sessionContext.getPlayer().setNotepadNotes(notes);
        } catch (UnsupportedEncodingException e) {
            Logging.Error.log(sessionContext.getSession().toString() + " saving notepad: URLDecoder", e);
        }
    }
}

package org.meds.net;

import org.meds.Player;

/**
 * Represents a context that holds the Session and Player objects
 * of the current client request.
 * Can be used in a PacketMessage handlers or other components
 * to retrieve the session information of the current handling operations
 * @author Romman
 */
public interface SessionContext {

    Session getSession();

    Player getPlayer();
}

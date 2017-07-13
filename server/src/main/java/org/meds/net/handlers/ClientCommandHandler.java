package org.meds.net.handlers;

import org.meds.net.ClientCommandData;

/**
 * @author Romman
 */
public interface ClientCommandHandler {

    /**
     * Gets a minimal length that allows to handle an command
     * @return minimal length or -1 if handler doesn't have the length limitation.
     */
    int getMinDataLength();

    /**
     * Indicates whether the command requires a successfully authenticated player to be handled.
     */
    boolean isAuthenticatedOnly();

    void handle(ClientCommandData data);
}

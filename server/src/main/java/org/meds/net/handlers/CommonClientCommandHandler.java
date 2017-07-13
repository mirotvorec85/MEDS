package org.meds.net.handlers;

/**
 * Represent a base class for the majority of handlers that don't have length limit and
 * require an authenticated player.
 * @author Romman
 */
public abstract class CommonClientCommandHandler implements ClientCommandHandler {

    @Override
    public int getMinDataLength() {
        return -1;
    }

    @Override
    public boolean isAuthenticatedOnly() {
        return true;
    }
}

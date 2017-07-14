package org.meds.net;

/**
 * @author Romman.
 */
public class ClientCommandHandleException extends RuntimeException {

    public ClientCommandHandleException(String message) {
        super(message);
    }

    public ClientCommandHandleException(String message, Throwable cause) {
        super(message, cause);
    }
}

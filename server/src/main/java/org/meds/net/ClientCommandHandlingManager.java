package org.meds.net;

import org.meds.net.handlers.ClientCommand;
import org.meds.net.handlers.ClientCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Romman
 */
@Component
public class ClientCommandHandlingManager {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SessionContext sessionContext;

    private Map<ClientCommandTypes, ClientCommandHandler> handlers;

    @PostConstruct
    private void init() {
        this.handlers = this.applicationContext.getBeansWithAnnotation(ClientCommand.class)
                .entrySet()
                .stream()
                .filter(entry -> {
                    // Must implement ClientCommandHandler interface
                    return entry.getValue() instanceof ClientCommandHandler;
                })
                .collect(Collectors.toMap(entry -> entry.getValue().getClass().getAnnotation(ClientCommand.class).value(),
                        entry -> (ClientCommandHandler) entry.getValue()));
    }

    /**
     * @throws ClientCommandHandleException
     */
    public void handle(ClientCommandData commandData) {
        ClientCommandTypes type = commandData.type();
        ClientCommandHandler handler = this.handlers.get(type);
        if (handler == null) {
            throw new ClientCommandHandleException("Handler for the command \"" + type + "\" not found.");
        }

        if (!sessionContext.getSession().isAuthenticated() && handler.isAuthenticatedOnly()) {
            throw new ClientCommandHandleException("Attempt to handle the command \"" +
                    type + "\" with the not authenticated session.");
        }

        if (handler.getMinDataLength() != -1 && commandData.size() < handler.getMinDataLength()) {
            String message = String.format("Command \"%s\" has the length %d, but minimal is %d. Handling aborted.",
                    type, commandData.size(), handler.getMinDataLength());
            throw new ClientCommandHandleException(message);
        }

        try {
            handler.handle(commandData);
        } catch (Exception ex) {
            throw new ClientCommandHandleException("An exception has occurred while handling the command " + type, ex);
        }
    }
}

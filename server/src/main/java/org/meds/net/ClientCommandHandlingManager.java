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
}

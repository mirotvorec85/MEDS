package org.meds.net.handlers;

import org.meds.net.ClientCommandTypes;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Indicates that annotated class is a Client Packet Command handler.
 * <p>
 * This annotation should be applied to a class that implements
 * {@link ClientCommandHandler} interface.
 *
 * @author Romman
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ClientCommand {
    ClientCommandTypes value();
}

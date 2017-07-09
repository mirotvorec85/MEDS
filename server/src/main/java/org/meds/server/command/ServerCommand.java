package org.meds.server.command;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Indicates that annotated class is a Server command handler.
 * Commands are applied via console input.
 * <p>
 * This annotation should be applied to a class that implements
 * {@link CommandHandler} interface.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ServerCommand {

    String value();
}

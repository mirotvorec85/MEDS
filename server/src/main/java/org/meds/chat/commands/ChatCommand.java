package org.meds.chat.commands;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Indicates that annotated class is a Chat command handler.
 * Commands are applied with as a chat message.
 * <p>
 * This annotation should be applied to a class that implements
 * {@link org.meds.chat.commands.ChatCommandHandler} interface.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ChatCommand {

    String value();
}

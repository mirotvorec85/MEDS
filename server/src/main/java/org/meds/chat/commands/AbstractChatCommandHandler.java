package org.meds.chat.commands;

public abstract class AbstractChatCommandHandler implements ChatCommandHandler {

    @Override
    public int getMinArgsCount() {
        return -1;
    }
}

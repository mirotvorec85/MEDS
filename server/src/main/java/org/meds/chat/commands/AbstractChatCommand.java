package org.meds.chat.commands;

public abstract class AbstractChatCommand implements ChatCommand {

    @Override
    public int getMinArgsCount() {
        return -1;
    }
}

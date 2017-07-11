package org.meds.chat.commands;

import org.meds.Player;
import org.meds.UnitParameters;
import org.meds.chat.ChatHandler;
import org.meds.enums.Parameters;
import org.meds.util.SafeConvert;
import org.springframework.beans.factory.annotation.Autowired;

@ChatCommand("parameters")
public class ParametersChatCommandHandler extends AbstractChatCommandHandler {

    @Autowired
    private ChatHandler chatHandler;

    @Override
    public void handle(Player player, String[] args) {
        int index = 0;
        if (args.length > 0) {
            index = SafeConvert.toInt32(args[0], 4);
        }

        UnitParameters.Parameter parameter = null;
        switch (index) {
            case 0:
                parameter = player.getParameters().base();
                chatHandler.sendSystemMessage(player, "Base Parameters:");
                break;
            case 1:
                parameter = player.getParameters().guild();
                chatHandler.sendSystemMessage(player, "Guild Parameters:");
                break;
            case 2:
                parameter = player.getParameters().equipment();
                chatHandler.sendSystemMessage(player, "Equipment Parameters:");
                break;
            case 3:
                parameter = player.getParameters().magic();
                chatHandler.sendSystemMessage(player, "Magic Parameters:");
                break;
        }

        if (parameter == null) {
            chatHandler.sendSystemMessage(player, "Total Parameters:");
            for (Parameters value : Parameters.values()) {
                chatHandler.sendSystemMessage(player, value.name() + " = " + player.getParameters().value(value));
            }
        } else {
            for (Parameters value : Parameters.values()) {
                chatHandler.sendSystemMessage(player, value.name() + " = " + parameter.value(value));
            }
        }
    }
}

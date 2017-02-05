package org.meds.chat.commands;

import org.meds.Player;
import org.meds.UnitParameters;
import org.meds.chat.ChatHandler;
import org.meds.enums.Parameters;
import org.meds.util.SafeConvert;

public class ParametersChatCommand extends AbstractChatCommand {

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
                ChatHandler.sendSystemMessage(player, "Base Parameters:");
                break;
            case 1:
                parameter = player.getParameters().guild();
                ChatHandler.sendSystemMessage(player, "Guild Parameters:");
                break;
            case 2:
                parameter = player.getParameters().equipment();
                ChatHandler.sendSystemMessage(player, "Equipment Parameters:");
                break;
            case 3:
                parameter = player.getParameters().magic();
                ChatHandler.sendSystemMessage(player, "Magic Parameters:");
                break;
        }

        if (parameter == null) {
            ChatHandler.sendSystemMessage(player, "Total Parameters:");
            for (Parameters value : Parameters.values()) {
                ChatHandler.sendSystemMessage(player, value.name() + " = " + player.getParameters().value(value));
            }
        } else {
            for (Parameters value : Parameters.values()) {
                ChatHandler.sendSystemMessage(player, value.name() + " = " + parameter.value(value));
            }
        }
    }
}

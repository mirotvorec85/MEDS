package org.meds.chat;

import org.meds.Group;
import org.meds.Player;
import org.meds.chat.commands.*;
import org.meds.logging.Logging;
import org.meds.net.ServerPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatCommandHandler {
    
    private final HashMap<String, ChatCommand> chatCommands;

    private final ServerPacket helpChatCommandResult;
    
    public ChatCommandHandler() {
        this.chatCommands = new HashMap<>();
        helpChatCommandResult = new ServerPacket();
        init();
    }

    private void init() {
        chatCommands.put("teleport", new TeleportChatCommand());
        chatCommands.put("set_level", new SetLevelChatCommand());
        chatCommands.put("announce", new AnnounceChatCommand());
        chatCommands.put("tlffa", new TeamLootChatCommand(Group.TeamLootModes.Regular));
        chatCommands.put("tlrandom", new TeamLootChatCommand(Group.TeamLootModes.Random));
        chatCommands.put("tlleader", new TeamLootChatCommand(Group.TeamLootModes.Leader));
        chatCommands.put("parameters", new ParametersChatCommand());

        chatCommands.put("?", new HelpChatCommand());
        chatCommands.put("info", new InfoChatCommand());
        chatCommands.put("locborn", new LocBornChatCommand());
        chatCommands.put("who", new WhoChatCommand());

        helpChatCommandResult
                .add(ChatHandler.constructSystemMessage("=============== GENERAL ==============="))
                .add(ChatHandler.constructSystemMessage("\\info"))
                .add(ChatHandler.constructSystemMessage("\\who"))
                .add(ChatHandler.constructSystemMessage("\\relax"))
                .add(ChatHandler.constructSystemMessage("\\notell"))
                .add(ChatHandler.constructSystemMessage("\\locborn"))
                .add(ChatHandler.constructSystemMessage("\\observe"))
                .add(ChatHandler.constructSystemMessage("\\scan"))
                .add(ChatHandler.constructSystemMessage("\\nomelee"))
                .add(ChatHandler.constructSystemMessage("\\balance"))
                .add(ChatHandler.constructSystemMessage("\\mail"))
                .add(ChatHandler.constructSystemMessage("\\powerup"))
                .add(ChatHandler.constructSystemMessage("\\replay"))
                .add(ChatHandler.constructSystemMessage("\\skills"))
                .add(ChatHandler.constructSystemMessage("\\noprotect"))
                .add(ChatHandler.constructSystemMessage("\\total_filter"))
                // TODO: add tips for the next commands (cost, format, etc.)
                .add(ChatHandler.constructSystemMessage("\\gra"))
                .add(ChatHandler.constructSystemMessage("\\invisible"))
                .add(ChatHandler.constructSystemMessage("\\doppel"))
                .add(ChatHandler.constructSystemMessage("\\hide_eq"))
                .add(ChatHandler.constructSystemMessage("\\compose"))
                .add(ChatHandler.constructSystemMessage("\\wimpy"))
                .add(ChatHandler.constructSystemMessage("\\sendpt"))
                .add(ChatHandler.constructSystemMessage("\\sendgold"))
                .add(ChatHandler.constructSystemMessage("\\roll"))
                .add(ChatHandler.constructSystemMessage("\\return"))
                .add(ChatHandler.constructSystemMessage("\\?"));
    }

    public void handle(Player player, String command, String commandArgs) {
        // Parsing commands
        // Each word except surrounding with quotes

        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(commandArgs);
        while(m.find()) {
            list.add(m.group(1).replace("\"", ""));
        }

        String[] args = list.toArray(new String[list.size()]);
        ChatCommand chatCommand = this.chatCommands.get(command);
        if (chatCommand == null) {
            return;
        }
        if (chatCommand.getMinArgsCount() != -1 && chatCommand.getMinArgsCount() > args.length) {
            return;
        }

        Logging.Info.log("Executing chat command \"%s\" for %s", command, player);
        chatCommand.handle(player, args);
    }

    private class HelpChatCommand extends AbstractChatCommand {

        @Override
        public void handle(Player player, String[] args) {
            if (player.getSession() != null) {
                player.getSession().sendServerMessage(1128).send(ChatCommandHandler.this.helpChatCommandResult);
            }
        }
    }
}

package org.meds;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.meds.enums.Parameters;
import org.meds.logging.Logging;
import org.meds.map.Location;
import org.meds.map.Map;
import org.meds.map.Region;
import org.meds.net.Server;
import org.meds.net.ServerCommands;
import org.meds.net.ServerPacket;
import org.meds.util.DateFormatter;
import org.meds.util.SafeConvert;

public final class ChatHandler
{
    public final static String Separator = "\u0002";
    public final static String MessageSeparator = "\u0030";
    public final static String SayChar = "\u0031";
    public final static String SystemChar = "\u0034";

    private static final HashMap<String, ChatCommand> chatCommands = new HashMap<>();

    private static final ServerPacket helpChatCommandResult = new ServerPacket();

    static {

        ChatHandler.helpChatCommandResult.add(constructSystemMessage("=============== GENERAL ==============="))
                .add(constructSystemMessage("\\info"))
                .add(constructSystemMessage("\\who"))
                .add(constructSystemMessage("\\relax"))
                .add(constructSystemMessage("\\notell"))
                .add(constructSystemMessage("\\locborn"))
                .add(constructSystemMessage("\\observe"))
                .add(constructSystemMessage("\\scan"))
                .add(constructSystemMessage("\\nomelee"))
                .add(constructSystemMessage("\\balance"))
                .add(constructSystemMessage("\\mail"))
                .add(constructSystemMessage("\\powerup"))
                .add(constructSystemMessage("\\replay"))
                .add(constructSystemMessage("\\skills"))
                .add(constructSystemMessage("\\noprotect"))
                .add(constructSystemMessage("\\total_filter"))
                // TODO: add tips for the next commands (cost, format, etc.)
                .add(constructSystemMessage("\\gra"))
                .add(constructSystemMessage("\\invisible"))
                .add(constructSystemMessage("\\doppel"))
                .add(constructSystemMessage("\\hide_eq"))
                .add(constructSystemMessage("\\compose"))
                .add(constructSystemMessage("\\wimpy"))
                .add(constructSystemMessage("\\sendpt"))
                .add(constructSystemMessage("\\sendgold"))
                .add(constructSystemMessage("\\roll"))
                .add(constructSystemMessage("\\return"))
                .add(constructSystemMessage("\\?"));

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
    }

    public static ServerPacket constructSystemMessage(String message) {
        ServerPacket packet = new ServerPacket(ServerCommands.ChatMessage);
        StringBuilder text = new StringBuilder();
        text.append(Separator).append(SystemChar).append(message);
        packet.add(text);
        return packet;
    }

    public static void sendSystemMessage(Player player, String message)
    {
        if (player == null || player.getSession() == null || message == null || message.length() == 0)
            return;

        player.getSession().send(constructSystemMessage(message));
    }

    public static void sendSystemMessage(String message)
    {
        if (message == null || message.length() == 0)
            return;

        World.getInstance().send(constructSystemMessage(message));
    }

    public static void handleSay(Player player, String message)
    {
        // Ignore empty messages
        if (message == null || message.length() == 0)
            return;

        // Player is located nowhere
        if (player.getPosition() == null) {
            Logging.Error.log(player + " says to chat" +
                    ", but he is not on the map (no location specified)");
            return;
        }

        // Message contains only whitespace
        for (int i = 0; i < message.length(); ++i)
        {
            if (message.charAt(i) != ' ')
            break;
            if (i == message.length() - 1)
            return;
        }

        // Is a command
        if (message.charAt(0) == '\\' && message.length() > 1)
        {
            String text = message.substring(1);
            String[] commandData = text.split(" ");
            String args;
            if (commandData.length > 1)
                args = message.substring(commandData[0].length() + 2);
            else
                args = "";
            handleCommand(player, commandData[0], args);
            return;
        }

        // Say this message
        ServerPacket packet = new ServerPacket(ServerCommands.ChatMessage);
        StringBuilder response = new StringBuilder();
        response.append(Separator).append(SayChar).append("[").append(player.getName()).append("]: ")
        .append(Separator).append(MessageSeparator).append(message);
        packet.add(response);

        // Send to all at the player's region
        player.getPosition().getRegion().send(packet);
    }

    public static void handleWhisper(Player player, String message)
    {
        // TODO: Implement whispering
    }

    public static void handleCommand(Player player, String command, String commandArgs)
    {
        // Parsing commands
        // Each word except surrounding with quotes

        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(commandArgs);
        while(m.find())
            list.add(m.group(1).replace("\"", ""));

        String[] args = list.toArray(new String[list.size()]);
        ChatCommand chatCommand = chatCommands.get(command);
        if (chatCommand == null)
        return;
        if (chatCommand.getMinArgsCount() != -1 && chatCommand.getMinArgsCount() > args.length)
        return;
        chatCommand.handle(player, args);
        Logging.Info.log("Executing command \"%s\" for %s", command, player);
    }

    private static abstract class ChatCommand
    {
        public int getMinArgsCount()
        {
            return -1;
        }

        public abstract void handle(Player player, String[] args);
    }

    private static class TeleportChatCommand extends ChatCommand
    {
        @Override
        public int getMinArgsCount()
        {
            return 1;
        }

        @Override
        public void handle(Player player, String[] args)
        {
            int locationId = SafeConvert.toInt32(args[0]);
            Location location = Map.getInstance().getLocation(locationId);
            if (location != null)
                player.setPosition(location);
        }
    }

    private static class SetLevelChatCommand extends ChatCommand
    {
        @Override
        public int getMinArgsCount()
        {
            return 1;
        }

        @Override
        public void handle(Player player, String[] args)
        {
            int level = SafeConvert.toInt32(args[0], -1);
            if (level < 0 || level > 360)
                return;
            player.setLevel(level);
        }
    }

    private static class AnnounceChatCommand extends ChatCommand
    {
        @Override
        public int getMinArgsCount()
        {
            return 1;
        }

        @Override
        public void handle(Player player, String[] args)
        {
            sendSystemMessage(args[0]);
        }
    }

    private static class TeamLootChatCommand extends ChatCommand {

        private Group.TeamLootModes mode;

        public TeamLootChatCommand(Group.TeamLootModes mode) {
            this.mode = mode;
        }

        @Override
        public void handle(Player player, String[] args) {
            Group group = player.getGroup();
            if (group == null || group.getLeader() != player)
                return;

            group.setTeamLootMode(this.mode);
            if (player.getSession() != null) {
                player.getSession().sendServerMessage(group.getTeamLootMode().getModeMessage())
                        .send(group.getTeamLootData());
            }
        }
    }

    private static class ParametersChatCommand extends ChatCommand {

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
                    sendSystemMessage(player, "Base Parameters:");
                    break;
                case 1:
                    parameter = player.getParameters().guild();
                    sendSystemMessage(player, "Guild Parameters:");
                    break;
                case 2:
                    parameter = player.getParameters().equipment();
                    sendSystemMessage(player, "Equipment Parameters:");
                    break;
                case 3:
                    parameter = player.getParameters().magic();
                    sendSystemMessage(player, "Magic Parameters:");
                    break;
            }

            if (parameter == null) {
                sendSystemMessage(player, "Total Parameters:");
                for (Parameters value : Parameters.values()) {
                    sendSystemMessage(player, value.name() + " = " + player.getParameters().value(value));
                }
            } else {
                for (Parameters value : Parameters.values()) {
                    sendSystemMessage(player, value.name() + " = " + parameter.value(value));
                }
            }
        }
    }

    private static class InfoChatCommand extends ChatCommand {

        @Override
        public void handle(Player player, String[] args) {

            if (player.getSession() == null)
                return;

            player.getSession().sendServerMessage(1173, DateFormatter.format(new Date()))
                .sendServerMessage(1174, Server.getServerStartTime())
                .sendServerMessage(1175, player.getSession().getLastLoginDate())
                .sendServerMessage(1176, player.getSession().getLastLoginIp())
                .sendServerMessage(1177, player.getSession().getCurrentIp());
        }
    }

    private static class HelpChatCommand extends ChatCommand {

        @Override
        public void handle(Player player, String[] args) {
            if (player.getSession() != null) {
                player.getSession().sendServerMessage(1128).send(ChatHandler.helpChatCommandResult);
            }
        }
    }

    private static class WhoChatCommand extends ChatCommand {

        @Override
        public void handle(Player player, String[] args) {
            if (player.getSession() == null || player.getPosition() == null)
                return;

            player.getSession().send(constructSystemMessage(player.getPosition().getTitle()));

            List<Player> players = player.getPosition().getPlayers();
            for (Player pl : players) {
                player.getSession().send(constructSystemMessage(pl.getName()));
            }

            player.getSession().send(constructSystemMessage("Total: " + players.size()));
        }
    }

    private static class LocBornChatCommand extends ChatCommand {

        @Override
        public void handle(Player player, String[] args) {
            if (player.getSession() == null)
                return;

            player.getSession().send(constructSystemMessage(player.getHome().getTitle()));
        }
    }
}

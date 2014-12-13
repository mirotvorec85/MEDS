package org.meds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.meds.logging.Logging;
import org.meds.map.Location;
import org.meds.map.Map;
import org.meds.map.Region;
import org.meds.net.ServerOpcodes;
import org.meds.net.ServerPacket;
import org.meds.util.SafeConvert;

public final class ChatHandler
{
    public final static String Separator = "\u0002";
    public final static String MessageSeparator = "\u0030";
    public final static String SayChar = "\u0031";
    public final static String SystemChar = "\u0034";

    private static HashMap<String, ChatCommand> chatCommands = new HashMap<String, ChatHandler.ChatCommand>();

    static
    {
        chatCommands.put("teleport", new TeleportChatCommand());
        chatCommands.put("set_level", new SetLevelChatCommand());
        chatCommands.put("announce", new AnnounceChatCommand());
        chatCommands.put("inspect_region", new InspectRegionChatCommand());
    }

    public static void sendSystemMessage(Player player, String message)
    {
        if (player == null || player.getSession() == null || message == null || message.length() == 0)
            return;

        ServerPacket packet = new ServerPacket(ServerOpcodes.ChatMessage);
        StringBuilder text = new StringBuilder();
        text.append(Separator).append(SystemChar).append(message);
        packet.add(text);
        player.getSession().addData(packet);
    }

    public static void sendSystemMessage(String message)
    {
        if (message == null || message.length() == 0)
            return;

        ServerPacket packet = new ServerPacket(ServerOpcodes.ChatMessage);
        StringBuilder text = new StringBuilder();
        text.append(Separator).append(SystemChar).append(message);
        packet.add(text);
        World.getInstance().sendToAll(packet);
    }

    public static void handleSay(Player player, String message)
    {
        // Ignore empty messages
        if (message == null || message.length() == 0)
            return;

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
        // TODO: Say to the region after Region implementation
        ServerPacket packet = new ServerPacket(ServerOpcodes.ChatMessage);
        StringBuilder response = new StringBuilder();
        response.append(Separator).append(SayChar).append("[").append(player.getName()).append("]: ")
        .append(Separator).append(MessageSeparator).append(message);
        packet.add(response);
        World.getInstance().sendToAll(packet);
    }

    public static void handleWhisper(Player player, String message)
    {
        // TODO: Implement whispering
    }

    public static void handleCommand(Player player, String command, String commandArgs)
    {
        // Parsing commands
        // Each word except surrounding with quotes

        List<String> list = new ArrayList<String>();
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
        Logging.Info.log("Executing command \"%s\" for Player \"%s\"", command, player.getName());
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

    private static class InspectRegionChatCommand extends ChatCommand
    {
        @Override
        public void handle(Player player, String[] args)
        {
            Region region = player.getPosition().getRegion();
            List<Location> locations = region.getLocations();
            for (Location location : locations)
            {
                if (location.isEmpty())
                    continue;
                sendSystemMessage(player, location.getId() + ". " + location.getTitle() + ":");
                for (Unit unit : location.getUnits())
                    sendSystemMessage(player, new StringBuilder().append("    [").append(unit.getGuid()).append("]")
                    .append(" ").append(unit.getName()).toString());
            }
        }
    }
}

package meds;

import java.util.Arrays;
import java.util.HashMap;

import meds.logging.Logging;
import meds.util.SafeConvert;

public final class ChatHandler
{
    public final static String MessageSeparator = "\u0030";
    public final static String PlayerSeparator = "\u0002";
    public final static String SayChar = "\u0031";

    private static HashMap<String, ChatCommand> chatCommands = new HashMap<String, ChatHandler.ChatCommand>();

    static
    {
        chatCommands.put("teleport", new TeleportChatCommand());
        chatCommands.put("set_level", new SetLevelChatCommand());
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
            handleCommand(player, commandData[0], Arrays.copyOfRange(commandData, 1, commandData.length));
            return;
        }

        // Say this message
        // TODO: Say to the region after Region implementation
        ServerPacket packet = new ServerPacket(ServerOpcodes.ChatMessage);
        StringBuilder response = new StringBuilder();
        response.append(PlayerSeparator).append(SayChar).append("[").append(player.getName()).append("]: ")
        .append(PlayerSeparator).append(MessageSeparator).append(message);
        packet.add(response);
        World.getInstance().sendToAll(packet);
    }

    public static void handleWhisper(Player player, String message)
    {
        // TODO: Implement whispering
    }

    public static void handleCommand(Player player, String command, String[] commandArgs)
    {
        ChatCommand chatCommand = chatCommands.get(command);
        if (chatCommand == null)
        return;
        if (chatCommand.getMinArgsCount() != -1 && chatCommand.getMinArgsCount() > commandArgs.length)
        return;
        chatCommand.handle(player, commandArgs);
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
            player.getLevel().setLevel(level);
        }
    }
}

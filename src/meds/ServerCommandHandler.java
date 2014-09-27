package meds;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import meds.logging.Logging;

public class ServerCommandHandler implements Runnable
{
    private HashMap<String, CommandHandler> commands;

    public ServerCommandHandler()
    {
        this.commands = new HashMap<String, ServerCommandHandler.CommandHandler>();
        this.commands.put("character", new CharacterCommandHandler());
        this.commands.put("help", new HelpCommandHandler());
        this.commands.put("shutdown", new ShutdownCommandHandler());
    }

    @Override
    public void run()
    {
        Logging.Debug.log("ServerCommandHandler started");
        Scanner scanner = new Scanner(System.in);
        String line;
        while(scanner.hasNext())
        {
            line = scanner.nextLine();
            if (line.isEmpty())
                continue;
            line = line.toLowerCase();
            Logging.Debug.log("Command entered: " + line);
            String[] commandArgs = line.split(" ");

            CommandHandler handler = this.commands.get(commandArgs[0]);
            if (handler == null)
            {
                System.out.println("Command \"" + commandArgs[0] + "\" not found. To see the list of all available commands type \"help\"");
                continue;
            }

            // Check subcommands
            String[] subCommands = handler.getSubCommands();
            if (subCommands != null)
            {
                if (commandArgs.length == 1)
                {
                    this.notifySubCommands(commandArgs[0], subCommands);
                    continue;
                }
                boolean subCommandFound = false;
                for (int i = 0; i < subCommands.length; ++i)
                {
                    if (subCommands[i].equals(commandArgs[1]))
                    {
                        subCommandFound = true;
                        break;
                    }
                }
                if (!subCommandFound)
                {
                    System.out.println("Unknown subcommand \"" + commandArgs[1] + "\".");
                    this.notifySubCommands(commandArgs[0], subCommands);
                    continue;
                }
            }
            handler.handle(Arrays.copyOfRange(commandArgs, 1, commandArgs.length));
        }
        scanner.close();
        Logging.Debug.log("ServerCommandHandler does not have next");
    }

    private void notifySubCommands(String command, String[] subCommands)
    {
        System.out.println("Use the following subcommands for the command \"" + command + "\":");
        for (int i = 0; i < subCommands.length; ++i)
            System.out.println(" " + subCommands[i]);
    }

    private interface CommandHandler
    {
        public String[] getSubCommands();
        public void handle(String[] args);
    }

    private class ShutdownCommandHandler implements CommandHandler
    {
        @Override
        public String[] getSubCommands()
        {
            return null;
        }
        @Override
        public void handle(String[] args)
        {
            Logging.Info.log("Shutting down...");
            Program.Exit();
        }
    }

    private class CharacterCommandHandler implements CommandHandler
    {
        @Override
        public String[] getSubCommands()
        {
            return new String[] { "ban", "create", "delete" };
        }

        @Override
        public void handle(String[] args)
        {
            Logging.Debug.log("Character command args: " + args.toString());
        }
    }

    private class HelpCommandHandler implements CommandHandler
    {
        @Override
        public String[] getSubCommands()
        {
            return null;
        }

        @Override
        public void handle(String[] args)
        {
            System.out.println("Available commands:");
            for (Entry<String, CommandHandler> entry : ServerCommandHandler.this.commands.entrySet())
            {
                System.out.println(" " + entry.getKey());
            }
        }
    }

}

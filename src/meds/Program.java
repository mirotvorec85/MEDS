package meds;

import java.io.IOException;

import meds.database.DBStorage;
import meds.database.Hibernate;
import meds.logging.Logging;
import meds.util.Random;


public class Program
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Random.initialize(); new Item.Prototype();

        if (!Configuration.load())
        {
            Logging.Info.log("Server is stopped.");
        }

        Hibernate.configure();

        DBStorage.load();
        Logging.Info.log("Database is loaded.");

        // Initialize Map instance and load its data
        Map.getInstance().load();
        Logging.Info.log("Map is loaded");

        new Player(6);

        try
        {
            Server server = new Server();
            server.Start();
        }
        catch (IOException ex)
        {
            Logging.Fatal.log("IOException while starting a server: " + ex.getMessage());
        }

        System.out.println("Server stopped.");
    }

    public static void Exit()
    {
        // TODO: a graceful server exit
        System.exit(0);
    }
}

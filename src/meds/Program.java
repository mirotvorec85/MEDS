package meds;

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
        try
        {
            Random.initialize();

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

            Server server = new Server();
            World.getInstance().createCreatures();

            server.Start();
        }
        catch (Exception ex)
        {
            Logging.Fatal.log("An exception has occured while starting the Server", ex);
        }
    }
}

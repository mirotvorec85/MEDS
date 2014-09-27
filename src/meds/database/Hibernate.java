package meds.database;

import meds.Program;
import meds.logging.Logging;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Hibernate
{
    private static SessionFactory sessionFactory;

    public static void configure()
    {
        try
        {
            Configuration configuration = new Configuration();
            // Default file name
            //cfg.addResource("hibernate.cfg.xml");
            configuration.configure();

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build();

            Hibernate.sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        catch(Exception e)
        {
            Logging.Fatal.log("Hibernate Exception: " + e.getMessage());
            e.printStackTrace();
            Program.Exit();
        }
        Logging.Info.log("Hibernate has been configured.");
    }

    public static SessionFactory getSessionFactory()
    {
        return Hibernate.sessionFactory;
    }
}

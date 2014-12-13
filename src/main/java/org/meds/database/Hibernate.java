package org.meds.database;

import org.meds.logging.Logging;

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
        catch(Exception ex)
        {
            Logging.Fatal.log("An exception occurred while confuguring Hibernate", ex);
            return;
        }
        Logging.Info.log("Hibernate has been configured.");
    }

    public static SessionFactory getSessionFactory()
    {
        return Hibernate.sessionFactory;
    }
}

package org.meds.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import org.meds.Configuration;
import org.meds.Locale;
import org.meds.data.dao.DAOFactory;
import org.meds.data.hibernate.dao.HibernateDAOFactory;
import org.meds.map.Map;
import org.meds.World;
import org.meds.database.DataStorage;
import org.meds.logging.Logging;
import org.meds.net.ServerCommandHandler;
import org.meds.net.Session;
import org.meds.util.DateFormatter;
import org.meds.util.Random;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
public class Server {

    public interface StopListener extends EventListener {
        void stop();
    }

    private class SessionDisconnect implements Session.DisconnectListener {
        @Override
        public void disconnect(Session session) {
            // Ignore disconnection while stopping
            if (Server.isStopping)
                return;

            Logging.Debug.log("Session disconnected");
            Server.this.sessions.remove(session);
        }
    }

    public static final int Build = 33554443; // 2.0.0.11

    public static final int MaxAllowedBuild = 33555200; // 2.0.3.0

    private static String serverStartTime;
    private static int startTimeMillis;
    private static Server instance;

    private static boolean isStopping;

    private static Set<StopListener> stopListeners = new HashSet<>();

    public static boolean isStopping() {
        return Server.isStopping;
    }

    public static void addStopListener(StopListener listener) {
        stopListeners.add(listener);
    }

    public static void removeStopListener(StopListener listener) {
        stopListeners.remove(listener);
    }

    public static String getServerStartTime() {
        return serverStartTime;
    }

    public static int getServerTimeMillis() {
        return (int) System.currentTimeMillis() - startTimeMillis;
    }

    public static void exit() {
        Server.isStopping = true;

        // Stop server socket
        try {
            Server.instance.serverSocket.close();
        } catch (IOException ex) {
            Logging.Error.log("IOException while stopping the server socket", ex);
        }


        // Then close all the session sockets
        for (Socket socket : Server.instance.sessions.values())
            try {
                socket.close();
            } catch (IOException ex) {
                Logging.Error.log("IOException while closing the session socket", ex);
            }

        // Stop all the listeners
        for (StopListener listener : stopListeners)
            listener.stop();

        // 5 seconds is enough for World to stop all the updated and save all the players
        Logging.Info.log("The server will be shut down in 5 seconds...");
        try {
            Thread.sleep(5000);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ServerConfiguration.class);

        try {
            Random.initialize();

            if (!Configuration.load()) {
                Logging.Info.log("Server startup aborted.");
                return;
            }

            // Set Hibernate DAO Factory
            DAOFactory.setFactory(new HibernateDAOFactory());

            DataStorage.load();
            Logging.Info.log("Database is loaded.");

            // Initialize Map instance and load its data
            Map.getInstance().load();
            Logging.Info.log("Map is loaded");

            Locale.load();

            World.getInstance().createCreatures();

            Server server = applicationContext.getBean(Server.class);
            server.start();
        } catch (Exception ex) {
            Logging.Fatal.log("An exception has occurred while starting the Server", ex);
        }
    }

    private java.util.Map<Session, Socket> sessions;
    private ServerSocket serverSocket;

    private SessionDisconnect sessionDisconnector;

    public Server() {
        Server.instance = this;
        this.sessions = new HashMap<>(100);
        this.sessionDisconnector = new SessionDisconnect();
    }

    public void start() {
        Server.startTimeMillis = (int) System.currentTimeMillis();
        Server.serverStartTime = DateFormatter.format(new Date());

        new Thread(new ServerCommandHandler(), "Server Commands handler").start();

        new Thread(World.getInstance(), "World updater").start();

        Logging.Info.log("Waiting for connections...");

        try {
            this.serverSocket = new ServerSocket(Configuration.getInt(Configuration.Keys.Port));
        } catch (IOException e) {
            Logging.Fatal.log("An exception on creating the server socket", e);
            return;
        }

        while (true) {
            try {
                Socket clientSocket = this.serverSocket.accept();
                Session session = new Session(clientSocket);
                session.addDisconnectListener(this.sessionDisconnector);
                Thread thread = new Thread(session, "Session " + clientSocket.getInetAddress().toString() + " Worker");
                Logging.Debug.log("New socket client: " + clientSocket.getInetAddress().toString());
                this.sessions.put(session, clientSocket);
                thread.start();

            } catch (IOException ex) {
                // Stopping the Server - the next exception is the expected
                if (!Server.isStopping)
                    Logging.Error.log("IO Exception while accepting a socket", ex);
                break;
            }
        }
    }
}

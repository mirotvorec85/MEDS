package meds;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import meds.logging.Logging;

public class Server
{
    public static final int Build = 16910084;

    public static final int MaxAllowedBuild = 16910095;

    private static int startTimeMillis;
    private static Server instance;

    public static int getServerTimeMillis()
    {
        return (int)System.currentTimeMillis() - startTimeMillis;
    }

    public static void disconnect(Session session)
    {
        Logging.Debug.log("Session disconnected");
        Server.instance.sessions.remove(session);
    }

    private Set<Session> sessions;
    private ServerSocket serverSocket;

    private boolean isLoaded;

    public Server() throws IOException
    {
        Server.instance = this;

        this.isLoaded = false;

        this.sessions = new HashSet<Session>(100);

        this.serverSocket = new ServerSocket(Configuration.getInt(Configuration.Keys.Port));

        new Thread(new ServerCommandHandler()).start();

        this.isLoaded = true;

        World.getInstance().createCreatures();
    }

    public void Start()
    {
        if (!this.isLoaded)
            return;

        Server.startTimeMillis = (int)System.currentTimeMillis();

        new Thread(World.getInstance()).start();

        Logging.Info.log("Waiting for connections...");

        while (true)
        {
            try
            {
                Socket clientSocket = this.serverSocket.accept();
                Session session = new Session(clientSocket);
                Thread thread = new Thread(session);
                Logging.Debug.log("New socket client: " + clientSocket.getInetAddress().toString());
                this.sessions.add(session);
                thread.start();

            }
            catch (IOException e)
            {
                Logging.Error.log("Cannot accept socket: " + e.getMessage());
            }
        }
    }
}

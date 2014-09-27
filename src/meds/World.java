package meds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import meds.database.Hibernate;
import meds.logging.Logging;

import org.hibernate.Session;

public class World implements Runnable
{
    private static World instance;

    public static World getInstance()
    {
        if (World.instance == null)
        {
            World.instance = new World();
        }

        return World.instance;
    }

    private int dayTime;

    private int tickTime;

    private HashMap<Integer, Unit> units;
    private HashMap<Integer, Player> players;

    private ServerPacket addPlayersPacket;
    private ServerPacket updatePlayersPacket;
    private ServerPacket deletePlayersPacket;

    private List<Battle> battles;
    private LinkedList<Battle> newBattles;
    private LinkedList<Battle> expiredBattles;

    private World()
    {
        this.players = new HashMap<Integer, Player>();
        this.units = new HashMap<Integer, Unit>();
        this.addPlayersPacket = new ServerPacket();
        this.updatePlayersPacket = new ServerPacket();
        this.deletePlayersPacket = new ServerPacket();

        this.battles = new ArrayList<>();
        this.newBattles = new LinkedList<Battle>();
        this.expiredBattles = new LinkedList<Battle>();

        this.dayTime = 0;
    }

    public void playerLoggedIn(Player player)
    {
        // Already in game
        if (this.players.containsKey(player.getGuid()))
            return;

        this.players.put(player.getGuid(), player);
        this.units.put(player.getGuid(), player);
        Logging.Debug.log("World adds a new player: " + player.getGuid() + "-" + player.getName());
        this.addPlayersPacket.add(ServerOpcodes.PlayersListAdd)
            .add(player.getGuid())
            .add(player.getName())
            .add(player.getLevel().getLevel())
            .add("0") // Religion Id
            .add(player.getLevel().getReligLevel())
            .add("0") // "1" is the group leader
            .add(player.getStatuses())
            .add(player.getClanId())
            .add(player.getClanMemberStatus())
            .add("0");
        Logging.Debug.log("addPlayersPacket updated");
    }

    public void playerLoggedOut(Player player)
    {
        this.units.remove(player.getGuid());
        this.players.remove(player.getGuid());
        this.deletePlayersPacket.add(ServerOpcodes.PlayersListDelete).add(player.getGuid());
        Logging.Debug.log("Player \"" + player.getName() + "\" just logged out.");
    }

    public void playerUpdated(Player player)
    {
        this.updatePlayersPacket.add(ServerOpcodes.PlayersListUpdate)
            .add(player.getGuid())
            .add(player.getLevel().getLevel())
            .add("0") // Religion Id
            .add(player.getLevel().getReligLevel())
            .add("0") // "1" is the group leader
            /* statuses here? */
            .add(player.getClanId())
            .add(player.getClanMemberStatus())
            .add("0");
    }

    public ServerPacket getOnlineData()
    {
        ServerPacket packet = new ServerPacket(ServerOpcodes.OnlineList);
        packet.add(this.players.size());
        synchronized (this.players)
        {
            for (Player player : this.players.values())
            {
                packet.add(player.getGuid())
                    .add(player.getName())
                    .add(player.getLevel().getLevel())
                    .add("0") // Religion ID
                    .add(player.getLevel().getReligLevel())
                    .add("0") // 1 WHen a player is a group leader
                    .add(player.getStatuses())
                    .add(player.getClanId())
                    .add(player.getClanMemberStatus())
                    .add("0");
            }
        }
        return packet;
    }

    public Player getPlayer(int guid)
    {
        return this.players.get(guid);
    }

    public Unit getUnit(int guid)
    {
        return this.units.get(guid);
    }

    public void addBattle(Battle battle)
    {
        this.newBattles.add(battle);
    }

    public void removeBattle(Battle battle)
    {
        this.expiredBattles.add(battle);
    }

    /**
     * Gets a Player if it was already logged or creates a new instance for further logging.
     * This method is called when new instance of Session class tries to find its Player instance.
     */
    public Player getOrCreatePlayer(int playerGuid)
    {
        Player player = this.players.get(playerGuid);
        if (player != null)
            return player;

        player = new Player(playerGuid);

        // Error occurred while player was creating or loading
        if (player.create() == 0)
        {
            return null;
        }

        return player;
    }

    @SuppressWarnings("unchecked")
    public void createCreatures()
    {
        // Load and spawn all the Creatures
        Session session = Hibernate.getSessionFactory().openSession();
        List<Creature> creatures = session.createCriteria(Creature.class).list();
        session.close();
        for (Creature creature : creatures)
        {
            // For some reasons can not create this creature
            if (creature.create() == 0)
                continue;

            creature.spawn();
        }
        Logging.Info.log("Creatures have been loaded. Count: " + this.units.size());
    }

    public void unitCreated(Unit unit)
    {
        this.units.put(unit.getGuid(), unit);
    }

    public ServerPacket getDayTimeData()
    {
        String day = this.dayTime < 360000 ? "0" : "1";
        String time = this.dayTime < 360000 ? Integer.toString(this.dayTime / 1000) : Integer.toHexString(this.dayTime / 1000 - 360);

        return new ServerPacket(ServerOpcodes.DayTime)
            .add(day)
            .add(time);
    }

    public void addDataToAll(ServerPacket packet)
    {
        synchronized (this.players)
        {
            for (Player player : this.players.values())
                if (player.getSession() != null)
                    player.getSession().addData(packet);
        }
    }

    public void sendToAll(ServerPacket packet)
    {
        synchronized (this.players)
        {
            for (Player player : this.players.values())
                if (player.getSession() != null)
                    player.getSession().send(packet);
        }
    }

    @Override
    public void run()
    {
        try
        {
            long lastTickDuration = 0;
            long sleepTime = 0;

            do
            {
                // Stop the thread for (2000 - world update time)
                // As a result the whole update-sleep cycle takes exactly 2 seconds
                sleepTime = 2000 - lastTickDuration;
                // Minimal sleep time is 50 ms
                if (sleepTime < 50)
                    sleepTime = 50;

                Logging.Debug.log("World sleeping time: " + sleepTime);
                Thread.sleep(sleepTime);

                this.tickTime = (int)sleepTime;

                // Assign last tick to the current system time
                lastTickDuration = Server.getServerTimeMillis();

                this.update(this.tickTime);

                // How much time takes the world update
                lastTickDuration = Server.getServerTimeMillis() - lastTickDuration;

            } while(true);
        }
        catch(Exception ex)
        {/*
            StackTraceElement[] ste = ex.getStackTrace();
            String stackTrace = "";
            for (int i = 0; i < ste.length; ++i)
                stackTrace += ste[i].toString() + "\n";

            Logging.Fatal.log("Fatal error while updating server Tact (%d). \nError Type: %s.\nMessage: %s\nStack Trace:\n%s", this.tickTime, ex.getClass().toString(), ex.getMessage(), stackTrace);
            Program.Exit();*/
            ex.printStackTrace();
        }
    }

    public void update(int time)
    {
        Logging.Debug.log("World update starts; Diff time: %d", time);

        // Set new Day Time
        this.dayTime += time;
        // Night begins
        if (this.dayTime >= 360000)
        {
            addDataToAll(getDayTimeData());
        }
        // New day begins
        else if (this.dayTime >= 720000)
        {
            this.dayTime -= 720000;
            addDataToAll(getDayTimeData());
        }

        // Update all units (Creatures and Players)
        synchronized (this.units)
        {
            for (java.util.Map.Entry<Integer, Unit> entry : this.units.entrySet())
                entry.getValue().update(time);
        }

        // Add new battles
        synchronized (this.newBattles)
        {
            this.battles.addAll(this.newBattles);
            this.newBattles.clear();
        }

        // Remove ended battles
        synchronized (this.expiredBattles)
        {
            this.battles.removeAll(expiredBattles);
            this.expiredBattles.clear();
        }

        // Update battle process
        synchronized (this.battles)
        {
            for (Battle battle : this.battles)
                battle.update(time);
        }

        // Update online lists
        synchronized (this.deletePlayersPacket)
        {
            if (!this.deletePlayersPacket.isEmpty())
            {
                this.addDataToAll(this.deletePlayersPacket);
                this.deletePlayersPacket.clear();
            }
        }

        synchronized (this.updatePlayersPacket)
        {
            if (!this.updatePlayersPacket.isEmpty())
            {
                this.addDataToAll(this.updatePlayersPacket);
                this.updatePlayersPacket.clear();
            }
        }

        synchronized (this.addPlayersPacket)
        {
            if (!this.addPlayersPacket.isEmpty())
            {
                this.addDataToAll(this.addPlayersPacket);
                Logging.Debug.log("addPlayersPacket added to all players: " + this.addPlayersPacket.toString());
                this.addPlayersPacket.clear();
                Logging.Debug.log("addPlayersPacket cleared. Current content: " + this.addPlayersPacket.toString());

            }
            else
            {
                Logging.Debug.log("addPlayersPacket is empty");
            }
        }

        // Update locations data (Movement, Unit list, etc.)
        Map.getInstance().update(time);

        // Add Server Time Data and send the packet
        synchronized (this.players)
        {
            for (java.util.Map.Entry<Integer, Player> entry : this.players.entrySet())
                if (entry.getValue().getSession() != null)
                    entry.getValue().getSession().addData(new ServerPacket(ServerOpcodes.ServerTime).add(Server.getServerTimeMillis())).send();
        }

        Logging.Debug.log("World update ends. Server Time: " + Server.getServerTimeMillis());
    }
}

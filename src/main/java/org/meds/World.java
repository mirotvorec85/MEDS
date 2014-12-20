package org.meds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.meds.map.Map;
import org.meds.net.Server;
import org.meds.net.Server.StopListener;
import org.meds.database.Hibernate;
import org.meds.logging.Logging;

import org.hibernate.Session;
import org.meds.net.ServerOpcodes;
import org.meds.net.ServerPacket;

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

    /**
     * Indicating whether the world is in stopping process
     */
    private boolean isStopping;

    private World()
    {
        this.players = new HashMap<>();
        this.units = new HashMap<>();
        this.addPlayersPacket = new ServerPacket();
        this.updatePlayersPacket = new ServerPacket();
        this.deletePlayersPacket = new ServerPacket();

        this.battles = new ArrayList<>();
        this.newBattles = new LinkedList<>();
        this.expiredBattles = new LinkedList<>();

        this.dayTime = 0;

        Server.addStopListener(new StopListener()
        {

            @Override
            public void stop()
            {
                // Set isStopping value and the World.stop() method
                // will be called just before the next update.
                World.this.isStopping = true;
            }
        });
    }

    public void playerLoggedIn(Player player)
    {
        // Due to possible delay this may happen
        if (this.isStopping)
            return;

        // Already in game
        if (this.players.containsKey(player.getGuid()))
            return;

        this.players.put(player.getGuid(), player);
        this.units.put(player.getGuid(), player);
        Logging.Debug.log("World adds a new player: " + player.getGuid() + "-" + player.getName());
        this.addPlayersPacket.add(ServerOpcodes.PlayersListAdd)
            .add(player.getGuid())
            .add(player.getName())
            .add(player.getLevel())
            .add(player.getReligion())
            .add(player.getReligLevel())
            .add(player.getGroup() != null && player.getGroup().getLeader() == player ? "1" : "0")
            .add(player.getStatuses())
            .add(player.getClanId())
            .add(player.getClanMemberStatus())
            .add("0"); // Religion Status
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
            .add(player.getLevel())
            .add(player.getReligion())
            .add(player.getReligLevel())
            // Next value sometimes is 4 or 5 (possibly related to pets)
            .add(player.getGroup() != null && player.getGroup().getLeader() == player ? "1" : "0")
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
                    .add(player.getLevel())
                    .add(player.getReligion())
                    .add(player.getReligLevel())
                    .add(player.getGroup() != null && player.getGroup().getLeader() == player ? "1" : "0")
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
        String time = this.dayTime < 360000 ? Integer.toString(this.dayTime / 1000) : Integer.toString(this.dayTime / 1000 - 360);

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

            try
            {
                Thread.sleep(sleepTime);

                if (this.isStopping)
                {
                    stop();
                    return;
                }

                this.tickTime = (int)sleepTime;

                // Assign last tick to the current system time
                lastTickDuration = Server.getServerTimeMillis();

                this.update(this.tickTime);
            }
            catch(InterruptedException ex)
            {
                Logging.Error.log("A Thread error in World run ", ex);
            }
            catch(Exception ex)
            {
                Logging.Error.log("An error while updating server Tact " + this.tickTime, ex);
            }

            // How much time takes the world update
            lastTickDuration = Server.getServerTimeMillis() - lastTickDuration;
        } while(true);
    }

    private void stop()
    {
        Logging.Info.log("Stopping the World");
        // Save of the players to DB
        for (Player player : this.players.values())
            player.save();
    }

    public void update(int time)
    {
        Logging.Debug.log("World update starts; Diff time: %d", time);

        // Set new Day Time
        this.dayTime += time;
        // Night begins
        if (this.dayTime - time < 360000 && this.dayTime >= 360000)
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
                this.addPlayersPacket.clear();
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

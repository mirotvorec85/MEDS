package org.meds.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

import org.meds.*;
import org.meds.Item.Prototype;
import org.meds.database.DBStorage;
import org.meds.database.entity.*;
import org.meds.database.entity.Character;
import org.meds.enums.*;
import org.meds.logging.Logging;
import org.meds.map.Location;
import org.meds.map.Map;
import org.meds.map.Region;
import org.meds.map.Shop;
import org.meds.util.MD5Hasher;
import org.meds.util.Random;
import org.meds.util.SafeConvert;

public class Session implements Runnable
{
    public interface DisconnectListener extends EventListener
    {
        public void disconnect(Session session);
    }

    private static Set<Session> sessionsToSend;

    static {
        sessionsToSend = new HashSet<>();
    }

    public static void sendBuffers() {
        synchronized (Session.sessionsToSend) {
            Iterator<Session> iterator = Session.sessionsToSend.iterator();
            while (iterator.hasNext()) {
                iterator.next().send();
                iterator.remove();
            }
        }
    }

    /**
     * Related Socket for this session.
     */
    private Socket socket;
    private HashMap<ClientOpcodes, OpcodeHandler> opcodeHandlers;

    private Player player;

    private ServerPacket packetBuffer;

    /**
     * Indicating whether the player passed login verification successful and loaded from DB.
     */
    private boolean isAuthenticated;

    /**
     * Session key.
     */
    private int key;

    private Set<DisconnectListener> listeners;

    private String sessionToString;

    public Session(Socket socket)
    {
        this.socket = socket;

        this.listeners = new HashSet<>();

        this.opcodeHandlers = new HashMap<>();
        this.opcodeHandlers.put(ClientOpcodes.Verification, new VerificationOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.Login, new LoginOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.Ready, new ReadyOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.Ping, new PingOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.Movement, new MovementOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.PutMoney, new PutMoneyOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.TakeMoney, new TakeMoneyOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.BankExchange, new BankExchangeOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.Attack, new AttackOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.UseMagic, new UseMagicOpcodeHander());
        this.opcodeHandlers.put(ClientOpcodes.Relax, new RelaxOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GuildLearn, new GuildLearnOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GetGuildLevels, new GetGuildLevelsOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.Say, new SayOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GetItemInfo, new GetItemInfoOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.SwapItem, new SwapItemsOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.LootCorpse, new LootCorpseOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.EnterShop, new EnterShopOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.SellItem, new SellItemOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.BuyItem, new BuyItemOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.SetAutoLoot, new SetAutoLootOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GetInn, new GetInnOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.InnStore, new InnStoreOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.InnGet, new InnGetOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.Whisper, new WhisperOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.DestroyItem, new DestroyItemOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.UseItem, new UseItemOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.QuestListFilter, new QuestListFilterOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GetQuestInfo, new GetQuestInfoOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.QuestInfoForAccept, new GetQuestInfoForAcceptOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.QuestAccept, new QuestAcceptOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.SetAutoSpell, new SetAutoSpellOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.EnterStar, new EnterStarOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.SetHome, new SetHomeOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GetLocationInfo, new LocationInfoOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.RegionLocations, new RegionLocationsOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GuildLessonsInfo, new GuildLessonsInfoOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.LearnGuildInfo, new LearnGuildInfoOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GroupCreate, new GroupCreateOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GroupSettingsChange, new GroupSettingsChangeOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GroupJoin, new GroupJoinOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GroupDisband, new GroupDisbandOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GroupQuit, new GroupQuitOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GroupKick, new GroupKickOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GroupChangeLeader, new GroupChangeLeaderOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.GetTrade, new GetTradeOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.TradeUpdate, new TradeUpdateOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.TradeApply, new TradeApplyOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.TradeCancel, new TradeCancelOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.SetAsceticism, new SetAsceticismOpcodeHandler());

        this.packetBuffer = new ServerPacket();

        this.key = Random.nextInt();

        this.sessionToString = "Session [" + this.socket.getInetAddress().toString() + "]: ";
    }

    public void addDisconnectListener(DisconnectListener listener)
    {
        this.listeners.add(listener);
    }

    public void removeDisconnectListener(DisconnectListener listener)
    {
        this.listeners.remove(listener);
    }

    @Override
    public void run()
    {
        try
        {
            InputStream is = this.socket.getInputStream();
            int bufferSize = 1024;

            while (true)
            {
                int receivedSize = 0;
                String receivedString = "";
                byte[] buffer = new byte[bufferSize];
                do
                {
                    receivedSize = is.read(buffer);
                    // End Of Stream / Socket is closed
                    if (receivedSize == -1)
                    {
                        Logging.Debug.log(toString() + "Received -1");
                        disconnect();
                        return;
                    }
                    receivedString += new String(Arrays.copyOf(buffer, receivedSize), "Unicode");
                }
                while (receivedSize == bufferSize);
                Logging.Debug.log(toString() + "Received string: " + receivedString);

                ClientPacket packet = new ClientPacket(receivedString);

                for (PacketOpcode opcode : packet.getPacketOpcodes())
                {
                    if (!opcode.isValid())
                        continue;
                    ClientOpcodes clientOpcode = ClientOpcodes.parse(opcode.getOpcode());
                    if (clientOpcode == null)
                    {
                        Logging.Warn.log(toString() + "Received unknown opcode \"" + opcode.getOpcode() + "\".");
                        continue;
                    }

                    OpcodeHandler handler = this.opcodeHandlers.get(clientOpcode);
                    if (handler == null)
                    {
                        Logging.Warn.log(toString() + "Handler for the opcode \"" + clientOpcode + "\" not found.");
                        continue;
                    }
                    if (!this.isAuthenticated && handler.isAuthenticatedOnly())
                    {
                        Logging.Warn.log(toString() + "Attempt to handle the opcode \"" +
                                clientOpcode + "\" with the not authenticated session.");
                        continue;
                    }

                    String[] data = opcode.getData();
                    if (handler.getMinDataLength() != -1 && data.length < handler.getMinDataLength())
                    {
                        Logging.Warn.log(toString() + "Opcode \"" + clientOpcode + "\" has the length " + data.length +
                                ", but minimal is " + handler.getMinDataLength() + ". Handling aborted.");
                        continue;
                    }

                    try
                    {
                        handler.handle(data);
                    }
                    catch(Exception ex)
                    {
                        Logging.Error.log( toString() + "An exception has occurred while handling the opcode " +
                                clientOpcode.toString(), ex);
                        continue;
                    }
                }
                Session.sendBuffers();
            }
        }
        catch (IOException e)
        {
            // Then the Server is stopping this exception is the expected
            if (!Server.isStopping())
                Logging.Error.log(toString() + "An exception while reading a socket.", e);
        }
    }

    private void disconnect()
    {
        try
        {
            this.socket.close();
        }
        catch (IOException ex)
        {
            Logging.Error.log(toString() + "IOException while trying to close the Session socket", ex);
        }

        for (DisconnectListener listener : this.listeners)
            listener.disconnect(this);

        this.listeners.clear();
    }

    /**
     * Sends an accumulated packet buffer for the current session.
     */
    private void send() {
        if (this.packetBuffer == null || this.packetBuffer.isEmpty())
            return;

        OutputStream os;
        try {
            os = this.socket.getOutputStream();
            byte[] bytes = packetBuffer.getBytes();
            os.write(bytes);
            Logging.Debug.log(toString() + "Sending data: " + packetBuffer.toString().replace('\u0000', '\n'));
        } catch (IOException e) {
            Logging.Error.log(toString() + "IOException while writing to a socket: " + e.getMessage());
        } finally {
            // Clean it anyway
            this.packetBuffer.clear();
        }
    }

    public Session send(ServerPacket packet) {
        this.packetBuffer.add(packet);
        Session.sessionsToSend.add(this);
        return this;
    }

    public Session sendServerMessage(int messageId, String... values) {
        ServerPacket packet = new ServerPacket(ServerOpcodes.ServerMessage).add(messageId);
        for (String string : values)
            packet.add(string);
        this.send(packet);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Session session = (Session) o;

        return this.key == session.key
                && socket != null ? socket.equals(session.socket) : session.socket != null;

    }

    @Override
    public int hashCode() {
        return this.socket != null ? socket.hashCode() + this.key : this.key;
    }

    @Override
    public String toString() {
        return this.sessionToString;
    }

    private abstract class OpcodeHandler {
        /**
         *  Gets a minimal length that allows to handle an opcode
         */
        public int getMinDataLength() {
            // No limit
            return -1;
        }

        /**
         * Indicates whether an opcode may be handled for only logged players.
         */
        public boolean isAuthenticatedOnly() {
            return true;
        }

        public abstract void handle(String[] data);
    }

    private class VerificationOpcodeHandler extends OpcodeHandler
    {
        @Override
        public boolean isAuthenticatedOnly()
        {
            return false;
        }

        @Override
        public void handle(String[] data)
        {
            ServerPacket packet = new ServerPacket(ServerOpcodes.Version);
            // Checking Client version
            if (data.length == 0)
            {
                packet.add(0);
            }
            else
            {
                int clientBuild = SafeConvert.toInt32(data[0]);
                if (clientBuild < Server.Build || clientBuild > Server.MaxAllowedBuild)
                {
                    packet.add(0);
                }
                else
                {
                    packet.add(Server.Build).add(Session.this.key);
                }
            }

            send(packet);
        }
    }

    private class LoginOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 2;
        }

        @Override
        public boolean isAuthenticatedOnly()
        {
            return false;
        }

        @Override
        public void handle(String[] data)
        {
            /*
             * Data Structure
             * data[0]: Login name
             * data[1]: 64 bytes hash
             *          first 32 bytes - MD5((MD5(PASSWORD) + SESSION_KEY)
             * */

            /*
             * Sometimes the next data is divided into separates packets
             * for ex.:
             * 1) login_result, cs
             * 2) kinf, mi, gi, si
             * 3) parameters data: sti, hp, exp, eq, inv etc.
             * 4) location data: tl, loc, li, pss etc.
             * */

            // Response packet starts with login_result
            ServerPacket packet = new ServerPacket(ServerOpcodes.LoginResult);
            String playerLogin = data[0].toLowerCase();
            Character character = DBStorage.findCharacter(playerLogin);

            // Player is not found
            // Sending "Wrong login or password" result
            if (character == null)
            {
                packet.add(LoginResults.WrongLoginOrPassword);
                send(packet);
                return;
            }

            // Check Password
            String receivedPasswordHash = data[1].substring(0, 32);
            String actualPassKeyHash = MD5Hasher.ComputeHash(character.getPasswordHash() + Session.this.key);

            // Hash does not match
            if (!receivedPasswordHash.equalsIgnoreCase(actualPassKeyHash))
            {
                packet.add(LoginResults.WrongLoginOrPassword);
                send(packet);
                return;
            }

            // Create a Player instance with found id
            Session.this.player = World.getInstance().getOrCreatePlayer(character.getId());
            // Something happened and player can not be created
            if (Session.this.player == null)
            {
                packet.add(LoginResults.InnerServerError);
                send(packet);
                return;
            }

            packet.add(LoginResults.OK);

            // Add New Messages
            if (DBStorage.NewMessageStore.size() != 0)
            {
                packet.add(ServerOpcodes.MessageList);
                for (NewMessage message : DBStorage.NewMessageStore.values())
                {
                    packet.add(message.getId()).add(message.getTypeId()).add(message.getMessage());
                }
            }

            // Unknown "cs" values
            packet.addData(ServerOpcodes._cs, "44", "0").addData(ServerOpcodes._cs, "45", "0").addData(ServerOpcodes._cs, "46", "0")
                .addData(ServerOpcodes._cs, "47", "0").addData(ServerOpcodes._cs, "48", "0").addData(ServerOpcodes._cs, "49", "0");

            packet.addData(ServerOpcodes.ClanInfo, "1", "1", "0", "Clan Name")
                .add(Session.this.player.getMagicData())
                .add(Session.this.player.getSkillData())
                .add(Session.this.player.getGuildData());

            // NOTE: Sometimes the data above is sent as a separate packet

            packet.add(Session.this.player.getCurrencyData())
                .add(Session.this.player.getParametersData())
                .addData(ServerOpcodes.BattleState, BattleStates.NoBattle.toString())
                .add(Session.this.player.getHealthManaData())
                .add(Session.this.player.getLevelData());

            send(packet);
            packet.clear();

            /*
             * No sharp data after 1.2.7.6
            // sharp "#"
            packet.send(ServerOpcodes.Sharp, "0", "0");
             * */

            /*
             * Do not remember what is this
            // msg
            String[] msg = new String[] { "msg", "\u00024" };
             * */

            // Empty Item info
            packet.addData(ServerOpcodes.ItemInfo, "");

            packet.add(Session.this.player.getInventory().getEquipmentData());

            // Again?? Why???
            packet.add(Session.this.player.getParametersData());

            // BonusMagicParameters? Why?
            packet.addData(ServerOpcodes.BonusMagicParameter, "10", "0")
                .addData(ServerOpcodes.BonusMagicParameter, "15", "0")
                .addData(ServerOpcodes.BonusMagicParameter, "16", "0")
                .addData(ServerOpcodes.BonusMagicParameter, "17", "0");

            // TODO: add cm datas here (current available magic spells)

            packet.add(Session.this.player.getInventory().getInventoryData());

            // Unknown Datas
            packet.addData(ServerOpcodes._wg, "84", "147"); // Weight
            // Possibly extended inventory price data
            packet.addData(ServerOpcodes._invt, "0", "5 платины");
            packet.addData(ServerOpcodes.AutoSpell, Integer.toString(Session.this.player.getAutoSpell())); // Default magic
            packet.addData(ServerOpcodes._s0, "");

            if (Session.this.player.isRelax())
                packet.add(ServerOpcodes.RelaxOn);
            else
                packet.add(ServerOpcodes.RelaxOff);

            packet.addData(ServerOpcodes._lh0, "");

            // TODO: add auras
            // TODO: add quest statuses

            // NOTE: Sometimes the data above is sended as a separate packet

            packet.add(Session.this.player.getAchievementData());

            // prot1 and prot2
            packet.addData(ServerOpcodes._prot1, "0").addData(ServerOpcodes._prot2, "0");

            // Last corpse location (Skull icon at the cell with this location)
            packet.addData(ServerOpcodes._tc, "1997");

            // Unknown
            packet.addData(ServerOpcodes._hs, "0");

            // TODO: implement Professions
            packet.addData(ServerOpcodes.Professions, "10", "Земледелие", "0", "0", "Сбор Урожая", "0", "0", "Переработка", "0", "0", "Экстракция",
                            "0", "0", "Горное Дело", "0", "0", "Воздухоплавание", "0", "0", "Травничество", "1", "69",
                            "Алхимия", "0", "26", "Охота", "0", "0", "Рыбная ловля", "0", "0", "Ловкость Рук", "0", "0");

            // Unknown
            packet.addData(ServerOpcodes._omg, "7", "0", "0");

            // TODO: Implement notepad
            packet.addData(ServerOpcodes._mem, "This is your notepad.");

            packet.add(World.getInstance().getDayTimeData());

            // "omg" again but different numbers
            packet.addData(ServerOpcodes._omg, "9", "1", "0");

            // Possibly sleep mode
            packet.addData(ServerOpcodes._zzz, "0");

            // Unknown
            packet.addData(ServerOpcodes._fpi, "0");
            packet.addData(ServerOpcodes._swf, "0");
            packet.addData(ServerOpcodes._fex, "0", "0");

            // "wg" ??? Again???
            packet.addData(ServerOpcodes._wg, "84", "147");

            packet.add(Session.this.player.getPosition().getData());
            packet.add(Session.this.player.getPosition().getCorpseData());
            // TODO: Send the neighbour locations info here

            // Unknown
            // But here is my email and link to the official webshop page
            packet.addData(ServerOpcodes._hoi, "http://ds-dealer.ru/dsrus/index.php?u=", "email@email.com", "0");

            packet.add(World.getInstance().getOnlineData());

            /*
            // Mentor
            packet.send(ServerOpcodes._mmy, "Mentor Name");
             * */

            // Send the custom welcome message
            packet.addData(ServerOpcodes.ServerMessage, "5001");

            Session.this.isAuthenticated = true;
            send(packet);

            // Change String representation of the Session
            Session.this.sessionToString = "Session [" + Session.this.player.getName() + "]: ";
        }
    }

    private class ReadyOpcodeHandler extends OpcodeHandler
    {
        @Override
        public void handle(String[] data)
        {
            Session.this.player.logIn(Session.this);
            World.getInstance().playerLoggedIn(Session.this.player);
        }
    }

    private class PingOpcodeHandler extends OpcodeHandler {

        @Override
        public void handle(String[] data) {
            // PING!:)
        }
    }

    private class MovementOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            // Battle does not allow movement
            if (Session.this.player.isInCombat())
                return;

            MovementDirections direction = MovementDirections.parse(SafeConvert.toInt32(data[0], -1));
            if (direction == null)
                return;

            Map.getInstance().registerMovement(Session.this.player, direction);
        }
    }

    private class PutMoneyOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            int amount = SafeConvert.toInt32(data[0], -1);
            // Absent amount means depositting all the gold
            if (amount == -1)
                amount = Session.this.player.getCurrencyAmount(Currencies.Gold);

            Session.this.player.depositMoney(amount);
        }
    }

    private class TakeMoneyOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            int amount = SafeConvert.toInt32(data[0], -1);

            // Absent amount means withdrawing all the gold
            if (amount == -1)
                amount = Session.this.player.getCurrencyAmount(Currencies.Bank);

            Session.this.player.withdrawMoney(amount);
        }
    }

    private class BankExchangeOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            // TODO: implement Player.bankExchange
            //Session.this.player.bankExchange(SafeConvert.toInt32(data[0]));

            //send(Session.this.player.getCurrencyData());
        }
    }

    private class AttackOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            /*
             * data[0] = Attack type ("udar", "run")
             * data[1] = victim GUID
             */
            if (data[0].equals("run"))
            {
                Session.this.player.runAway() ;
                return;
            }
            else if (data[0].equals("udar"))
            {
                if (data.length < 2)
                    return;

                String victimGuid = data[1];
                // TODO: Find out why
                // HACK: remove last symbol of the victim guid
                victimGuid = victimGuid.substring(0, victimGuid.length() - 1);
                Unit victim = World.getInstance().getUnit(SafeConvert.toInt32(victimGuid));
                // target is not found
                if (victim == null)
                    return;

                Session.this.player.interact(victim);
            }
        }
    }

    private class UseMagicOpcodeHander extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 2;
        }

        @Override
        public void handle(String[] data)
        {
            int spellId = SafeConvert.toInt32(data[0]);
            int targetGuid = SafeConvert.toInt32(data[1]);
            Session.this.player.useMagic(spellId, targetGuid);
        }
    }

    private class RelaxOpcodeHandler extends OpcodeHandler
    {
        @Override
        public void handle(String[] data)
        {
            /*
             * data[0] - "0".
             */
            Session.this.player.castSpell(60); // Cast Relax spell
        }
    }

    private class GuildLearnOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            // Inside a Guild location only
            if (Session.this.player.getPosition().getSpecialLocationType() != SpecialLocationTypes.MagicSchool)
                return;

            Session.this.player.learnGuildLesson(DBStorage.GuildStore.get(SafeConvert.toInt32(data[0])));
        }
    }

    private class GetGuildLevelsOpcodeHandler extends OpcodeHandler
    {
        @Override
        public void handle(String[] data)
        {
            send(Session.this.player.getGuildLevelData());
        }
    }

    private class SayOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            ChatHandler.handleSay(Session.this.player, data[0]);
        }
    }

    private class GetItemInfoOpcodeHandler extends OpcodeHandler
    {
        @Override
        public void handle(String[] data)
        {
            ServerPacket packet = new ServerPacket();
            int templateId;
            int modification;
            for (int i = 1; i < data.length; i += 2)
            {
                templateId = SafeConvert.toInt32(data[i - 1]);
                modification = SafeConvert.toInt32(data[i]);
                Item.getItemInfo(templateId, modification, packet);
            }
            send(packet);
        }
    }

    private class SwapItemsOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 3;
        }

        @Override
        public void handle(String[] data)
        {
            int slot1 = SafeConvert.toInt32(data[0]);
            int slot2 = SafeConvert.toInt32(data[1]);
            int count = SafeConvert.toInt32(data[2]);

            Session.this.player.getInventory().swapItem(slot1, slot2, count);
        }
    }

    private class LootCorpseOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 3;
        }

        @Override
        public void handle(String[] data)
        {
            int guid = SafeConvert.toInt32(data[0], 0);

            int itemModification = SafeConvert.toInt32(data[1]);
            int itemDurability = SafeConvert.toInt32(data[2]);

            // Do not know why but always true
            Session.this.send(new ServerPacket(ServerOpcodes.GetCorpse).add("true"));

            // TODO: sound 26 on gold collect. Sound 27 on item collect

            // Loot a corpse
            if (guid > 0)
            {
                Corpse corpse = Session.this.player.getPosition().getCorpse(guid);
                if (corpse == null)
                    return;

                Session.this.player.lootCorpse(corpse);
            }
            // Pick up an item
            else if (guid < 0)
            {
                Prototype proto = new Prototype(-guid, itemModification, itemDurability);
                Item item = Session.this.player.getPosition().getItem(proto);
                if (item == null)
                    return;
                int itemCount = item.getCount();
                if (Session.this.player.getInventory().tryStoreItem(item))
                {
                    Session.this.sendServerMessage(1014, itemCount > 1 ? itemCount + " " : "", item.Template.getTitle());
                    Session.this.player.getPosition().send(Session.this.player, new ServerPacket(ServerOpcodes.ServerMessage).add("1026").add(Session.this.player.getName()).add(itemCount > 1 ? itemCount + " " : "").add(item.Template.getTitle()));
                    if (item.getCount() == 0)
                        Session.this.player.getPosition().removeItem(item);
                }
                else
                {
                    Session.this.sendServerMessage(1001, item.Template.getTitle());
                }
            }
        }
    }

    private class EnterShopOpcodeHandler extends OpcodeHandler
    {
        @Override
        public void handle(String[] data)
        {
            Location location = Session.this.player.getPosition();
            if (location.getSpecialLocationType() == SpecialLocationTypes.Generic)
                return;

            Shop shop = Map.getInstance().getShop(location.getSpecialLocationId());
            if (shop == null)
                return;

            Session.this.send(shop.getData());
        }
    }

    private class SellItemOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 5;
        }

        @Override
        public void handle(String[] data)
        {
            Prototype proto = new Prototype(SafeConvert.toInt32(data[0]), SafeConvert.toInt32(data[1], -1), SafeConvert.toInt32(data[2], -1));
            int count = SafeConvert.toInt32(data[3], -1);
            // int unk5 = SafeConvert.ToInt32(data[4]); // Always 0
            if (proto.getTemplateId() == 0 || count == -1)
                return;

            // Player must be at shop
            if (player.getPosition().getSpecialLocationType() == SpecialLocationTypes.Generic)
                return;

            Shop shop = Map.getInstance().getShop(player.getPosition().getSpecialLocationId());
            // There is no shop with this id
            if (shop == null)
                return;

            if (shop.buyItem(player, proto, count) && player.getSession() != null)
                player.getSession().send(shop.getData());
        }
    }

    private class BuyItemOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 5;
        }

        @Override
        public void handle(String[] data)
        {
            Prototype proto = new Prototype(SafeConvert.toInt32(data[0]), SafeConvert.toInt32(data[1], -1), SafeConvert.toInt32(data[2], -1));
            int count = SafeConvert.toInt32(data[3], -1);
            // int unk5 = SafeConvert.ToInt32(data[4]); // Always 0
            if (proto.getTemplateId() == 0 || count == -1)
                return;

            // Player must be at shop
            if (player.getPosition().getSpecialLocationType() == SpecialLocationTypes.Generic)
                return;

            Shop shop = Map.getInstance().getShop(player.getPosition().getSpecialLocationId());
            // There is no shop with this id
            if (shop == null)
                return;

            if (shop.sellItem(player, proto, count) && player.getSession() != null)
                player.getSession().send(shop.getData());
        }
    }

    private class SetAutoLootOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            int status = SafeConvert.toInt32(data[0]);
            if (status == 1)
                Session.this.player.getSettings().set(PlayerSettings.AutoLoot);
            else
                Session.this.player.getSettings().unset(PlayerSettings.AutoLoot);
        }
    }

    private class GetInnOpcodeHandler extends OpcodeHandler
    {
        @Override
        public void handle(String[] data)
        {
            send(Session.this.player.getInn().getInnData());
        }
    }

    private class InnStoreOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 4;
        }

        @Override
        public void handle(String[] data)
        {
            Prototype proto = new Prototype(SafeConvert.toInt32(data[0]), SafeConvert.toInt32(data[1], -1), SafeConvert.toInt32(data[2], -1));
            int count = SafeConvert.toInt32(data[3], -1);

            if (proto.getTemplateId() == 0 || count == -1)
                return;
            Session.this.player.getInn().tryStoreItem(proto, count);
        }
    }

    private class InnGetOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 4;
        }

        @Override
        public void handle(String[] data)
        {
            Prototype proto = new Prototype(SafeConvert.toInt32(data[0]), SafeConvert.toInt32(data[1], -1), SafeConvert.toInt32(data[2], -1));
            int count = SafeConvert.toInt32(data[3], -1);

            if (proto.getTemplateId() == 0 || count == -1)
                return;
            Session.this.player.getInn().tryTakeItem(proto, count);
        }
    }

    private class WhisperOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            ChatHandler.handleWhisper(Session.this.player, data[0]);
        }
    }

    private class DestroyItemOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 2;
        }

        @Override
        public void handle(String[] data)
        {
            int slotId = SafeConvert.toInt32(data[0], -1);
            int count = SafeConvert.toInt32(data[1]);

            Session.this.player.getInventory().destroyItem(slotId, count);
        }
    }

    private class UseItemOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 3;
        }

        @Override
        public void handle(String[] data)
        {
            int slotId = SafeConvert.toInt32(data[0], -1);
            // TODO: assign unknown second and third values
            if (slotId == -1)
                return;
            Session.this.player.getInventory().useItem(slotId);
        }
    }

    private class QuestListFilterOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            boolean isHideCompleted = SafeConvert.toInt32(data[0]) == 1;
            Iterator<Quest> iterator = Session.this.player.getQuestIterator();
            while (iterator.hasNext()) {
                Quest quest = iterator.next();

                if (!quest.isAccepted())
                    continue;

                if (isHideCompleted && quest.getStatus() == QuestStatuses.Completed)
                    continue;
                Session.this.send(quest.getQuestData());
            }
        }
    }

    private class GetQuestInfoOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            int questId = SafeConvert.toInt32(data[0]);
            QuestTemplate template = DBStorage.QuestTemplateStore.get(questId);
            if (template != null)
                send(template.getQuestInfoData());
        }
    }

    private class GetQuestInfoForAcceptOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            Session.this.player.tryAcceptQuest(SafeConvert.toInt32(data[0]));
        }
    }

    private class QuestAcceptOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            int questId = SafeConvert.toInt32(data[0]);
            Quest quest = Session.this.player.getQuest(questId);
            // This quest previously wasn't requested to accept.
            if (quest == null)
                return;

            quest.accept();
        }
    }

    private class SetAutoSpellOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            Session.this.player.setAutoSpell(SafeConvert.toInt32(data[0]));
        }
    }

    private class EnterStarOpcodeHandler extends OpcodeHandler
    {
        @Override
        public void handle(String[] data)
        {
            send(new ServerPacket(ServerOpcodes.StarInfo).add(Session.this.player.getHome().getId()).add("0") // Corpse1 Location ID
                    .add("0") // Corpse2 Location ID
                    .add("0") // Corpse3 Location ID
                    .add("") // ???
                    .add("") // ???
                    .add("")); // ???
        }
    }

    private class SetHomeOpcodeHandler extends OpcodeHandler
    {
        @Override
        public void handle(String[] data)
        {
            Session.this.player.setHome();
        }
    }

    private class LocationInfoOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            Location location = Map.getInstance().getLocation(SafeConvert.toInt32(data[0]));
            if (location != null)
                Session.this.send(location.getInfoData());
        }
    }

    private class RegionLocationsOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            Region region = Map.getInstance().getRegion(SafeConvert.toInt32(data[0]));
            if (region != null)
                Session.this.send(region.getLocationListData());
        }
    }

    private class GuildLessonsInfoOpcodeHandler extends OpcodeHandler
    {
        @Override
        public int getMinDataLength()
        {
            return 1;
        }

        @Override
        public void handle(String[] data)
        {
            Guild guild = DBStorage.GuildStore.get(SafeConvert.toInt32(data[0]));
            if (guild != null)
                Session.this.send(guild.getLessonsData());
        }
    }

    private class LearnGuildInfoOpcodeHandler extends OpcodeHandler
    {
        @Override
        public void handle(String[] data)
        {
            ServerPacket packet = new ServerPacket(ServerOpcodes.LearnGuildInfo);
            packet.add("0");  // Always 0
            int availableCount = Session.this.player.getLevel() - Session.this.player.getGuildLevel();
            packet.add(availableCount); // Available levels

            // Positive - free available lessons count
            // Negative - total learned lessons
            packet.add(-Session.this.player.getGuildLevel());
            packet.add(availableCount);
            // Next lesson gold
            packet.add(LevelCost.getGold(Session.this.player.getGuildLevel() + 1));
            // Gold for all available lessons
            packet.add(LevelCost.getTotalGold(Session.this.player.getGuildLevel() + 1, Session.this.player.getLevel()));

            // Lessons reset cost
            // TODO: locale strings
            packet.add("free");

            // ??? Maybe next reset cost?
            packet.add("+100500 gold");

            Session.this.send(packet);
        }
    }

    private class GroupCreateOpcodeHandler extends OpcodeHandler {

        @Override
        public void handle(String[] data) {
            Session.this.player.createGroup();
        }
    }

    private class GroupSettingsChangeOpcodeHandler extends OpcodeHandler {

        @Override
        public int getMinDataLength() {
            return 9;
        }

        @Override
        public void handle(String[] data) {
            Group group = Session.this.player.getGroup();

            // The player is not in a group
            // or is not a leader
            if (group == null || group.getLeader() != Session.this.player)
                return;

            group.setMinLevel(SafeConvert.toInt32(data[0]));
            group.setMaxLevel(SafeConvert.toInt32(data[1]));
            group.setNoReligionAllowed(SafeConvert.toInt32(data[2], 1) != 0);
            group.setSunAllowed(SafeConvert.toInt32(data[3], 1) != 0);
            group.setMoonAllowed(SafeConvert.toInt32(data[4], 1) != 0);
            group.setOrderAllowed(SafeConvert.toInt32(data[5], 1) != 0);
            group.setChaosAllowed(SafeConvert.toInt32(data[6], 1) != 0);
            Group.ClanAccessModes mode = Group.ClanAccessModes.parse(SafeConvert.toInt32(data[7], 0));
            if (mode == null)
                mode = Group.ClanAccessModes.All;
            group.setClanAccessMode(mode);
            group.setOpen(SafeConvert.toInt32(data[8], 1) != 0);

            Session.this.send(group.getSettingsData()).send(group.getTeamLootData());
        }
    }

    private class GroupDisbandOpcodeHandler extends OpcodeHandler {

        @Override
        public void handle(String[] data) {
            Group group = Session.this.player.getGroup();

            if (group == null || group.getLeader() != Session.this.player) {
                return;
            }

            group.disband();
        }
    }

    private class GroupJoinOpcodeHandler extends OpcodeHandler {

        @Override
        public int getMinDataLength() {
            return 1;
        }

        @Override
        public void handle(String[] data) {
            Session.this.player.joinGroup(World.getInstance().getPlayer(SafeConvert.toInt32(data[0])));

            // No matter a group has been create or has not
            // send a group relation anyway
            int leaderGuid;
            if (Session.this.player.getGroup() == null) {
                leaderGuid = 0;
            } else {
                leaderGuid = Session.this.player.getGroup().getLeader().getGuid();
            }
            Session.this.send(new ServerPacket(ServerOpcodes.GroupCreated).add("0") // Not a leader
                    .add(leaderGuid));
        }
    }

    private class GroupQuitOpcodeHandler extends OpcodeHandler {

        @Override
        public void handle(String[] data) {
            Session.this.player.leaveGroup();
        }
    }

    private class GroupKickOpcodeHandler extends  OpcodeHandler {

        @Override
        public int getMinDataLength() {
            return 1;
        }

        @Override
        public void handle(String[] data) {
            // The player is in a group and is a leader
            Group group = Session.this.player.getGroup();
            if (group == null || group.getLeader() != Session.this.player)
                return;

            // Kicking target exists and is in the same group
            Player player = World.getInstance().getPlayer(SafeConvert.toInt32(data[0]));
            if (player == null || player.getGroup() != group)
                return;

            // Just leave as usual Quit
            player.leaveGroup();
        }
    }

    private class GroupChangeLeaderOpcodeHandler extends OpcodeHandler {

        @Override
        public int getMinDataLength() {
            return 1;
        }

        @Override
        public void handle(String[] data) {
            // The player is in a group and is a leader
            Group group = Session.this.player.getGroup();
            if (group == null || group.getLeader() != Session.this.player)
                return;

            group.setLeader(World.getInstance().getPlayer(SafeConvert.toInt32(data[0])));
        }
    }

    private class GetTradeOpcodeHandler extends OpcodeHandler {

        @Override
        public int getMinDataLength() {
            return 1;
        }

        @Override
        public void handle(String[] data) {
            // Try to find the trader
            Player trader = World.getInstance().getPlayer(SafeConvert.toInt32(data[0]));
            if (trader == null) {
                return;
            }

            // Create a new trade
            if (Session.this.player.getTrade() == null) {
                // The trader is trading already
                if (trader.getTrade() != null) {
                    // The other side of the trade is this player
                    if (trader.getTrade().getOtherSide().getPlayer() == Session.this.player) {
                        Logging.Warn.log(toString() + " has not trade, but " + trader.toString() + " has a trade" +
                                "where the other side is the current player.");
                        // Send the existing trade data
                        Session.this.player.setTrade(trader.getTrade().getOtherSide());
                        Session.this.player.getTrade().sendTradeData();
                    } else {
                        // TODO: Determine what to do in this situation
                        return;
                    }
                } else {
                    new Trade(Session.this.player, trader);
                }

            // Send the existing trade data
            } else {
                Session.this.player.getTrade().sendTradeData();
            }
        }
    }

    private class TradeUpdateOpcodeHandler extends OpcodeHandler {

        @Override
        public int getMinDataLength() {
            return 15;
        }

        @Override
        public void handle(String[] data) {
            Trade trade = Session.this.player.getTrade();
            if (trade == null)
                return;
            Player trader = World.getInstance().getPlayer(SafeConvert.toInt32(data[0]));
            // The real trader and the new supply trader do not match
            if (trader != trade.getOtherSide().getPlayer()) {
                return;
            }

            Trade.Supply supply = trade.new Supply();
            int counter = 1;
            for (int i = 0; i < 3; ++i) {
                Item item = new Item(new Prototype(
                        SafeConvert.toInt32(data[counter++]),
                        SafeConvert.toInt32(data[counter++]),
                        SafeConvert.toInt32(data[counter++])),
                        SafeConvert.toInt32(data[counter++]));
                if (item.Template == null || item.getCount() == 0)
                    continue;
                if (!Session.this.player.getInventory().hasItem(item)) {
                    Logging.Warn.log("Trade: " + Session.this.player.toString() + "places item " +
                            item.getPrototype().toString() + "but he has no this item in the inventory");
                    continue;
                }
                supply.setItem(i, item);
            }

            int gold = SafeConvert.toInt32(data[counter++]);
            int platinum = SafeConvert.toInt32(data[counter++]);
            if (gold > 0) {
                if (gold > Session.this.player.getCurrencyAmount(Currencies.Gold)) {
                    gold = Session.this.player.getCurrencyAmount(Currencies.Gold);
                }
                supply.setGold(gold);
            }
            if (platinum > 0) {
                if (platinum > Session.this.player.getCurrencyAmount(Currencies.Platinum)) {
                    platinum = Session.this.player.getCurrencyAmount(Currencies.Platinum);
                }
                supply.setPlatinum(platinum);
            }

            trade.setCurrentSupply(supply);
        }
    }

    private class TradeApplyOpcodeHandler extends OpcodeHandler {

        @Override
        public int getMinDataLength() {
            return 29;
        }

        @Override
        public void handle(String[] data) {
            Trade trade = Session.this.player.getTrade();
            if (trade == null)
                return;
            Player trader = World.getInstance().getPlayer(SafeConvert.toInt32(data[0]));
            // The real trader and the new supply trader do not match
            if (trader != trade.getOtherSide().getPlayer()) {
                return;
            }

            Trade.Supply supply = trade.new Supply();
            int counter = 1;
            for (int i = 0; i < 3; ++i) {
                Item item = new Item(new Prototype(
                            SafeConvert.toInt32(data[counter++]),
                            SafeConvert.toInt32(data[counter++]),
                            SafeConvert.toInt32(data[counter++])),
                        SafeConvert.toInt32(data[counter++]));
                if (item.Template == null || item.getCount() == 0)
                    continue;
                if (!Session.this.player.getInventory().hasItem(item)) {
                    Logging.Warn.log("Trade: " + Session.this.player.toString() + "places item " +
                            item.getPrototype().toString() + "but he has no this item in the inventory");
                    continue;
                }
                supply.setItem(i, item);
            }

            int gold = SafeConvert.toInt32(data[counter++]);
            int platinum = SafeConvert.toInt32(data[counter++]);
            if (gold > Session.this.player.getCurrencyAmount(Currencies.Gold)) {
                gold = Session.this.player.getCurrencyAmount(Currencies.Gold);
            }
            if (platinum > Session.this.player.getCurrencyAmount(Currencies.Platinum)) {
                platinum = Session.this.player.getCurrencyAmount(Currencies.Platinum);
            }
            supply.setGold(gold);
            supply.setPlatinum(platinum);

            if (!trade.getCurrentSupply().equals(supply)) {
                Logging.Warn.log("Trade: " + Session.this.player.toString() + " agreed to a trade, but his own" +
                        "supply does not match. The current supply is updated");
                trade.setCurrentSupply(supply);
            }

            Trade.Supply demand = trade.new Supply();
            for (int i = 0; i < 3; ++i) {
                Item item = new Item(new Prototype(
                        SafeConvert.toInt32(data[counter++]),
                        SafeConvert.toInt32(data[counter++]),
                        SafeConvert.toInt32(data[counter++])),
                        SafeConvert.toInt32(data[counter++]));
                if (item.Template == null || item.getCount() == 0)
                    continue;
                demand.setItem(i, item);
            }

            gold = SafeConvert.toInt32(data[counter++]);
            platinum = SafeConvert.toInt32(data[counter++]);

            demand.setGold(gold);
            demand.setPlatinum(platinum);

            trade.agree(demand);
        }
    }

    private class TradeCancelOpcodeHandler extends OpcodeHandler {

        @Override
        public void handle(String[] data) {
            if (Session.this.player.getTrade() == null) {
                return;
            }
            Session.this.player.getTrade().cancel();
        }
    }

    private class SetAsceticismOpcodeHandler extends OpcodeHandler {

        @Override
        public int getMinDataLength() {
            return 1;
        }

        @Override
        public void handle(String[] data) {
            boolean set = SafeConvert.toInt32(data[0]) == 1;
            if (set) {
                Session.this.player.getSettings().set(PlayerSettings.Asceticism);
                Session.this.sendServerMessage(430);
            } else {
                Session.this.player.getSettings().unset(PlayerSettings.Asceticism);
                Session.this.sendServerMessage(431);
            }

            Session.this.send(Session.this.player.getParametersData());
        }
    }
}

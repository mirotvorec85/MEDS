package meds;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

import meds.Item.Prototype;
import meds.database.DBStorage;
import meds.database.entity.Character;
import meds.database.entity.NewMessage;
import meds.database.entity.QuestTemplate;
import meds.enums.BattleStates;
import meds.enums.Currencies;
import meds.enums.LoginResults;
import meds.enums.MovementDirections;
import meds.enums.PlayerSettings;
import meds.enums.SpecialLocationTypes;
import meds.logging.Logging;
import meds.util.MD5Hasher;
import meds.util.Random;
import meds.util.SafeConvert;

public class Session implements Runnable
{
    /**
     * Related Socket for this session.
     */
    private Socket socket;
    private HashMap<ClientOpcodes, OpcodeHandler> opcodeHandlers;

    /**
     * Queue of data for sending.
     * Key - Managed Thread ID.
     * Value - Data Packet
     */
    private HashMap<Long, ServerPacket> packets;

    private Player player;

    private boolean isAuthenticated;

    /**
     * Session key.
     */
    private int key;

    public Session(Socket socket)
    {
        this.socket = socket;

        this.opcodeHandlers = new HashMap<ClientOpcodes, Session.OpcodeHandler>();
        this.opcodeHandlers.put(ClientOpcodes.Verification, new VerificationOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.Login, new LoginOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.Ready, new ReadyOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.Movement, new MovementOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.PutMoney, new PutMoneyOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.TakeMoney, new TakeMoneyOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.BankExchange, new BankExchangeOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.Attack, new AttackOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.UseMagic, new UseMagicOpcodeHander());
        this.opcodeHandlers.put(ClientOpcodes.Relax, new RelaxOpcodeHandler());
        this.opcodeHandlers.put(ClientOpcodes.EnterGuild, new EnterGuildOpcodeHandler());
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

        this.packets = new HashMap<Long, ServerPacket>();

        this.key = Random.nextInt();
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
                Logging.Debug.log("Start reading next packets/.");
                int receivedSize = 0;
                String receivedString = "";
                byte[] buffer = new byte[bufferSize];
                do
                {
                    receivedSize = is.read(buffer);
                    // End Of Stream / Socket is closed
                    if (receivedSize == -1)
                    {
                        Logging.Debug.log("Received -1");
                        disconnect();
                        return;
                    }
                    receivedString += new String(Arrays.copyOf(buffer, receivedSize), "Unicode");
                }
                while (receivedSize == bufferSize);
                Logging.Debug.log("Received string: " + receivedString);

                ClientPacket packet = new ClientPacket(receivedString);

                for (PacketOpcode opcode : packet.getPacketOpcodes())
                {
                    if (!opcode.isValid())
                        continue;
                    ClientOpcodes clientOpcode = ClientOpcodes.parse(opcode.getOpcode());
                    if (clientOpcode == null)
                    {
                        Logging.Warn.log("Received unknown opcode \"" + opcode.getOpcode() + "\".");
                        continue;
                    }

                    OpcodeHandler handler = this.opcodeHandlers.get(clientOpcode);
                    if (handler == null)
                    {
                        Logging.Warn.log("Handler for the opcode \"" + clientOpcode + "\" not found.");
                        continue;
                    }
                    if (!this.isAuthenticated && handler.isAuthenticatedOnly())
                    {
                        Logging.Warn.log("Attempt to handle the opcode \"" + clientOpcode + "\" with the not authenticated session.");
                        continue;
                    }

                    String[] data = opcode.getData();
                    if (handler.getMinDataLength() != -1 && data.length < handler.getMinDataLength())
                    {
                        Logging.Warn.log("Opcode \"" + clientOpcode + "\" has the length " + data.length + ", but minimal is " + handler.getMinDataLength() + ". Handling abborted.");
                        continue;
                    }

                    handler.handle(data);
                    // Send a packet buffer
                    if (handler.isPositionSend() && this.player != null && this.player.getPosition() != null)
                        this.player.getPosition().send();
                    else
                        send();
                }
            }
        }
        catch (IOException e)
        {
            Logging.Error.log("Exception while reading a socket: " + e.getMessage());
        }
    }

    private void disconnect()
    {
        if (this.player != null)
        {
            this.player.logOut();
            World.getInstance().playerLoggedOut(this.player);
        }

        try
        {
            this.socket.close();
        }
        catch (IOException ex)
        {
            Logging.Error.log("IOException on socket.close(): " + ex.getMessage());
        }
        Server.disconnect(this);
    }

    public void send(ServerPacket packet)
    {
        if (packet == null || packet.isEmpty())
            return;

        OutputStream os;
        try
        {
            os = this.socket.getOutputStream();
            byte[] bytes = packet.getBytes();
            os.write(bytes);
            Logging.Debug.log("Sending data: " + packet.toString().replace('\u0000', '\n'));
            //os.close();
        }
        catch (IOException e)
        {
            Logging.Error.log("IOException while writing to a socket: " + e.getMessage());
        }
    }

    /**
     * Sends an accumulated packet buffer for the current thread.
     */
    public void send()
    {
        ServerPacket packet = this.packets.get(Thread.currentThread().getId());
        if (packet != null)
        {
            this.send(packet);
            this.packets.remove(Thread.currentThread().getId());
        }
    }

    public Session addData(ServerPacket packet)
    {
        ServerPacket _packet = this.packets.get(Thread.currentThread().getId());
        if (_packet == null)
        {
            this.packets.put(Thread.currentThread().getId(), packet.clone());
        }
        else
        {
            _packet.add(packet);
        }

        return this;
    }

    public Session addServerMessage(int messageId, String... values)
    {
        ServerPacket packet = new ServerPacket(ServerOpcodes.ServerMessage).add(messageId);
        for (String string : values)
            packet.add(string);
        this.addData(packet);
        return this;
    }

    public Session sendServerMessage(int messageId, String... values)
    {
        ServerPacket packet = new ServerPacket(ServerOpcodes.ServerMessage).add(messageId);
        for (String string : values)
            packet.add(string);
        this.send(packet);
        return this;
    }

    private abstract class OpcodeHandler
    {
        /**
         *  Gets a minimal length that allows to handle an opcode
         */
        public int getMinDataLength()
        {
            // No limit
            return -1;
        }

        /**
         * Indicates whether an opcode may be handled for only logged players.
         */
        public boolean isAuthenticatedOnly()
        {
            return true;
        }

        /**
         * Indicates whether an opcode handling requires sending data to all players at the current location.
         */
        public boolean isPositionSend()
        {
            return false;
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

            // NOTE: Sometimes the data above is sended as a separate packet

            packet.add(Session.this.player.getCurrencyData())
                .add(Session.this.player.getParametersData())
                .addData(ServerOpcodes.BattleState, BattleStates.NoBattle.toString())
                .add(Session.this.player.getHealthManaData())
                .add(Session.this.player.getLevel().getPacketData());

            send(packet);
            packet.clear();

            /*
             * No sharp data after 1.2.7.6
            // sharp "#"
            packet.addData(ServerOpcodes.Sharp, "0", "0");
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

            if (Session.this.player.getRelax())
                packet.add(ServerOpcodes.RelaxOn);
            else
                packet.add(ServerOpcodes.RelaxOff);

            packet.addData(ServerOpcodes._lh0, "");

            // TODO: add auras
            // TODO: add quest statuses

            // NOTE: Sometimes the data above is sended as a separate packet

            // TODO: add achievement list here ("acl" opcode)

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
            packet.addData(ServerOpcodes._mmy, "Mentor Name");
             * */

            // Send the custom welcome message
            packet.addData(ServerOpcodes.ServerMessage, "5001");
            send(packet);
        }
    }

    private class ReadyOpcodeHandler extends OpcodeHandler
    {
        @Override
        public boolean isAuthenticatedOnly()
        {
            return false;
        }

        @Override
        public void handle(String[] data)
        {
            Session.this.isAuthenticated = true;
            Session.this.player.logIn(Session.this);
            World.getInstance().playerLoggedIn(Session.this.player);
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
            Session.this.player.setRelax(!Session.this.player.getRelax());
        }
    }

    private class EnterGuildOpcodeHandler extends OpcodeHandler
    {
        @Override
        public void handle(String[] data)
        {
            Location position = Session.this.player.getPosition();
            if ((position.getSpecialLocationType() != SpecialLocationTypes.MagicSchool &&
                    position.getSpecialLocationType() != SpecialLocationTypes.MilitarySchool) ||
                    position.getSpecialLocationId() == 0)
                    return;
                int guildId = position.getSpecialLocationId();

                if (!DBStorage.GuildStore.containsKey(guildId))
                    return;

                send(Session.this.player.getGuildLessonsData(guildId));
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
            int guildId = SafeConvert.toInt32(data[0]);
            // TODO: Add checking for the guild chains
            //if (guildId != this.player.Position.SpecialLocationId)
            //    return;
            if (!DBStorage.GuildStore.containsKey(guildId))
                return;

            Session.this.player.learnGuildLesson(guildId);
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
            for (int i = 1; i < data.length; i += 2)
            {
                templateId = SafeConvert.toInt32(data[i - 1]);
                //modification = SafeConvert.toInt32(data[i]);
                // TODO: modification
                Item item = new Item(templateId);
                packet.add(item.getPacketData());
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
            Session.this.addData(new ServerPacket(ServerOpcodes.GetCorpse).add("true"));

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
                    Session.this.addServerMessage(1014, itemCount > 1 ? itemCount + " " : "", item.Template.getTitle());
                    Session.this.player.getPosition().addData(Session.this.player, new ServerPacket(ServerOpcodes.ServerMessage).add("1026").add(Session.this.player.getName()).add(itemCount > 1 ? itemCount + " " : "").add(item.Template.getTitle()));
                    if (item.getCount() == 0)
                        Session.this.player.getPosition().removeItem(item);
                }
                else
                {
                    Session.this.addServerMessage(1001, item.Template.getTitle());
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

            Session.this.addData(shop.getData());
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
            Session.this.player.sellItem(proto, count);
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
            Session.this.player.buyItem(proto, count);
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
                Session.this.player.setSettings(PlayerSettings.AutoLoot);
            else
                Session.this.player.unsetSettings(PlayerSettings.AutoLoot);
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
            // TODO: Implement Character Quests
            /*
            boolean isHideCompleted = SafeConvert.toInt32(data[0]) == 1;
            foreach (Quest quest in this.player.Quests.Values)
            {
                // Ignore not accepted quests
                if (!quest.IsAccepted)
                    continue;

                if (isHideCompleted && quest.Status == QuestStatuses.Completed)
                    continue;
                addData(quest.GetQuestData());
            }
            */
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
                addData(template.getQuestInfoData());
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


            // TODO: implement CharacterQuest
            /*
            int questId = SafeConvert.toInt32(data[0]);
            Quest quest;
            // This quest previously wasn't requested to accept.
            if (!this.player.Quests.TryGetValue(questId, out quest))
                return;

            quest.IsAccepted = true;
            */
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
            addData(new ServerPacket(ServerOpcodes.StarInfo)
                .add(Session.this.player.getHome().getId())
                .add("0") // Corpse1 Location ID
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
        public boolean isAuthenticatedOnly()
        {
            return false;
        }

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
                Session.this.addData(location.getInfoData());
        }
    }
}

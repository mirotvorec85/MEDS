package org.meds.net;

import org.meds.util.SafeConvert;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A component used to parse a received string from a client socket
 * and create {@link ClientCommandData} objects for every command
 * that contains in the string.
 * @author Romman
 */
@Component
public class ClientPacketParser {

    public static final String COMMAND_SEPARATOR = "\u0000";
    public static final String COMMAND_DATA_SEPARATOR = "\u0001";

    /**
     * Converts a string into list of command data objects
     * @param receivedString
     * @throws ClientPacketParseException when a command has unknown type
     * @return
     */
    public List<ClientCommandData> parse(String receivedString) {
        // ClientPacket
        String[] commands = receivedString.split(COMMAND_SEPARATOR);
        List<ClientCommandData> commandDatas = new ArrayList<>(commands.length);

        for (String commandString : commands) {
            // Empty command value. Skip it
            if (commandString.isEmpty()) {
                continue;
            }

            String[] commandDataArray = commandString.split(COMMAND_DATA_SEPARATOR);
            ClientCommandTypes type = ClientCommandTypes.parse(commandDataArray[0]);
            if (type == null) {
                throw new ClientPacketParseException("Received unknown command type: \"" + commandDataArray[0] + "\".");
            }
            commandDataArray = Arrays.copyOfRange(commandDataArray, 1, commandDataArray.length);
            CommandData commandData = new CommandData(type, commandDataArray);
            commandDatas.add(commandData);
        }
        return commandDatas;
    }

    private static class CommandData implements ClientCommandData {

        private ClientCommandTypes type;
        private String[] data;

        CommandData(ClientCommandTypes type, String[] data) {
            this.type = type;
            this.data = data;
        }

        @Override
        public ClientCommandTypes type() {
            return this.type;
        }

        @Override
        public int size() {
            return data.length;
        }

        @Override
        public int getInt(int index) {
            return SafeConvert.toInt32(data[index]);
        }

        @Override
        public int getInt(int index, int defaultValue) {
            return SafeConvert.toInt32(data[index], defaultValue);
        }

        @Override
        public String getString(int index) {
            return data[index];
        }
    }
}

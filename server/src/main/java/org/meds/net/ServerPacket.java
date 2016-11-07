package org.meds.net;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ServerPacket implements Cloneable {

    private enum PacketEnds {
        Value,
        InterValue,
        InterPacket,
    }

    private PacketEnds packetEnd;
    private StringBuilder data;

    private ServerPacket(CharSequence seq, PacketEnds end) {
        this.data = new StringBuilder(seq);
        this.packetEnd = end;
    }

    /**
     * Initializes a new empty instance of the ServerPacket class.
     */
    public ServerPacket() {
        this.data = new StringBuilder();
        this.packetEnd = PacketEnds.InterPacket;
    }

    public ServerPacket(ServerCommands command) {
        this();
        add(command);
    }

    public ServerPacket(ServerPacket packet) {
        this.data = packet.data;
        this.packetEnd = packet.packetEnd;
    }

    public void clear() {
        this.data.setLength(0);
        this.packetEnd = PacketEnds.InterPacket;
    }

    public boolean isEmpty() {
        return this.data.length() == 0;
    }

    public ServerPacket add(ServerCommands command) {
        if (this.packetEnd != PacketEnds.InterPacket)
            this.data.append("\u0000");
        this.data.append(command.toString()).append("\u0001");
        this.packetEnd = PacketEnds.InterValue;
        return this;
    }

    public ServerPacket add(String value) {
        if (this.packetEnd == PacketEnds.Value)
            this.data.append("\u0001");
        this.data.append(value);
        this.packetEnd = PacketEnds.Value;
        return this;
    }

    public ServerPacket add(int value) {
        return add(Integer.toString(value));
    }

    public ServerPacket add(Object object) {
        // NULL should be replaced with empty value
        if (object == null)
            return add("");
        return add(object.toString());
    }

    public ServerPacket add(ServerPacket packet) {
        if (packet == null || packet.isEmpty())
            return this;
        if (this.packetEnd != PacketEnds.InterPacket)
            this.data.append("\u0000");
        this.data.append(packet.data);
        this.packetEnd = packet.packetEnd;
        return this;
    }

    /**
     * Appends the specified packet as an command and an array of strings..
     *
     * @return A reference to this instance after addition operation has completed.
     */
    public ServerPacket addData(ServerCommands command, String... data) {
        add(command);

        if (data.length != 0) {
            for (String value : data)
                add(value);
            this.data.append("\u0000");
        }
        this.packetEnd = PacketEnds.InterPacket;
        return this;
    }

    @Override
    public String toString() {
        if (this.packetEnd != PacketEnds.InterPacket)
            this.data.append("\u0000");
        return this.data.toString();
    }

    public byte[] getBytes() {
        String string = this.toString();

        byte[] bytes;
        try {
            bytes = string.getBytes("Unicode");
            bytes = Arrays.copyOfRange(bytes, 2, bytes.length);
        } catch (UnsupportedEncodingException e) {
            bytes = new byte[0];
        }
        /*
        char[] chars = string.toCharArray();
        byte[] bytes = new byte[chars.length * 2];

        for (int i = 0; i < chars.length; ++i)
        {
            int code = Character.getNumericValue();
            if (code / 256 > 0)
            {
                bytes[i * 2] = (byte)(code / 256);
                bytes[i * 2 + 1] = (byte)(code % 256);
            }
            else
                bytes[i * 2 + 1] = (byte)code;
        }
        */
        return bytes;
    }

    @Override
    public ServerPacket clone() {
        return new ServerPacket(this.data, this.packetEnd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServerPacket that = (ServerPacket) o;

        return this.data != null ? this.data.equals(that.data) : that.data != null;
    }

    @Override
    public int hashCode() {
        return data != null ? data.hashCode() : 0;
    }
}

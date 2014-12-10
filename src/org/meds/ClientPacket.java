package org.meds;

import org.meds.net.PacketOpcode;

public class ClientPacket
{
    private PacketOpcode[] packetOpcodes;

    public ClientPacket(String data)
    {
        String[] opcodes = data.split("\u0000");
        this.packetOpcodes = new PacketOpcode[opcodes.length];
        for (int i = 0; i < opcodes.length; ++i)
        {
            this.packetOpcodes[i] = new PacketOpcode(opcodes[i]);
        }
    }

    public PacketOpcode[] getPacketOpcodes()
    {
        return this.packetOpcodes;
    }
}

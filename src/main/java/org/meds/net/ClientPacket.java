package org.meds.net;

public class ClientPacket
{
    private PacketCommand[] packetCommands;

    public ClientPacket(String data)
    {
        String[] commands = data.split("\u0000");
        this.packetCommands = new PacketCommand[commands.length];
        for (int i = 0; i < commands.length; ++i)
        {
            this.packetCommands[i] = new PacketCommand(commands[i]);
        }
    }

    public PacketCommand[] getPacketCommands()
    {
        return this.packetCommands;
    }
}

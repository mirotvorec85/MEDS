package org.meds.net;

import java.util.Arrays;

public class PacketOpcode
{
    private String opcode;
    private String[] data;
    private boolean isValid;

    public PacketOpcode(String data)
    {
        if (data.isEmpty())
        {
            this.isValid = false;
            return;
        }

        this.data = data.split("\u0001");
        this.opcode = this.data[0];
        this.data = Arrays.copyOfRange(this.data, 1, this.data.length);
        this.isValid = true;
    }

    public PacketOpcode(String[] data)
    {
        if (data.length == 0)
        {
            this.isValid = false;
            return;
        }
        this.opcode = data[0];
        this.data = Arrays.copyOfRange(data, 1, data.length);
        this.isValid = true;
    }

    public String getOpcode()
    {
        return this.opcode;
    }

    public String[] getData()
    {
        return this.data;
    }

    public boolean isValid()
    {
        return this.isValid;
    }
}

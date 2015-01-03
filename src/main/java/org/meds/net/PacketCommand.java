package org.meds.net;

import java.util.Arrays;

public class PacketCommand
{
    private String command;
    private String[] data;
    private boolean isValid;

    public PacketCommand(String data)
    {
        if (data.isEmpty())
        {
            this.isValid = false;
            return;
        }

        this.data = data.split("\u0001");
        this.command = this.data[0];
        this.data = Arrays.copyOfRange(this.data, 1, this.data.length);
        this.isValid = true;
    }

    public String getCommand()
    {
        return this.command;
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

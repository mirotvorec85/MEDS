package meds.util;

public class EnumFlags<T extends Enum<T> & Valued> implements Valued
{
    private int flags;

    public EnumFlags()
    {
        this.flags = 0;
    }

    public EnumFlags(int flags)
    {
        this.flags = flags;
    }

    public EnumFlags(T flag)
    {
        this.flags = flag.getValue();
    }

    public EnumFlags(T[] flags)
    {
        for (T flag : flags)
            this.flags |= flag.getValue();
    }

    public boolean set(T flag)
    {
        return this.set(flag.getValue());
    }

    public boolean set(int flag)
    {
        if (this.has(flag))
            return false;
        else
            this.flags |= flag;
        return true;
    }

    public boolean unset(int flag)
    {
        if (!this.has(flag))
            return false;
        else
            this.flags ^= flag;
        return true;
    }

    public boolean unset(T flag)
    {
        return this.unset(flag.getValue());
    }

    public boolean has(int flag)
    {
        return (this.flags & flag) > 0;
    }

    public boolean has(T flag)
    {
        return this.has(flag.getValue());
    }

    @Override
    public int getValue()
    {
        return this.flags;
    }

    @Override
    public String toString()
    {
        return Integer.toString(this.flags);
    }
}

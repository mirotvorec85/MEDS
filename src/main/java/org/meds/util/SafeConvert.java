package org.meds.util;

public class SafeConvert
{
    public static int toInt32(String s)
    {
        return toInt32(s, 0);
    }

    public static int toInt32(String s, int defaultValue /* = 0 */)
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException e)
        {
            return defaultValue;
        }
    }
}

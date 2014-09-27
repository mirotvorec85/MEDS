package meds.util;

public class Random
{
    private static java.util.Random random;

    public static void initialize()
    {
        Random.random = new java.util.Random(System.currentTimeMillis());
    }

    /**
     * Returns non-negative random number.
     */
    public static int nextInt()
    {
        return Random.random.nextInt();
    }

    /**
     * Returns a pseudorandom value between 0 (inclusive) and the specified maxValue (exclusive)
     */
    public static int nextInt(int maxValue)
    {
        return Random.random.nextInt(maxValue);
    }

    /**
     * Returns a pseudorandom value between minValue (inclusive) and the specified maxValue (exclusive)
     */
    public static int nextInt(int minValue, int maxValue)
    {
        if (minValue >= maxValue)
            return minValue;
        return Random.random.nextInt(maxValue - minValue) + minValue;
    }

    public static double nextDouble()
    {
        return Random.random.nextDouble();
    }
}

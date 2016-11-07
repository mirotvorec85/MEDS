package org.meds.database;

public class LevelCost {
    
    /**
     * Canceled amount of gold that should has been paid for leveling from 1 up to 7 level.
     */
    private static final int SeverLevelsGold = 205;

    public static int getExp(int level) {
        if (level < 1 || level > 360)
            return 0;

        int exp = level * level + 4 * level;

        if (level >= 301)
            exp *= 1.25 + 0.25 * (level - 301);

        return exp;
    }

    public static int getGold(int level) {
        if (level < 7 || level > 360)
            return 0;

        int gold = level * level + 4 * level + 5;
        if (level >= 301) {
            gold *= 1.5 + 0.5 * (level - 302);
            gold += 25 * (299 - level);
        }

        return gold;
    }

    public static int getTotalGold(int level) {
        if (level < 8)
            return 0;

        if (level > 360)
            level = 360;

        int firstLevels = level;
        int lastLevels = 0;
        if (firstLevels > 300) {
            firstLevels = 300;
            lastLevels = level - 300;
        }

        int gold = firstLevels * (firstLevels + 1) * (2 * firstLevels + 1) / 6 + 2 * (firstLevels + 1) * firstLevels + 5 * firstLevels - SeverLevelsGold;

        while (lastLevels != 0) {
            gold += getGold(lastLevels-- + 300);
        }

        return gold;
    }

    public static int getTotalGold(int startLevel, int finalLevel) {
        if (startLevel > finalLevel)
            return 0;

        return getTotalGold(finalLevel) - getTotalGold(startLevel - 1);
    }
}

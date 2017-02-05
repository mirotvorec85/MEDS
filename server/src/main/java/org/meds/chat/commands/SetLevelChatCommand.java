package org.meds.chat.commands;

import org.meds.Player;
import org.meds.util.SafeConvert;

public class SetLevelChatCommand extends AbstractChatCommand {

    @Override
    public int getMinArgsCount() {
        return 1;
    }

    @Override
    public void handle(Player player, String[] args) {
        int level = SafeConvert.toInt32(args[0], -1);
        if (level < 0 || level > 360)
            return;
        player.setLevel(level);
    }
}

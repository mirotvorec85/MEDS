package org.meds.chat.commands;

import org.meds.Player;
import org.meds.map.Location;
import org.meds.map.Map;
import org.meds.util.SafeConvert;

public class TeleportChatCommand extends AbstractChatCommand {

    @Override
    public int getMinArgsCount() {
        return 1;
    }

    @Override
    public void handle(Player player, String[] args) {
        int locationId = SafeConvert.toInt32(args[0]);
        Location location = Map.getInstance().getLocation(locationId);
        if (location != null) {
            player.setPosition(location);
        }
    }
}

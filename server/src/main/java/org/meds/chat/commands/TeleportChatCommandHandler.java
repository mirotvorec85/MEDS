package org.meds.chat.commands;

import org.meds.Player;
import org.meds.map.Location;
import org.meds.map.MapManager;
import org.meds.util.SafeConvert;
import org.springframework.beans.factory.annotation.Autowired;

@ChatCommand("teleport")
public class TeleportChatCommandHandler extends AbstractChatCommandHandler {

    @Autowired
    private MapManager mapManager;

    @Override
    public int getMinArgsCount() {
        return 1;
    }

    @Override
    public void handle(Player player, String[] args) {
        int locationId = SafeConvert.toInt32(args[0]);
        Location location = mapManager.getLocation(locationId);
        if (location != null) {
            player.setPosition(location);
        }
    }
}

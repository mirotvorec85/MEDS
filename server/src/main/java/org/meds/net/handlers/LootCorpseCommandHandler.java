package org.meds.net.handlers;

import org.meds.Corpse;
import org.meds.Player;
import org.meds.item.Item;
import org.meds.item.ItemPrototype;
import org.meds.item.ItemTitleConstructor;
import org.meds.net.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
@ClientCommand(ClientCommandTypes.LootCorpse)
public class LootCorpseCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext context;
    @Autowired
    private ItemTitleConstructor itemTitleConstructor;

    @Override
    public int getMinDataLength() {
        return 3;
    }

    @Override
    public void handle(ClientCommandData data) {
        int id = data.getInt(0, 0);

        int itemModification = data.getInt(1);
        int itemDurability = data.getInt(2);

        // Do not know why but always true
        context.getSession().send(new ServerPacket(ServerCommands.GetCorpse).add("true"));
        Player player = context.getPlayer();

        // TODO: sound 26 on gold collect. Sound 27 on item collect

        // Loot a corpse
        if (id > 0) {
            Corpse corpse = player.getPosition().getCorpse(id);
            if (corpse == null) {
                return;
            }

            player.lootCorpse(corpse);
        }
        // Pick up an item
        else if (id < 0) {
            ItemPrototype proto = new ItemPrototype(-id, itemModification, itemDurability);
            Item item = player.getPosition().getItem(proto);
            if (item == null) {
                return;
            }
            int itemCount = item.getCount();
            String itemTitle = itemTitleConstructor.getTitle(item);
            if (player.getInventory().tryStoreItem(item)) {
                context.getSession().sendServerMessage(1014, itemCount > 1 ? itemCount + " " : "", itemTitle);

                ServerPacket pickUpMessage = new ServerPacket(ServerCommands.ServerMessage)
                        .add("1026").add(player.getName())
                        .add(itemCount > 1 ? itemCount + " " : "")
                        .add(itemTitle);
                player.getPosition().send(player, pickUpMessage);
                if (item.getCount() == 0) {
                    player.getPosition().removeItem(item);
                }
            } else {
                context.getSession().sendServerMessage(1001, itemTitle);
            }
        }
    }
}

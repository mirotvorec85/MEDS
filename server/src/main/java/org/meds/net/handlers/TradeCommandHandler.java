package org.meds.net.handlers;

import org.meds.Player;
import org.meds.Trade;
import org.meds.World;
import org.meds.enums.Currencies;
import org.meds.item.Item;
import org.meds.item.ItemFactory;
import org.meds.item.ItemFlags;
import org.meds.item.ItemPrototype;
import org.meds.logging.Logging;
import org.meds.net.ClientCommandData;
import org.meds.net.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Romman.
 */
public abstract class TradeCommandHandler extends CommonClientCommandHandler {

    @Autowired
    private SessionContext sessionContext;
    @Autowired
    private World world;
    @Autowired
    private ItemFactory itemFactory;

    public abstract boolean isApply();

    @Override
    public void handle(ClientCommandData data) {
        Trade trade = sessionContext.getPlayer().getTrade();
        if (trade == null) {
            return;
        }
        Player player = sessionContext.getPlayer();
        Player trader = world.getPlayer(data.getInt(0));
        // The real trader and the new supply trader do not match
        if (trader != trade.getOtherSide().getPlayer()) {
            return;
        }

        Trade.Supply supply = trade.new Supply();
        int counter = 1;
        for (int i = 0; i < 3; ++i) {
            ItemPrototype prototype = new ItemPrototype(
                    data.getInt(counter++),
                    data.getInt(counter++),
                    data.getInt(counter++));
            Item item = itemFactory.create(prototype, data.getInt(counter++));

            // An item has not been constructed right
            if (item.getTemplate() == null || item.getCount() == 0) {
                continue;
            }
            if (item.hasFlag(ItemFlags.IsPersonal)) {
                continue;
            }

            if (!player.getInventory().hasItem(item)) {
                Logging.Warn.log("Trade: " +player.toString() + "places item " +
                        item.getPrototype().toString() + "but he has no this item in the inventory");
                continue;
            }
            supply.setItem(i, item);
        }

        int gold = data.getInt(counter++);
        int platinum = data.getInt(counter++);
        if (gold <= 0) {
            gold = 0;
        } else if (gold > player.getCurrencyAmount(Currencies.Gold)) {
            gold = player.getCurrencyAmount(Currencies.Gold);
        }
        if (platinum <= 0) {
            platinum = 0;
        } else if (platinum > player.getCurrencyAmount(Currencies.Platinum)) {
            platinum = player.getCurrencyAmount(Currencies.Platinum);
        }
        supply.setGold(gold);
        supply.setPlatinum(platinum);

        if (isApply()) {
            if (!trade.getCurrentSupply().equals(supply)) {
                Logging.Warn.log("Trade: " + player.toString() + " agreed to a trade, but his own" +
                        "supply does not match. The current supply is updated");
                trade.setCurrentSupply(supply);
            }

            Trade.Supply demand = trade.new Supply();
            for (int i = 0; i < 3; ++i) {
                ItemPrototype prototype = new ItemPrototype(
                        data.getInt(counter++),
                        data.getInt(counter++),
                        data.getInt(counter++));
                Item item = itemFactory.create(prototype, data.getInt(counter++));

                if (item.getTemplate() == null || item.getCount() == 0) {
                    continue;
                }
                demand.setItem(i, item);
            }

            gold = data.getInt(counter++);
            platinum = data.getInt(counter++);

            demand.setGold(gold);
            demand.setPlatinum(platinum);

            trade.agree(demand);
        }
        // Trade update
        else {
            trade.setCurrentSupply(supply);
        }
    }
}

package dev.martim.marketplace.menu;

import dev.martim.marketplace.account.Account;
import dev.martim.marketplace.api.item.Item;
import dev.martim.marketplace.api.menu.Menu;
import dev.martim.marketplace.api.menu.sound.MenuSound;
import dev.martim.marketplace.backend.data.MarketData;
import dev.martim.marketplace.manager.Manager;
import dev.martim.marketplace.market.MarketType;
import dev.martim.marketplace.market.items.MarketItem;
import dev.martim.marketplace.util.DateUtil;
import dev.martim.marketplace.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MarketMenu extends Menu {

    private final Account account;
    private final MarketType type;

    private final MarketData data = Manager.getMarketData();

    public MarketMenu(Player player, MarketType type) {
        super(player, type.getName(), 6, 21);

        this.account = Manager.getAccountController().of(player.getUniqueId());
        this.type = type;
    }

    @Override
    public void handle() {
        clear();

        List<MarketItem> list = new ArrayList<>(data.list());

        if (list.isEmpty())
            addItem(22, Item.of(Material.BARRIER, "§cThere are no items on the market..."));
        else {
            if (type.equals(MarketType.BLACK))
                list.forEach(item -> item.setPrice(item.getPrice() / 2));

            // Build Page
            buildPageItems(list, 10, (item, slot) -> {
                Account author = Manager.getAccountData().read(item.getAuthor());

                addItem(slot, Item.fromStack(item.deserialize())
                        .name("§aItem #" + item.getId())
                        .lore("§7Seller: §f" + (author != null ? author.getName() : "..."),
                                "§7Price: §f" + Util.formatNumber(item.getPrice()),
                                "",
                                "§7Created in: §f" + DateUtil.getSimpleDate(item.getCreatedAt()),
                                "",
                                "§eClick to buy!")
                        .click(event -> {

                            if (account.getCoins() < item.getPrice()) {
                                sound(MenuSound.ERROR);
                                getPlayer().sendMessage("§cYou don't have enough coins!");
                                return;
                            }

                            close();
                            sound(MenuSound.SUCCESS);

                            account.buy(item, type);
                            data.remove(item);

                            getPlayer().sendMessage("§aYou purchased a new item for " + Util.formatNumber(item.getPrice()) + ".");
                        }));
            });
        }
        display();
    }
}

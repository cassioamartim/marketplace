package dev.martim.marketplace.menu;

import dev.martim.marketplace.account.Account;
import dev.martim.marketplace.api.item.Item;
import dev.martim.marketplace.api.menu.Menu;
import dev.martim.marketplace.market.items.TransactionItem;
import dev.martim.marketplace.manager.Manager;
import dev.martim.marketplace.util.DateUtil;
import dev.martim.marketplace.util.Util;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HistoryMenu extends Menu {

    private final Account account;

    public HistoryMenu(Player player, Account account, Menu last) {
        super(player, "Transaction History", last, 6, 21);

        this.account = account;
    }

    @Override
    public void handle() {
        clear();

        List<TransactionItem> history = new ArrayList<>(account.getHistory());

        if (history.isEmpty())
            addItem(22, Item.of(Material.BARRIER, "§cThere is no transaction history..."));
        else {
            // Build Page
            buildPageItems(history, 10, (item, slot) -> {
                int id = history.indexOf(item) + 1;

                Account seller = Manager.getAccountData().read(item.getSeller());

                addItem(slot, Item.fromStack(item.deserialize())
                        .name("§aTransaction #" + id)
                        .lore("§7Sold by: §f" + (seller != null ? seller.getName() : "..."),
                                "§7Carried out: §f" + DateUtil.getSimpleDate(item.getCreatedAt()),
                                "",
                                "§7Price: §f" + Util.formatNumber(item.getPrice()),
                                "§7Purchased in: §f" + item.getMarket().getName()
                        ));
            });
        }

        if (isReturnable())
            addBackButton();

        display();
    }
}

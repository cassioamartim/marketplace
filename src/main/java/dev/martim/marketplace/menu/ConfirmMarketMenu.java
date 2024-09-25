package dev.martim.marketplace.menu;

import dev.martim.marketplace.account.Account;
import dev.martim.marketplace.api.item.Item;
import dev.martim.marketplace.api.menu.Menu;
import dev.martim.marketplace.api.menu.sound.MenuSound;
import dev.martim.marketplace.manager.Manager;
import dev.martim.marketplace.market.MarketType;
import dev.martim.marketplace.market.items.MarketItem;
import dev.martim.marketplace.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class ConfirmMarketMenu extends Menu {

    private final Account account;
    private final MarketItem item;

    private final MarketType type;

    public ConfirmMarketMenu(Player player, Account account, MarketItem item, MarketType type, Menu last) {
        super(player, Manager.getMessage("confirm-market-title"), last, 3);

        this.account = account;
        this.item = item;

        this.type = type;
    }

    @Override
    public void handle() {
        clear();

        addItem(12, Item.of(Material.RED_DYE)
                .name(Manager.getMessage("cancel-button"))
                .click(event -> {
                    sound(MenuSound.ERROR);
                    getLast().handle();
                }));

        addItem(14, Item.of(Material.LIME_DYE)
                .name(Manager.getMessage("confirm-button"))
                .click(event -> {
                    close();

                    if (account.getCoins() < item.getPrice()) {
                        sound(MenuSound.ERROR);
                        getPlayer().sendMessage(Manager.getMessage("not-enough-coins"));
                        return;
                    }

                    Account author = Manager.getAccountData().read(item.getAuthor());

                    int price = type.equals(MarketType.BLACK) ? item.getPrice() * 2 : item.getPrice();

                    if (author != null) {
                        author.addCoins(price);

                        if (type.equals(MarketType.BLACK)) {
                            Player player = Bukkit.getPlayer(author.getId());

                            if (player != null)
                                player.sendMessage(Manager.getMessage("item-purchased-black-market"));
                        }
                    }

                    ItemStack stack = item.deserialize();

                    /* Send Webhook */
                    if (Manager.getDiscord().isValid())
                        Manager.getDiscord().getWebhook(Manager.getMessage("discord-webhook-url"))
                                .sendMessage(new EmbedBuilder()
                                        .setDescription(Manager.getMessage("discord-item-message",
                                                stack.getType().name(), getPlayer().getName(), Util.formatNumber(item.getPrice()))));

                    sound(MenuSound.SUCCESS);

                    account.buy(item, type);
                    Manager.getMarketData().remove(item);

                    getPlayer().getInventory().addItem(stack);

                    getPlayer().sendMessage(Manager.getMessage("item-purchased", Util.formatNumber(item.getPrice())));
                }));

        display();
    }
}

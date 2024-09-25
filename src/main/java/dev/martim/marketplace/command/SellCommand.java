package dev.martim.marketplace.command;

import dev.martim.marketplace.command.structure.CommandBuilder;
import dev.martim.marketplace.manager.Manager;
import dev.martim.marketplace.market.items.MarketItem;
import dev.martim.marketplace.util.Serializer;
import dev.martim.marketplace.util.Util;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SellCommand extends CommandBuilder {

    public SellCommand() {
        super("sell");

        setPermission("marketplace.sell");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!isPlayer(sender)) return false;

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Manager.getMessage("sell-usage", label));
            return false;
        }

        if (!Util.isNumber(args[0])) {
            player.sendMessage(Manager.getMessage("sell-only-number"));
            return false;
        }

        int price = Integer.parseInt(args[0]);

        if (price <= 0) {
            player.sendMessage(Manager.getMessage("sell-price-cannot-zero"));
            return false;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        System.out.println(hand.getType());

        if (hand.getType() == Material.AIR) {
            player.sendMessage(Manager.getMessage("sell-cant-air"));
            return false;
        }

        byte[] data = Serializer.serialize(hand);

        Manager.getMarketData().save(new MarketItem(player.getUniqueId(), data, price));

        player.getInventory().setItemInMainHand(null);
        player.sendMessage(Manager.getMessage("sell-put-up", Util.formatNumber(price)));

        return true;
    }
}

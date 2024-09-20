package dev.martim.marketplace.command;

import dev.martim.marketplace.command.structure.CommandBuilder;
import dev.martim.marketplace.manager.Manager;
import dev.martim.marketplace.market.MarketType;
import dev.martim.marketplace.market.items.MarketItem;
import dev.martim.marketplace.menu.MarketMenu;
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
            player.sendMessage("§cUsage: /" + label + " <price>.");
            return false;
        }

        if (!Util.isNumber(args[0])) {
            player.sendMessage("§cOnly numbers is valid!");
            return false;
        }

        int price = Integer.parseInt(args[0]);

        if (price <= 0) {
            player.sendMessage("§cThe price cannot be less than or equal to zero.");
            return false;
        }

        ItemStack hand = player.getInventory().getItemInMainHand();

        System.out.println(hand.getType());

        if (hand.getType().equals(Material.AIR)) {
            player.sendMessage("§cYou can't sell an air.");
            return false;
        }

        byte[] data = Serializer.serialize(hand);

        Manager.getMarketData().save(new MarketItem(player.getUniqueId(), data, price));

        player.getInventory().setItemInMainHand(null);
        player.sendMessage("§aYou have put your item up for sale for " + Util.formatNumber(price) + "!");

        return true;
    }
}

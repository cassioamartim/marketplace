package dev.martim.marketplace.command;

import dev.martim.marketplace.command.structure.CommandBuilder;
import dev.martim.marketplace.market.MarketType;
import dev.martim.marketplace.menu.MarketMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MarketPlaceCommand extends CommandBuilder {

    public MarketPlaceCommand() {
        super("marketplace");

        setPermission("marketplace.view");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!isPlayer(sender)) return false;

        new MarketMenu((Player) sender, MarketType.DEFAULT).handle();
        return true;
    }
}

package dev.martim.marketplace.command;

import dev.martim.marketplace.account.Account;
import dev.martim.marketplace.command.structure.CommandBuilder;
import dev.martim.marketplace.manager.Manager;
import dev.martim.marketplace.util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCoinsCommand extends CommandBuilder {

    public SetCoinsCommand() {
        super("setcoins");

        setPermission("marketplace.setcoins");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!isPlayer(sender)) return false;

        Player player = (Player) sender;

        if (args.length <= 1) {
            player.sendMessage(Manager.getMessage("setcoins-usage", label));
            return false;
        }

        Account target = Manager.getAccountData().read(args[0]);

        if (target == null) {
            player.sendMessage(TARGET_NOT_FOUND);
            return false;
        }

        if (!Util.isNumber(args[1])) {
            player.sendMessage(Manager.getMessage("sell-only-numbers"));
            return false;
        }

        int coins = Integer.parseInt(args[1]);

        target.setCoins(coins);

        player.sendMessage(Manager.getMessage("setcoins-apply", Util.formatNumber(coins), target.getName()));

        return true;
    }
}

package dev.martim.marketplace.command;

import dev.martim.marketplace.account.Account;
import dev.martim.marketplace.command.structure.CommandBuilder;
import dev.martim.marketplace.manager.Manager;
import dev.martim.marketplace.menu.HistoryMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class TransactionsCommand extends CommandBuilder {

    public TransactionsCommand() {
        super("transactions");

        setPermission("marketplace.history");
        setAliases(Collections.singletonList("history"));
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!isPlayer(sender)) return false;

        Player player = (Player) sender;

        // View your own history
        if (args.length == 0) {
            new HistoryMenu(player, Manager.getAccountController().of(player.getUniqueId()), null).handle();
            return true;
        }

        Account target = Manager.getAccountData().read(args[0]);

        if (target == null) {
            player.sendMessage(TARGET_NOT_FOUND);
            return false;
        }

        new HistoryMenu(player, target, null).handle();

        return true;
    }
}

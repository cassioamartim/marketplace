package dev.martim.marketplace.command.structure;

import dev.martim.marketplace.manager.Manager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CommandBuilder extends Command {

    protected final String TARGET_NOT_FOUND = Manager.getMessage("target-not-found");

    public CommandBuilder(String name) {
        super(name);
    }

    public boolean isPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Manager.getMessage("only-players-use-command"));
            return false;
        }

        return true;
    }
}

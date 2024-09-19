package dev.martim.marketplace.command.builder;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CommandBuilder extends Command {

    public CommandBuilder(String name) {
        super(name);
    }

    public boolean isPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return false;
        }

        return true;
    }
}

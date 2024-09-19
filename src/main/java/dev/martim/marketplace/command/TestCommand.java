package dev.martim.marketplace.command;

import dev.martim.marketplace.menu.TestMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand extends Command {

    public TestCommand() {
        super("test");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] strings) {

        new TestMenu((Player) sender).handle();

        return false;
    }
}

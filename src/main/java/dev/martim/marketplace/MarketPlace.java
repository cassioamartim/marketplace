package dev.martim.marketplace;

import dev.martim.marketplace.command.structure.CommandHandler;
import dev.martim.marketplace.listener.handler.ListenerHandler;
import dev.martim.marketplace.manager.Manager;
import org.bukkit.plugin.java.JavaPlugin;

public class MarketPlace extends JavaPlugin {

    @Override
    public void onLoad() {
        super.onLoad();

        Manager.handle(getLogger());
    }

    @Override
    public void onEnable() {

        new CommandHandler(this).handle("dev.martim.marketplace.command");
        new ListenerHandler(this).handle("dev.martim.marketplace.listener");

        getLogger().info("Plugin started successfully!");
    }

    @Override
    public void onDisable() {
        super.onDisable();

        Manager.unload();
    }
}

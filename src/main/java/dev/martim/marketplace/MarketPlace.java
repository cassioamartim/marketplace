package dev.martim.marketplace;

import dev.martim.marketplace.command.TestCommand;
import dev.martim.marketplace.listener.MenuListener;
import dev.martim.marketplace.manager.Manager;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

public class MarketPlace extends JavaPlugin {

    @Override
    public void onLoad() {
        super.onLoad();

        Manager.handle(getLogger());
    }

    @Override
    public void onEnable() {

        CraftServer server = (CraftServer) getServer();

        server.getCommandMap().register("test", new TestCommand());

        getServer().getPluginManager().registerEvents(new MenuListener(), this);

        getLogger().info("Plugin started successfully!");
    }

    @Override
    public void onDisable() {
        super.onDisable();

        Manager.unload();
    }
}

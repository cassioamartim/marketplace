package dev.martim.marketplace;

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

        getLogger().info("Plugin started successfully!");
    }

    @Override
    public void onDisable() {
        super.onDisable();

        Manager.unload();
    }
}

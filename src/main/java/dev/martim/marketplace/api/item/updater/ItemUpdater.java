package dev.martim.marketplace.api.item.updater;

import org.bukkit.inventory.InventoryView;

public interface ItemUpdater {
    void runUpdate(InventoryView view);
}

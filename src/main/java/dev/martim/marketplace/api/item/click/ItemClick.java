package dev.martim.marketplace.api.item.click;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface ItemClick {
    void runClick(InventoryClickEvent event);
}

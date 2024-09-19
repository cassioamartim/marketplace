package dev.martim.marketplace.api.item.interact;

import org.bukkit.event.player.PlayerInteractEvent;

public interface ItemInteract {
    void runInteract(PlayerInteractEvent event);
}

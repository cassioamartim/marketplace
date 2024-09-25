package dev.martim.marketplace.listener;

import dev.martim.marketplace.api.item.Item;
import dev.martim.marketplace.api.item.click.ItemClick;
import dev.martim.marketplace.api.menu.Menu;
import dev.martim.marketplace.controller.list.MenuController;
import dev.martim.marketplace.manager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {

    private final MenuController controller;

    public MenuListener() {
        controller = Manager.getMenuController();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        controller.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        controller.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        controller.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerClickItem(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player && controller.contains(player)) {
            if (event.getClickedInventory() == null) {
                event.setCancelled(true);
                return;
            }

            org.bukkit.inventory.Inventory clicked = event.getClickedInventory();

            if (clicked.equals(player.getInventory())) {
                event.setCancelled(true);
                return;
            }

            Menu menu = controller.of(player.getUniqueId());

            ClickType clickType = event.getClick();

            ItemStack currentItem = event.getCurrentItem(), cursor = event.getCursor();

            if ((clickType.equals(ClickType.RIGHT) && currentItem != null && currentItem.getAmount() > 1) || clickType.equals(ClickType.NUMBER_KEY)
                    || clickType.name().contains("DROP")) {
                event.setCancelled(true);
                return;
            }

            if (event.getRawSlot() > clicked.getSize()) {
                event.setCancelled(false);
                return;
            }

            if (clickType.name().startsWith("SHIFT") && !menu.isAllowShift()) {
                event.setCancelled(true);
                return;
            }

            if (cursor != null && !cursor.getType().equals(Material.AIR) && clickType.name().contains("RIGHT")) {
                event.setCancelled(true);
                return;
            }

            Item item = menu.getContents().get(event.getSlot());

            if (item == null) return;

            event.setCancelled(menu.isProtectedContent(event.getCurrentItem()) || !menu.isAllowClick());

            if (!menu.isAllowClick())
                player.setItemOnCursor(null);

            /* Rodando o clickType */
            ItemClick itemClick = item.getClick();

            if (itemClick != null) itemClick.runClick(event);
        }
    }

    @EventHandler
    public void onInteractInventory(InventoryCloseEvent event) {
        InventoryView view = event.getView();

        ItemStack cursor = view.getCursor();

        if (cursor != null)
            view.setCursor(null);
    }

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ItemStack stack = player.getInventory().getItemInMainHand();

        if (stack.getType().equals(Material.AIR) || !stack.hasItemMeta()) return;
        if (!Item.exists(stack)) return;

        Item item = Item.convertItem(stack);

        if (event.getAction().name().startsWith("RIGHT") && item.getInteract() != null)
            item.getInteract().runInteract(event);
    }

}
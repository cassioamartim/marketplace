package dev.martim.marketplace.api.menu;

import dev.martim.marketplace.api.item.Item;
import dev.martim.marketplace.api.menu.page.ItemPageBuilder;
import dev.martim.marketplace.api.menu.sound.MenuSound;
import dev.martim.marketplace.manager.Manager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class Menu {

    private final Player player;

    private String title;
    private final String initialTitle;

    private org.bukkit.inventory.Inventory holder;

    private final Menu last;
    private final Map<Integer, Item> contents;

    private final List<ItemStack> protectedContents;
    private final List<Integer> protectedSlots;

    // Variáveis configuráveis
    private boolean returnable, paginated, allowClick, allowDrag = true, allowShift = true, showTitlePage = true;

    private int size, rows, maxItems, pageIndex, pageNumber = 1, totalPages = 1;

    public Menu(Player player, String title, Menu last, int rows, int maxItems) {
        this.player = player;

        this.title = title;
        this.initialTitle = title;

        this.last = last;
        this.contents = new HashMap<>();

        this.returnable = last != null;
        this.paginated = maxItems > 0;

        this.rows = rows;
        this.size = rows * 9;

        this.maxItems = maxItems;

        this.protectedContents = new ArrayList<>();
        this.protectedSlots = new ArrayList<>();

        this.holder = Bukkit.createInventory(player, size, title);
    }

    public Menu(Player player, String title, Menu last, int rows) {
        this(player, title, last, rows, 0);
    }

    public Menu(Player player, String title, int rows, int maxItems) {
        this(player, title, null, rows, maxItems);
    }

    public Menu(Player player, String title, int rows) {
        this(player, title, null, rows, 0);
    }

    public Menu(Player player, String title, boolean allowClick, int rows) {
        this(player, title, null, rows, 0);

        setAllowClick(allowClick);
    }

    /**
     * Inicializar o processo de criação do inventário.
     */
    public abstract void handle();

    public void display() {
        if (player == null) return;

        contents.forEach((slot, item) -> holder.setItem(slot, item));

        player.openInventory(holder);

//        if (totalPages > 0 && showTitlePage)
//            updateTitle();

        Manager.getMenuController().save(this);
    }

    public void close() {
        player.closeInventory();
    }

    public void clear() {
        if (holder != null)
            holder.clear();

        if (!contents.isEmpty())
            contents.clear();
    }

    public void clear(int... selectedSlots) {
        for (int slot : selectedSlots) {

            Item item = contents.get(slot);
            if (item == null) continue;

            item.type(Material.AIR);
            contents.remove(slot);
        }
    }

    public boolean isProtectedContent(ItemStack stack) {
        return stack != null && protectedContents.stream().anyMatch(item -> item.isSimilar(stack));
    }

    public boolean isProtectedSlot(int slot) {
        return protectedSlots.contains(slot);
    }

//    public void updateTitle() {
//        EntityPlayer entity = ((CraftPlayer) player).getHandle();
//
//        PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(
//                entity.activ.windowId,
//                "minecraft:chest",
//                new ChatMessage(title),
//                player.getOpenInventory().getTopInventory().getSize());
//
//        entity.playerConnection.sendPacket(packet);
//        entity.updateInventory(entity.activeContainer);
//    }

    public void addItem(int slot, Item item) {
        addItem(slot, item, false);
    }

    public void addItem(int slot, Item item, boolean protection) {
        contents.put(slot, item);

        if (protection)
            protectedContents.add(item);
    }

    public <T> void buildPageItems(List<T> list, int initialSlot, ItemPageBuilder<T> itemPageBuilder) {
        setTotalPages((list.size() + maxItems - 1) / maxItems);
        addBorderPage();

        if (totalPages > 0 && showTitlePage)
            setTitle(initialTitle + " (" + pageNumber + "/" + totalPages + ")");

        int last = initialSlot;

        for (int i = 0; i < maxItems; i++) {
            int index = maxItems * (pageNumber - 1) + i;
            if (index >= list.size()) break;

            T item = list.get(index);

            if (item != null)
                itemPageBuilder.accept(item, initialSlot);

            initialSlot++;
            if (initialSlot == (last + 7)) {
                initialSlot += 2;
                last = initialSlot;
            }
        }
    }

    public void addBorderPage() {
        // Adicionar botão de página anterior.
        if (pageNumber > 1) {
            addItem(getTotalSlots() - 6, Item.of(Material.ARROW, "§cPágina " + (pageNumber - 1))
                    .click(event -> {
                        setPageNumber(pageNumber - 1);

                        if (showTitlePage)
                            setTitle(initialTitle + " (" + pageNumber + "/" + totalPages + ")");

                        sound(MenuSound.PAGINATED);
                        handle();
                    }));
        }

        // Adicionar botão de próxima página.
        if (pageNumber < totalPages) {
            addItem(getTotalSlots() - 4, Item.of(Material.ARROW, "§aPágina " + (pageNumber + 1))
                    .click(event -> {
                        setPageNumber(pageNumber + 1);

                        if (showTitlePage)
                            setTitle(initialTitle + " (" + pageNumber + "/" + totalPages + ")");

                        sound(MenuSound.PAGINATED);
                        handle();
                    }));
        }
    }

    public boolean hasBackButton() {
        boolean found = false;

        for (Map.Entry<Integer, Item> entry : contents.entrySet()) {
            int slot = entry.getKey();

            Item item = entry.getValue();

            if (slot == (getTotalSlots() - 5) && item.getType().equals(Material.ARROW)) {
                found = true;
                break;
            }
        }

        return found;
    }

    public void addBackButton(int slot) {
        addItem(slot, Item.of(Material.ARROW, "§aVoltar", last != null ? "§7Para " + last.getInitialTitle() : "")
                .click(event -> {
                    if (last == null) {
                        sound(MenuSound.ERROR);
                        return;
                    }
                    last.handle();

                    sound(MenuSound.PAGINATED);
                }), true);
    }

    public void addBackButton() {
        if (pageNumber <= 1)
            addBackButton(getTotalSlots() - (totalPages > 1 ? 6 : 5));
    }

    public void sound(MenuSound sound) {
        this.sound(sound.getSound());
    }

    public void sound(Sound sound) {
        player.playSound(player.getLocation(), sound, 2f, 2.5f);
    }

    public int getTotalSlots() {
        return rows * 9;
    }
}
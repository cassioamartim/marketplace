package dev.martim.marketplace.api.item;

import dev.martim.marketplace.api.item.click.ItemClick;
import dev.martim.marketplace.api.item.interact.ItemInteract;
import dev.martim.marketplace.api.item.updater.ItemUpdater;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;

@Getter
public class Item extends ItemStack {

    @Getter
    protected static final Set<Item> itemList = new HashSet<>();

    protected ItemMeta meta = getItemMeta();

    private ItemClick click;
    private ItemInteract interact;

    private ItemUpdater updater;

    public Item(Material type) {
        super(type);
    }

    public Item(String type) {
        super(Material.getMaterial(type));
    }

    public Item(Material type, int amount) {
        super(type, amount);
    }

    public Item(Material type, int amount, int id) {
        super(type, amount, (short) id);
    }

    public static Item of(Material type) {
        return new Item(type);
    }

    public static Item of(Material type, String name) {
        return new Item(type).name(name);
    }

    public static Item of(Material type, int id) {
        return new Item(type, 1, id);
    }

    public static Item of(Material type, int id, String name, String... lore) {
        return new Item(type, 1, id).name(name).lore(lore);
    }

    public static Item of(Material type, int id, String name, List<String> lore) {
        return new Item(type, 1, id).name(name).lore(lore);
    }

    public static Item of(Material type, int id, String name) {
        return new Item(type, 1, id).name(name);
    }

    public static Item of(Material type, String name, String... lore) {
        return new Item(type).name(name).lore(lore);
    }

    public static Item of(Material type, String name, List<String> lore) {
        return new Item(type).name(name).lore(lore);
    }

    /* Object Methods */
    public String getName() {
        return getType().name();
    }

    public void updateMeta(ItemMeta meta) {
        this.meta = meta;
        setItemMeta(meta);
    }

    public static Item convertItem(ItemStack stack) {
        return itemList.stream().filter(item -> item.isSimilar(stack)).findFirst().orElse(null);
    }

    public static Item fromStack(ItemStack stack) {
        Item item = new Item(stack.getType());

        item.setAmount(stack.getAmount());
        //item.setDurability(stack.getDurability());
        item.setData(stack.getData());

        ItemMeta meta = stack.getItemMeta();

        if (meta != null) {
            if (meta.hasEnchants()) {
                for (Enchantment enchantment : meta.getEnchants().keySet()) {
                    try {
                        item.enchantment(enchantment, meta.getEnchantLevel(enchantment));
                    } catch (IllegalArgumentException ignored) {
                        item.addUnsafeEnchantment(enchantment, meta.getEnchantLevel(enchantment));
                    }
                }
            }

            item.setItemMeta(meta);
        }

        return item;
    }

    public static boolean exists(ItemStack stack) {
        return itemList.stream().anyMatch(item -> item.isSimilar(stack));
    }

    public Item click(ItemClick click) {
        this.click = click;

        itemList.add(this);
        return this;
    }

    public Item interact(ItemInteract interact) {
        this.interact = interact;

        itemList.add(this);
        return this;
    }

    public Item updater(ItemUpdater updater) {
        this.updater = updater;

        itemList.add(this);
        return this;
    }

    /* Item Stack Methods */
    public Item type(Material type) {
        setType(type);
        return this;
    }

    public Item name(String name) {
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

        updateMeta(meta);
        return this;
    }

    public Item leatherColor(Color color) {
        LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;

        armorMeta.setLore(null);
        armorMeta.setColor(color);

        updateMeta(armorMeta);
        return this;
    }

    public Item amount(int amount) {
        setAmount(amount);
        return this;
    }

    public Item lore(List<String> lore) {
        List<String> translatedLore = new ArrayList<>();

        lore.forEach(line -> translatedLore.add(ChatColor.translateAlternateColorCodes('&', line)));

        meta.setLore(translatedLore);

        updateMeta(meta);
        return this;
    }

    public Item lore(String... lore) {
        meta.setLore(Arrays.asList(lore));

        updateMeta(meta);
        return this;
    }

    public Item flags(ItemFlag... flags) {
        meta.addItemFlags(flags);

        updateMeta(meta);
        return this;
    }

    public Item enchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);

        updateMeta(meta);
        return this;
    }

    public boolean hasEnchantments() {
        return meta.hasEnchants();
    }
}

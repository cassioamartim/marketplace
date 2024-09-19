package dev.martim.marketplace.menu;

import dev.martim.marketplace.api.item.Item;
import dev.martim.marketplace.api.menu.Menu;
import dev.martim.marketplace.api.menu.sound.MenuSound;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class TestMenu extends Menu {

    public TestMenu(Player player) {
        super(player, "Menu de Testes", 4);
    }

    @Override
    public void handle() {
        clear();

        addItem(10, Item.of(Material.GOLD_INGOT)
                .click(event -> sound(MenuSound.PAGINATED)));

        addItem(11, Item.of(Material.LEATHER)
                .click(event -> sound(MenuSound.ERROR)));

        addItem(12, Item.of(Material.FLINT)
                .click(event -> sound(MenuSound.SUCCESS)));

        addItem(13, Item.of(Material.SLIME_BLOCK)
                .click(event -> sound(MenuSound.DONE)));

        display();
    }
}

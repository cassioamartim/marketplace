package dev.martim.marketplace.controller.list;

import dev.martim.marketplace.api.menu.Menu;
import dev.martim.marketplace.controller.Controller;
import org.bukkit.entity.Player;

public class MenuController extends Controller<Player, Menu> {

    @Override
    public void save(Menu menu) {
        getCache().put(menu.getPlayer(), menu);
    }
}

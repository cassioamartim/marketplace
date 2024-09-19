package dev.martim.marketplace.controller.list;

import dev.martim.marketplace.api.menu.Menu;
import dev.martim.marketplace.controller.Controller;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MenuController extends Controller<Player, Menu> {

    @Override
    public void save(Menu menu) {
        getCache().put(menu.getPlayer(), menu);
    }

    public Menu of(UUID id) {
        return of(menu -> menu.getPlayer().getUniqueId().equals(id));
    }

    public void remove(UUID id) {
        list().removeIf(menu -> menu.getPlayer().getUniqueId().equals(id));
    }
}

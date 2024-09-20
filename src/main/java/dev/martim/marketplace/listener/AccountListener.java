package dev.martim.marketplace.listener;

import dev.martim.marketplace.account.Account;
import dev.martim.marketplace.backend.data.AccountData;
import dev.martim.marketplace.controller.list.AccountController;
import dev.martim.marketplace.manager.Manager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class AccountListener implements Listener {

    private final AccountData data;
    private final AccountController controller;

    public AccountListener() {
        this.data = Manager.getAccountData();
        this.controller = Manager.getAccountController();
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID id = event.getUniqueId();

        String name = event.getName();

        try {
            Account account = data.read(id);

            if (account == null) {
                account = new Account(id, name);

                data.save(account);
            }

            if (!controller.contains(id))
                controller.save(account);

            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);

            Manager.getLogger().info("Account " + name + " logged in server.");
        } catch (Exception e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cYour account could not be loaded!");
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (!controller.contains(event.getPlayer().getUniqueId())) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "§cYour account could not be loaded!");
            return;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getInventory().clear();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        controller.remove(event.getPlayer().getUniqueId());
    }
}

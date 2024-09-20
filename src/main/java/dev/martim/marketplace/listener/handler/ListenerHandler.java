package dev.martim.marketplace.listener.handler;

import dev.martim.marketplace.manager.Manager;
import dev.martim.marketplace.util.loader.ClassLoader;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;

@RequiredArgsConstructor
public class ListenerHandler {

    private final JavaPlugin plugin;

    public void handle(String path) {
        Instant now = Instant.now();

        int loaded = 0;

        Manager.getLogger().info("Registering events...");

        for (Class<?> listenerClass : ClassLoader.getClassesForPackage(plugin, path)) {
            if (Listener.class.isAssignableFrom(listenerClass)) {
                try {
                    Listener listener = (Listener) listenerClass.newInstance();

                    plugin.getServer().getPluginManager().registerEvents(listener, plugin);

                    for (Method method : listenerClass.getMethods()) {
                        if (method.isAnnotationPresent(EventHandler.class))
                            loaded++;
                    }

                } catch (Exception e) {
                    Manager.getLogger().log(Level.SEVERE, "Unable to register the event " + listenerClass.getSimpleName(), e);
                }
            }
        }

        if (loaded > 0)
            Manager.getLogger().info("Event registration completed. (Total events recorded: " + loaded + " in " + Duration.between(Instant.now(), now).toMillis() + "ms)");
    }
}
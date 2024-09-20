package dev.martim.marketplace.command.structure;

import dev.martim.marketplace.manager.Manager;
import dev.martim.marketplace.util.loader.ClassLoader;
import lombok.RequiredArgsConstructor;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Level;

@RequiredArgsConstructor
public class CommandHandler {

    private final JavaPlugin plugin;

    public void handle(String path) {
        Instant now = Instant.now();

        int loaded = 0;

        Manager.getLogger().info("Registering commands...");

        for (Class<?> commandClass : ClassLoader.getClassesForPackage(plugin, path)) {
            if (commandClass.equals(CommandBuilder.class)) continue;

            if (CommandBuilder.class.isAssignableFrom(commandClass)) {
                try {
                    CommandBuilder command = (CommandBuilder) commandClass.getDeclaredConstructor().newInstance();

                    ((CraftServer) plugin.getServer()).getCommandMap().register(command.getName(), command);

                    loaded++;
                } catch (Exception e) {
                    Manager.getLogger().log(Level.SEVERE, "Unable to register the command " + commandClass.getSimpleName(), e);
                }
            }
        }

        if (loaded > 0)
            Manager.getLogger().info("Command registration completed. (Total commands recorded: " + loaded + " in " + Duration.between(Instant.now(), now).toMillis() + "ms)");
    }
}

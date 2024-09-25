package dev.martim.marketplace.discord;

import dev.martim.marketplace.manager.Manager;
import lombok.Getter;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.webhook.IncomingWebhook;

@Getter
public class Discord {

    private DiscordApi api;

    public Discord() {
        new DiscordApiBuilder()
                .setToken(Manager.getMessage("discord-bot-token"))
                .addIntents(Intent.GUILD_MEMBERS)
                .login()
                .thenAccept(this::connect)
                .join();
    }

    public void connect(DiscordApi api) {
        this.api = api;

        api.updateActivity(ActivityType.PLAYING, "Marketplace.");

        Manager.getLogger().info("Bot started with successfully.");
    }

    public void disconnect() {
        api.disconnect().join();
    }

    public IncomingWebhook getWebhook(String link) {
        return api.getIncomingWebhookByUrl(link).join();
    }

    public boolean isValid() {
        return api != null;
    }
}

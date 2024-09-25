package dev.martim.marketplace.manager;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import dev.martim.marketplace.MarketPlace;
import dev.martim.marketplace.backend.data.AccountData;
import dev.martim.marketplace.backend.data.MarketData;
import dev.martim.marketplace.backend.database.mongodb.MongoDatabase;
import dev.martim.marketplace.controller.list.AccountController;
import dev.martim.marketplace.controller.list.MenuController;
import dev.martim.marketplace.discord.Discord;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;

import java.util.logging.Logger;

public class Manager {

    @Getter
    @Setter
    private static Logger logger;

    @Getter
    @Setter
    private static Discord discord;

    @Getter
    @Setter
    private static MongoDatabase mongoDB;

    @Getter
    @Setter
    private static AccountData accountData;

    @Getter
    @Setter
    private static MarketData marketData;

    @Getter
    private final static AccountController accountController = new AccountController();

    @Getter
    private final static MenuController menuController = new MenuController();

    public static final Gson GSON = new Gson();
    public static final JsonParser PARSER = new JsonParser();

    public static void handle(Logger logger) {
        setLogger(logger);

        MongoDatabase mongoDatabase = new MongoDatabase(true);

        mongoDatabase.load();

        setMongoDB(mongoDatabase);

        setAccountData(new AccountData(mongoDatabase));
        setMarketData(new MarketData(mongoDatabase));

        setDiscord(new Discord());
    }

    public static void unload() {
        if (mongoDB != null)
            mongoDB.unload();

        if (discord != null)
            discord.disconnect();
    }

    public static String getMessage(String field, Object... args) {
        String message = MarketPlace.getPlugin(MarketPlace.class).getConfig().getString(field);

        if (message != null) {
            message = String.format(message, args);

            return ChatColor.translateAlternateColorCodes('&', message);
        }

        return "§cAn error occurred while loading!";
    }
}

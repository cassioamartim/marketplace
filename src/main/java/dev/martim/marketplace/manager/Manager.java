package dev.martim.marketplace.manager;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import dev.martim.marketplace.backend.data.AccountData;
import dev.martim.marketplace.backend.data.MarketData;
import dev.martim.marketplace.backend.database.mongodb.MongoDatabase;
import dev.martim.marketplace.controller.list.AccountController;
import dev.martim.marketplace.controller.list.MenuController;
import lombok.Getter;
import lombok.Setter;

import java.util.logging.Logger;

public class Manager {

    @Getter
    @Setter
    private static Logger logger;

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
    }

    public static void unload() {
        if (mongoDB != null)
            mongoDB.unload();
    }
}

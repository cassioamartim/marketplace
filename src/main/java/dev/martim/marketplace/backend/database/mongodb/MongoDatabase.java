package dev.martim.marketplace.backend.database.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import dev.martim.marketplace.backend.database.Database;
import dev.martim.marketplace.backend.database.DatabaseCredentials;
import dev.martim.marketplace.manager.Manager;
import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.regex.Pattern;

@Getter
public class MongoDatabase implements Database {

    private static final String PATTERN = "([01]?[0-9]{1,2}|2[0-4][0-9]|25[0-5])";

    private static final Pattern IP_PATTERN = Pattern
            .compile(PATTERN + "\\." + PATTERN + "\\." + PATTERN + "\\." + PATTERN);

    private final String url;

    private final DatabaseCredentials credentials;

    private MongoClient client;
    private com.mongodb.client.MongoDatabase database;

    public MongoDatabase(String url, String database) {
        this.url = url;

        this.credentials = DatabaseCredentials.builder().database(database).build();
    }

    public MongoDatabase(DatabaseCredentials credential) {
        this.url = (IP_PATTERN.matcher(credential.getHost()).matches()
                ? "mongodb://" + (credential.getUser() == null ? "" : credential.getUser() + ":" + credential.getPassword() + "@")
                + credential.getHost() + ":" + credential.getPort() + "/" + credential.getDatabase()
                + "?retryWrites=true&w=majority"
                : "mongodb+srv://" + ((credential.getUser() == null || credential.getUser().isEmpty()) ? "" : credential.getUser() + ":" + credential.getPassword() + "@")
                + credential.getHost() + ":" + credential.getPort() + "/"
                + credential.getDatabase() + "?retryWrites=true&w=majority");

        this.credentials = credential;
    }

    public MongoDatabase(boolean local) {
        this(local
                // Local
                ? DatabaseCredentials.builder()
                .host("127.0.0.1")
                .port(27017)
                .database("marketplace")
                .build()
                // Server
                : DatabaseCredentials.builder().build());
    }

    @Override
    public void load() {
        Instant now = Instant.now();

        Manager.getLogger().info("[MongoDB] Starting MongoDB...");

        /* Efetuando Conexão */
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                ConnectionString connectionString = new ConnectionString(url);

                MongoClientSettings settings = MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .build();

                client = MongoClients.create(settings);
                database = client.getDatabase(credentials.getDatabase());

                break;
            } catch (Exception e) {
                Manager.getLogger().info("[MongoDB] Error establishing connection to MongoDB. Attempt " + (i + 1) + " of " + maxRetries);

                try {
                    Thread.sleep(1000); // Aguarda 1 segundo antes de tentar novamente
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (client == null)
            Manager.getLogger().info("[MongoDB] Unable to connect to MongoDB after " + maxRetries + " attempts. Check credentials.");
        else
            Manager.getLogger().info("[MongoDB] Connection to MongoDB successful. (Average time: " + Duration.between(now, Instant.now()).toMillis() + "ms)");
    }

    @Override
    public void unload() {
        if (isAvailable())
            client.close();
    }

    @Override
    public boolean isAvailable() {
        return client != null;
    }
}

package dev.martim.marketplace.backend.data;

import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexModel;
import dev.martim.marketplace.controller.list.AccountController;
import dev.martim.marketplace.manager.Manager;
import dev.martim.marketplace.account.Account;
import dev.martim.marketplace.backend.database.mongodb.MongoDatabase;
import dev.martim.marketplace.util.JsonUtil;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AccountData {

    private final MongoCollection<Document> collection;

    private final AccountController controller;

    public AccountData(MongoDatabase mongo) {
        this.collection = mongo.getDatabase().getCollection("accounts");

        this.collection.createIndexes(Arrays.asList(
                new IndexModel(new Document("id", 1)),
                new IndexModel(new Document("name", 1))
        ));

        this.controller = Manager.getAccountController();
    }

    public void save(Account account) {
        Document document = collection.find(Filters.eq("id", account.getId().toString())).first();

        if (document == null) {
            collection.insertOne(Document.parse(Manager.GSON.toJson(account)));

            controller.save(account);
        }
    }

    public Account read(UUID id) {
        Account account = controller.of(id);

        if (account == null) {
            Document document = collection.find(Filters.eq("id", id.toString())).first();

            if (document != null)
                account = Manager.GSON.fromJson(Manager.GSON.toJson(document), Account.class);
        }

        return account;
    }

    public Account read(String name) {
        Account account = controller.of(search -> search.getName().equalsIgnoreCase(name));

        if (account == null) {
            Document document = collection.find(Filters.eq("name", name)).first();

            if (document != null)
                account = Manager.GSON.fromJson(Manager.GSON.toJson(document), Account.class);
        }

        return account;
    }

    public void save(Account account, String field) {
        JsonObject tree = JsonUtil.jsonTree(account);

        Object value = tree.has(field) ? JsonUtil.elementToBson(tree.get(field)) : null;

        Document document = collection.find(Filters.eq("id", account.getId().toString())).first();

        if (document != null) {
            collection.updateOne(document, new Document(value != null ? "$set" : "$unset", new Document(field, value)));
        }
    }

    public List<Account> list() {
        return collection.find()
                .map(document -> Manager.GSON.fromJson(Manager.GSON.toJson(document), Account.class))
                .into(new ArrayList<>());
    }
}

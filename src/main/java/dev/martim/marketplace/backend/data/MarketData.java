package dev.martim.marketplace.backend.data;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import dev.martim.marketplace.backend.database.mongodb.MongoDatabase;
import dev.martim.marketplace.market.items.MarketItem;
import dev.martim.marketplace.manager.Manager;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MarketData {

    private final MongoCollection<Document> collection;

    public MarketData(MongoDatabase mongo) {
        this.collection = mongo.getDatabase().getCollection("market_items");
    }

    public void save(MarketItem item) {
        Document document = collection.find(Filters.eq("id", item.getId())).first();

        if (document == null)
            collection.insertOne(Document.parse(Manager.GSON.toJson(item)));
    }

    public void remove(MarketItem item) {
        Document document = collection.find(Filters.eq("id", item.getId())).first();

        if (document != null)
            collection.deleteOne(document);
    }

    public MarketItem get(int id) {
        Document document = collection.find(Filters.eq("id", id)).first();

        return document != null ? Manager.GSON.fromJson(Manager.GSON.toJson(document), MarketItem.class) : null;
    }

    public List<MarketItem> list() {
        return collection.find()
                .map(document -> Manager.GSON.fromJson(Manager.GSON.toJson(document), MarketItem.class))
                .into(new ArrayList<>());
    }
}

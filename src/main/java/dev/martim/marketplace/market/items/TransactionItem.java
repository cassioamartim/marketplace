package dev.martim.marketplace.market.items;

import dev.martim.marketplace.market.MarketType;
import dev.martim.marketplace.util.Serializer;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
public class TransactionItem {

    private final UUID seller;
    private final MarketType market;

    private final byte[] data;

    private final int price;
    private final long createdAt;

    public TransactionItem(MarketItem item, MarketType market) {
        this.seller = item.getAuthor();
        this.market = market;

        this.data = item.getData();
        this.price = item.getPrice();

        this.createdAt = System.currentTimeMillis();
    }

    public ItemStack deserialize() {
        return Serializer.deserialize(data);
    }
}

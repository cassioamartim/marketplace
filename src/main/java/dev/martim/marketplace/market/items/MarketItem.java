package dev.martim.marketplace.market.items;

import dev.martim.marketplace.manager.Manager;
import dev.martim.marketplace.util.Serializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class MarketItem {

    private final int id = Manager.getMarketData().list().size() + 1;

    private final UUID author;
    private final byte[] data;

    private int price;
    private final long createdAt = System.currentTimeMillis();

    public ItemStack deserialize() {
        return Serializer.deserialize(data);
    }
}

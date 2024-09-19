package dev.martim.marketplace.item.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class TransactionItem {

    private final UUID seller;
    private final TransactionType type;

    private final long createdAt = System.currentTimeMillis();

    public enum TransactionType {
        NORMAL, BLACK_MARKET
    }
}

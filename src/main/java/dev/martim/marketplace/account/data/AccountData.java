package dev.martim.marketplace.account.data;

import dev.martim.marketplace.market.items.TransactionItem;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class AccountData {

    private final Set<TransactionItem> history = new HashSet<>();

    private int coins;

    private final long createdAt = System.currentTimeMillis();
}

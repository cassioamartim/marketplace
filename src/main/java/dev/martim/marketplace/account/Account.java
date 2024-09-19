package dev.martim.marketplace.account;

import dev.martim.marketplace.account.data.AccountData;
import dev.martim.marketplace.item.MarketItem;
import dev.martim.marketplace.item.transaction.TransactionItem;
import dev.martim.marketplace.manager.Manager;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
public class Account {

    private final UUID id;
    private final String name;

    private AccountData data;

    public Account(UUID id, String name) {
        this.id = id;
        this.name = name;

        this.data = new AccountData();
    }

    protected void save(String... fields) {
        for (String field : fields) {
            Manager.getAccountData().save(this, field);
        }
    }

    /* Account Data Methods */
    public void saveData(AccountData data) {
        this.data = data;
        save("data");
    }

    public Set<TransactionItem> getHistory() {
        return data.getHistory();
    }

    public void buy(MarketItem item, TransactionItem.TransactionType type) {
        getHistory().add(new TransactionItem(item.getAuthor(), type));
        saveData(data);
    }

}

package dev.martim.marketplace.controller.list;

import dev.martim.marketplace.account.Account;
import dev.martim.marketplace.controller.Controller;

import java.util.UUID;

public class AccountController extends Controller<UUID, Account> {

    @Override
    public void save(Account account) {
        getCache().put(account.getId(), account);
    }
}

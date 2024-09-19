package dev.martim.marketplace.backend.database;

public interface Database {

    void load();

    void unload();

    boolean isAvailable();
}

package com.gavin.app.pool;

import java.sql.Connection;

/**
 * @author gavin
 * @date 2023/4/11 12:02 AM
 */
final public class MyPoolEntry {
    Connection connection;

    long lastAccessed;

    private volatile boolean evict;

    private final MyHikariPool hikariPool;

    MyPoolEntry(final Connection connection, final MyPoolBase poolBase) {
        this.connection = connection;
        this.hikariPool = (MyHikariPool) poolBase;
        this.lastAccessed = System.currentTimeMillis();
    }
}

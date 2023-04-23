package com.gavin.app.pool;

import java.sql.Connection;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author gavin
 * @date 2023/4/11 12:02 AM
 */
final public class MyPoolEntry implements MyConcurrentBag.IConcurrentBagEntry {

    private static final AtomicIntegerFieldUpdater<MyPoolEntry> stateUpdater;
    Connection connection;

    long lastAccessed;

    private volatile int state = 0;

    private volatile boolean evict;

    private final MyHikariPool hikariPool;

    static {
        stateUpdater = AtomicIntegerFieldUpdater.newUpdater(MyPoolEntry.class, "state");
    }

    MyPoolEntry(final Connection connection, final MyPoolBase poolBase) {
        this.connection = connection;
        this.hikariPool = (MyHikariPool) poolBase;
        this.lastAccessed = System.currentTimeMillis();
    }

    @Override
    public boolean compareAndSet(int expectState, int newState) {
        return stateUpdater.compareAndSet(this, expectState, newState);
    }

    @Override
    public void setState(int newState) {
        stateUpdater.set(this, newState);
    }

    @Override
    public int getState() {
        return stateUpdater.get(this);
    }

    private MyPoolEntry newPoolEntry() {
        return null;
    }
}

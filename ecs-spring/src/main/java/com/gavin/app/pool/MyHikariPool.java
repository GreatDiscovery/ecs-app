package com.gavin.app.pool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 1. 需要一个容器存放连接
 * 2. 需要管理线程池的生命周期
 *
 * @author gavin
 * @date 2023/4/11 12:11 AM
 */
public class MyHikariPool extends MyPoolBase implements MyConcurrentBag.MyBagEntryListener, MyHikariPoolMXBean {

    public volatile int poolState;
    public static final int POOL_NORMAL = 0;
    public static final int POOL_SUSPENDED= 1;
    public static final int POOL_SHUTDOWN = 2;
    private final MyConcurrentBag<MyPoolEntry> concurrentBag;

    private final Collection<Runnable> addConnectionQueue;
    private final ThreadPoolExecutor addConnectionExecutor;

    private final MyPoolEntryCreator POOL_ENTRY_CREATOR = new MyPoolEntryCreator(null /*logging prefix*/);

    public MyHikariPool(final MyHikariConfig config) {
        super(config);
        this.concurrentBag = new MyConcurrentBag<MyPoolEntry>(this);
        LinkedBlockingQueue<Runnable> addConnectionQueue = new LinkedBlockingQueue<>(config.getMaxPoolSize());
        this.addConnectionQueue = Collections.unmodifiableCollection(addConnectionQueue);
        this.addConnectionExecutor = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, addConnectionQueue, new ThreadFactoryBuilder().setNameFormat("connection adder").build(), new ThreadPoolExecutor.DiscardPolicy());
    }

    @Override
    public void addBagEntry(int waiting) {
        final boolean shouldAdd = waiting - addConnectionQueue.size() > 0;
        if (shouldAdd) {
            addConnectionExecutor.submit(POOL_ENTRY_CREATOR);
        }
    }

    @Override
    public int getTotalConnections() {
        return concurrentBag.size();
    }

    private final class MyPoolEntryCreator implements Callable<Boolean> {

        public MyPoolEntryCreator(String prefix) {
        }

        @Override
        public Boolean call() throws Exception {
            while (poolState == POOL_NORMAL && shouldCreateAnotherConnection()) {
                MyPoolEntry poolEntry = createPoolEntry();
                if (poolEntry != null) {
                    concurrentBag.add(poolEntry);
                    return true;
                }
            }
            return false;
        }

        private boolean shouldCreateAnotherConnection() {
            return getTotalConnections() < config.getMaxPoolSize();
        }
    }

    private MyPoolEntry createPoolEntry() {
//        MyPoolEntry poolEntry = new();
        return null;
    }
}

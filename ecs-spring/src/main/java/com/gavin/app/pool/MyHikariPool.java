package com.gavin.app.pool;

/**
 * 1. 需要一个容器存放连接
 * 2. 需要管理线程池的生命周期
 * @author gavin
 * @date 2023/4/11 12:11 AM
 */
public class MyHikariPool extends MyPoolBase implements MyConcurrentBag.MyBagEntryListener {

    private final MyConcurrentBag<MyPoolEntry> concurrentBag;



    public MyHikariPool(final MyHikariConfig config) {
        super(config);
        this.concurrentBag = new MyConcurrentBag<MyPoolEntry>(this);
    }

    @Override
    public void addBagEntry(int waiting) {

    }
}

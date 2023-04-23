package com.gavin.app.pool;

/**
 * @author gavin
 * @date 2023/4/11 12:11 AM
 */
public class MyPoolBase {

    public final MyHikariConfig config;

    public MyPoolBase(final MyHikariConfig config) {
        this.config = config;
    }

    MyPoolEntry newPoolEntry() {
        return new MyPoolEntry(newConnection(), this, );
    }
}

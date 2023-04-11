package com.gavin.app.pool;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author gavin
 * @date 2023/4/11 11:49 PM
 */
public class MyConcurrentBag<T extends MyConcurrentBag.IConcurrentBagEntry> {

    private final CopyOnWriteArrayList<T> sharedList;
    public interface IConcurrentBagEntry {
        int STATE_NOT_IN_USER = 0;
        int STATE_IN_USE = 1;
        int STATE_REMOVED = -1;
        int STATE_RESERVED = -2;

        boolean compareAndSet(int expectState, int newState);

        void setState(int newState);
        int getState();
    }

    public MyConcurrentBag() {
        this.sharedList = new CopyOnWriteArrayList<T>();
    }
}

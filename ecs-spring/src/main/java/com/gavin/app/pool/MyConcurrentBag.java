package com.gavin.app.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gavin
 * @date 2023/4/11 11:49 PM
 */
public class MyConcurrentBag<T extends MyConcurrentBag.IConcurrentBagEntry> {

    private final CopyOnWriteArrayList<T> sharedList;

    private final ThreadLocal<List<Object>> threadList;

    private final AtomicInteger waiters;

    private MyBagEntryListener listener;

    public interface IConcurrentBagEntry {
        int STATE_NOT_IN_USER = 0;
        int STATE_IN_USE = 1;
        int STATE_REMOVED = -1;
        int STATE_RESERVED = -2;

        boolean compareAndSet(int expectState, int newState);

        void setState(int newState);
        int getState();
    }

    public interface MyBagEntryListener {
        void addBagEntry(int waiting);
    }

    public MyConcurrentBag(final MyBagEntryListener listener) {
        this.sharedList = new CopyOnWriteArrayList<T>();
        this.threadList = ThreadLocal.withInitial(() -> new ArrayList<Object>(16));
        this.waiters = new AtomicInteger();
        this.listener = listener;
    }

    public void add(final T bagEntry) {
        sharedList.add(bagEntry);
    }

    public T borrow(long timeout, final TimeUnit timeUnit) {
        final List<Object> list = threadList.get();
        for (int i = list.size() - 1; i >= 0; i--) {
            final Object entry = list.remove(i);
            final T bagEntry = (T) entry;
            if (bagEntry != null && bagEntry.compareAndSet(IConcurrentBagEntry.STATE_NOT_IN_USER, IConcurrentBagEntry.STATE_IN_USE)) {
                return bagEntry;
            }
        }

        final int waiting = waiters.incrementAndGet();
        for (T bagEntry : sharedList) {
            if (bagEntry.compareAndSet(IConcurrentBagEntry.STATE_NOT_IN_USER, IConcurrentBagEntry.STATE_IN_USE)) {
                if (waiting > 1) {
                    listener.addBagEntry(waiting - 1);
                }
                return bagEntry;
            }
        }
        return null;
    }
}

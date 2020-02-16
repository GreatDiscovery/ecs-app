package com.gavin.app.util.algorithm.map;

/**
 * @author gavin
 * @date 2020/2/15 10:59 上午
 */
public class Weighers {
    public static <K,V> EntryWeigher<K, V> entrySingleton() {
        return (EntryWeigher<K, V>) SingletonEntryWeigher.INSTANCE;
    }

    enum SingletonEntryWeigher implements EntryWeigher<Object, Object> {
        INSTANCE;

        @Override
        public int weightOf(Object o, Object o2) {
            return 1;
        }
    }
}

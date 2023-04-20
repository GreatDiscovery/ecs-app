package com.gavin.app.pool;

import java.util.concurrent.Callable;

/**
 * @author gavin
 * @date 2023/4/20 11:22 PM
 */
public class MyPoolEntryCreator implements Callable<Boolean> {

    public MyPoolEntryCreator(String prefix) {
    }

    @Override
    public Boolean call() throws Exception {
        return null;
    }
}

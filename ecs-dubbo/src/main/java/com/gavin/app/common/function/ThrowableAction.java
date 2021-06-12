package com.gavin.app.common.function;

/**
 *
 * 用于在lamdda表达式中跑出异常
 * @author gavin
 * @date 2021/6/12 下午11:32
 */
@FunctionalInterface
public interface ThrowableAction {
    void execute() throws Throwable;

    static void execute(ThrowableAction throwableAction) throws RuntimeException {
        try {
            throwableAction.execute();
        } catch (Throwable e) {
            throw new RuntimeException();
        }
    }
}

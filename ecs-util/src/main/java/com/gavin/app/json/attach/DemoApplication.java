package com.gavin.app.json.attach;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author gavin
 * @date 2021/10/19 下午11:29
 */
public class DemoApplication {

    private static Logger LOGGER = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            testExceptionTruncate();
        }
    }

    public static void testExceptionTruncate() throws Exception {
        Thread.sleep(5000);
        try {
            // 人工构造异常抛出的场景
            ((Object) null).getClass();
        } catch (Exception e) {
            if (e.getStackTrace().length == 0) {
                System.out.println("stack miss;");
                try {
                    // 堆栈消失的时候当前线程休眠5秒，便于观察
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                    // do nothing
                }
            }
        }
        System.out.println("stack still exist;");
    }
}

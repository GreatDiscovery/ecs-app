package com.gavin.app.exception;

/**
 * 消失的堆栈，在某次循环中，会出现堆栈为空的情况
 * 丢失的原因的解释：https://mp.weixin.qq.com/s?__biz=MzkyMjIzOTQ3NA==&mid=2247484608&idx=1&sn=41fada59ca9e417c1dee1c310ae8d3d9&source=41#wechat_redirect
 * 被直接编译为Native Code，因此丢失了堆栈
 * @author gavin
 * @date 2021/10/19 下午11:19
 */
public class DisappearStack {
    public static void main(String[] args) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            System.out.println("times:" + i + " , result:" + testExceptionTrunc());
        }
    }

    public static boolean testExceptionTrunc() {
        try {
            // 人工构造异常抛出的场景
            ((Object) null).getClass();
        } catch (Exception e) {
            if (e.getStackTrace().length == 0) {
                try {
                    // 堆栈消失的时候当前线程休眠5秒，便于观察
                    Thread.sleep(5000);
                } catch (InterruptedException interruptedException) {
                    // do nothing
                }
                return true;
            }
        }
        return false;
    }
}

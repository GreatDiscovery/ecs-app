package com.gavin.app.util.algorithm.multiThread;

/**
 * 3个线程顺序执行
 *
 * @author gavin
 * @date 2020/8/13 10:35 下午
 */
public class MultiThread {
    public static void main(String[] args) {
        Thread t1 = new Thread(new Work(null));
        Thread t2 = new Thread(new Work(t1));
        Thread t3 = new Thread(new Work(t2));
        t1.start();
        t2.start();
        t3.start();
    }

    static class Work implements Runnable {
        private Thread beforeThread;
        public Work(Thread thread) {
            beforeThread = thread;
        }

        @Override
        public void run() {
            if (beforeThread != null) {
                try {
                    beforeThread.join();
                    System.out.println("thread start:" + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("thread start:" + Thread.currentThread().getName());
            }
        }
    }
}

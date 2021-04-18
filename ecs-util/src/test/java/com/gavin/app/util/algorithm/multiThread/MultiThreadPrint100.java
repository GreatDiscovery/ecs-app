package com.gavin.app.util.algorithm.multiThread;

/**
 * 多线程交替打印100
 *
 * @author gavin
 * @date 2021/4/18 上午9:55
 */
public class MultiThreadPrint100 {
    public static void main(String[] args) {
        PrintNumber printNumber = new PrintNumber();
        new Thread(printNumber, "线程1").start();
        new Thread(printNumber, "线程2").start();
        new Thread(printNumber, "线程3").start();
    }
}

class PrintNumber implements Runnable {
    private int i = 0;
    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                notify();
                if (i < 100) {
                    i++;
                    System.out.println(Thread.currentThread().getName() + ":" + i);
                } else {
                    break;
                }
                try {
                    wait();
                } catch (Exception e){

                }
            }
        }
    }
}

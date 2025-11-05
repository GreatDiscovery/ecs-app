package com.gavin.app.base;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

/**
 * PhantomReference 的核心特性
 * <p>
 * 必须与 ReferenceQueue 配合使用
 * <p>
 * 当 GC 判定某个对象“已死亡”（不可达）后，会把对应的 PhantomReference 加入到 ReferenceQueue 中。
 * <p>
 * 你可以在队列中检测到它，从而执行资源清理逻辑。
 * <p>
 * 无法通过 get() 访问对象
 * <p>
 * phantomRef.get() 永远返回 null。
 * <p>
 * 说明此时对象已经“即将”或“已经”被 GC 回收，不能再被访问。
 * <p>
 * 主要用于代替 finalize() 做更安全的资源释放
 * <p>
 * finalize() 已被弃用，且执行不可靠。
 * <p>
 * PhantomReference + ReferenceQueue 是更现代、更安全的替代方案。
 */
public class PhantomReferenceTEst {
    public static void main(String[] args) throws InterruptedException {
        ReferenceQueue<Object> refQueue = new ReferenceQueue<>();
        Object obj = new Object();
        PhantomReference<Object> phantomRef = new PhantomReference<>(obj, refQueue);

        System.out.println("Before GC: " + phantomRef.isEnqueued()); // false

        obj = null; // 清除强引用
        System.gc(); // 触发垃圾回收

        Thread.sleep(100); // 等待GC线程执行

        System.out.println("After GC: " + phantomRef.isEnqueued()); // true
        if (phantomRef.isEnqueued()) {
            System.out.println("Object is ready to be reclaimed.");
        }
    }
}

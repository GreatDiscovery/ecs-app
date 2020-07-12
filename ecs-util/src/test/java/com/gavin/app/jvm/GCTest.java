package com.gavin.app.jvm;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * 实验gc相关的原理
 *
 * @author gavin
 * @date 2020/6/21 3:10 下午
 */
public class GCTest {

    private static ReferenceQueue<Object> rq = new ReferenceQueue<Object>();
    public static void main(String[] args){

        Object obj = new Object();
        PhantomReference<Object> pr = new PhantomReference<Object>(obj, rq);
        System.out.println(pr.get());
        obj = null;
        System.gc();
//        System.out.println(pr.get());
        Reference<Object> r = (Reference<Object>)rq.poll();
        if(r!=null){
            System.out.println("回收");
        }
    }
}


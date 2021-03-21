package com.gavin.app.util.algorithm.sort;

import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.longlong.Roaring64Bitmap;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

/**
 * 精确去重工具测试。针对long类型的去重
 *
 * @author gavin
 * @date 2021/3/20 下午11:17
 */
public class RoaringBitmapTest {
    public static final int ONE_MILLION = 100_0000;
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(10000);
        System.out.println("start");
        List<Integer> list = new ArrayList<>(ONE_MILLION);
        for (int i = 0; i < ONE_MILLION; i++) {
            list.add(i);
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("插入100W数据");
        RoaringBitmap bitmap = new RoaringBitmap();
        for (int i = 0; i < list.size(); i++) {
            bitmap.add(list.get(i));
        }
        stopWatch.stop();
        int duplication = 0;
        stopWatch.start("查重100W数据");
        for (int i = 0; i < list.size(); i++) {
            if (bitmap.contains(list.get(i))) {
                duplication++;
            }
        }
        stopWatch.stop();
        System.out.println("duplication = " + duplication);
        System.out.println(stopWatch.prettyPrint());
        Thread.sleep(5000);
        System.out.println("end");
    }
}

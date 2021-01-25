package com.gavin.app.util.algorithm.average;

/**
 * 指数平滑算法，被用于计算负载均衡
 * S(t) = yt * a + S(t-1) * (1 - a)
 *  a越大，则近期的值权重越大
 *  系统负载，
 * @author: Gavin
 * @date: 2021/1/25 19:57
 * @description:
 */
public class ExponentialSmoothingTest {
    public static void main(String[] args) {
        int[] samples = {5, 7, 10, 15, 6, 8, 9, 5, 6};
        int sum = 0;
        for (int i = 0; i < samples.length; i++) {
            sum += samples[i];
        }
        double avg = ((double) sum) / samples.length;
        System.out.println("avg=" + avg);

        double st_1 = 3;
        double a = 0.920044415;
        double st;
        for (int i = 0; i < samples.length; i++) {
            st = samples[i] * a + (1 - a) * st_1;
            st_1 = st;
            System.out.println("第" + i + "分钟负载为:" + st);
        }
    }
}

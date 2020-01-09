package com.gavin.app.util.algorithm;

import java.util.Random;

/**
 * 蓄水池算法抽签
 *
 * @author: Gavin
 * @date: 2020/1/9 20:18
 * @description:
 */
public class Lottery {

    public static String[] members = new String[15];

    static {
        members[0] = "1";
        members[1] = "2";
        members[2] = "3";
        members[3] = "4";
        members[4] = "5";
        members[5] = "6";
        members[6] = "7";
        members[7] = "8";
        members[8] = "9";
        members[9] = "10";
        members[10] = "11";
        members[11] = "12";
        members[12] = "13";
        members[13] = "14";
        members[14] = "15";
    }

    public static String sampling() {
        int k = 1;
        String[] winner = new String[k];
        winner[0] = members[0];
        Random random = new Random();

        for (int i = k; i < members.length; i++) {
            int r = random.nextInt(i + 1);
            if (r < k) {
                winner[0] = members[i];
            }
        }
        return winner[0];
    }

    public static void main(String[] args) {
        System.out.println(sampling());
    }
}

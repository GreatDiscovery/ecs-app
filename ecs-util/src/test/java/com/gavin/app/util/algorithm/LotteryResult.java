package com.gavin.app.util.algorithm;


import com.gavin.app.json.internal.LinkedTreeMap;

import java.util.Map;

/**
 * 抽奖结果验证
 *
 * @author: Gavin
 * @date: 2020/1/9 21:02
 * @description:
 */
public class LotteryResult extends Lottery {
    public static void main(String[] args) {
        Map<String, Integer> result = new LinkedTreeMap();
        for (int i = 0; i < members.length; i++) {
            result.put(members[i], 0);
        }

        for (int i = 0; i < 1000000; i++) {
            String winner = sampling();
            result.put(winner, (result.get(winner) + 1));
        }

        System.out.println(result.toString());
    }
}

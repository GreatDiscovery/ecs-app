package com.gavin.app.util.algorithm;

import java.util.*;

/**
 * 子串能否组成主串：
 * 字符串：01010101010001101
 * 子串：[01,10,010,101,100]
 * 子串能否组成主串，每个子串可以使用0次或者多次，顺序无关。
 * <p>
 * 非递归的中序遍历 -》左中右
 *
 * @author gavin
 * @date 2020/8/15 10:54 上午
 */
public class StringDp {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Arrays.asList("01", "10", "010", "101", "100"));
        String s = "01010101010001101";
        System.out.println(wordBreak(s, list));
    }

    public static boolean wordBreak(String s, List<String> wordDict) {
        Set<String> set = new HashSet<>(wordDict);
        boolean[] dp = new boolean[wordDict.size() + 1];
        dp[0] = true;
        for (int i = 1; i < wordDict.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && set.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }
}


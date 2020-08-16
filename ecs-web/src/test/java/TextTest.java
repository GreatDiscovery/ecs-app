import org.apache.commons.lang.StringEscapeUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;

/**
 * @author: Gavin
 * @date: 2020/3/6 18:45
 * @description:
 */
public class TextTest {
    public static void main(String[] args) {
//        Map<String, Integer> map = new HashMap<>();
        ConcurrentMap<String, Integer> map = new ConcurrentHashMap();
        map.put("1", 1);
        Thread thread1 = new Thread(() -> {
            map.put("1", map.get("1") + 1);
        });
        Thread thread2 = new Thread(() -> {
            map.put("1", map.get("1") + 1);
        });
        thread1.start();
        thread2.start();
        System.out.println(map);
    }
}

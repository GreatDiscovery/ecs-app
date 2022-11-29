package com.gavin.app.timezone;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author QiangZhi
 * @date 2022/11/29 11:32 上午
 */
public class TimezoneTest {
    public static void main(String[] args) {
        System.out.println("TimeZone.getDefault() = " + TimeZone.getDefault());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        // 注意在做Date和String转化过程中，一定要注意时区问题，看看是否需要使用零时区
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println("simpleDateFormat.format(new Date()) = " + simpleDateFormat.format(new Date()));
    }
}

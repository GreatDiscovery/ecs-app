package com.gavin.app.convert;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateTimeConverter;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author QiangZhi
 * @date 2022/11/28 11:09 下午
 */
public class DateConvertTest {
    public static void main(String[] args) throws Exception {
        Person1 person1 = new Person1();
        person1.setBirthday(new Date(System.currentTimeMillis()));
        person1.setName("gavin");
        System.out.println("JSONObject.toJSONString(person1) = " + JSONObject.toJSONString(person1));

        DateTimeConverter dateTimeConverter = new DateTimeConverter() {
            @Override
            protected Class getDefaultType() {
                return Date.class;
            }
        };
        dateTimeConverter.setTimeZone(TimeZone.getTimeZone(ZoneId.SHORT_IDS.get("CTT")));
        dateTimeConverter.setPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateTimeConverter.setTimeZone(TimeZone.getTimeZone("GMT"));
        ConvertUtils.register(dateTimeConverter, String.class);
        Person2 person2 = new Person2();
        BeanUtils.copyProperties(person2, person1);
        System.out.println(JSONObject.toJSONString(person2));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println("simpleDateFormat.format(new Date()) = " + simpleDateFormat.format(new Date()));
    }
}




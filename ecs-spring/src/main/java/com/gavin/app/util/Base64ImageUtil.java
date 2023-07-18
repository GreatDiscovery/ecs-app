package com.gavin.app.util;

import org.apache.tomcat.util.codec.binary.Base64;

/**
 * @author QiangZhi
 * @date 2023/3/5 13:16
 */
public class Base64ImageUtil {
    /**
     * 二进制流转Base64字符串
     * <p>Title: byteArr2String</p>
     * @author Liyan
     * @date   2017年3月6日 下午3:04:47
     * @return String
     */
    public static String byteArr2String(byte[] byteArr) throws Exception {
        String stringBase64 = null;
        try {
            Base64 encoder = new Base64();
            stringBase64 =(byteArr != null ? encoder.encodeToString(byteArr) : "");
        } catch (Exception e) {
            throw new Exception("byteArr2String转换异常:"+e);
        }
        return stringBase64;
    }
}

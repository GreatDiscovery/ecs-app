package com.gavin.app.proxy;

/**
 * 修改Class文件，只提供修改常量池功能
 *
 * @author gavin
 * @date 2020/11/8 9:20 下午
 */
public class ClassModifier {
    /**
     * 常量池的起始偏移
     */
    public static final int CONSTANT_POOL_COUNT_INDEX = 8;
    /**
     * utf8_info的tag标志
     */
    public static final int CONSTANT_UTF8_INFO = 1;

    /**
     * 常量池11中常量所占的宽度
     */
    public static final int[] CONSTANT_ITEM_LENGTH = {-1, -1, -1, 5, 5, 9, 9, 3, 3, 5, 5, 5, 5};

    public static final int u1 = 1;
    public static final int u2 = 2;

    private byte[] classByte;

    public ClassModifier(byte[] classByte) {
        this.classByte = classByte;
    }

    /**
     * 修改常量池utf8_info的内容
     * @param oldStr
     * @param newStr
     * @return
     */
    public byte[] modifyURF8Constant(String oldStr, String newStr) {
        return null;
    }
}

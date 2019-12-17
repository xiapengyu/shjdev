/**
 * Copyright 2017-2025 Evergrande Group.
 */
package com.yunjian.modules.automat.util;

/**
 * base64编码工具类
 *
 * @author gucunyang
 * @since 2018-02-08
 */
public class Base64Utils {

    private Base64Utils() {
    }

    /**
     * 将base64编码字符串解码为字节数组
     *
     * @param base64
     * @return
     */
    public static byte[] decode(String base64) {
        return Base64.decode(base64.toCharArray());
    }

    /**
     * 将字节数组编码为base64字符串
     *
     * @param bytes
     * @return
     */
    public static String encode(byte[] bytes) {
        return new String(Base64.encode(bytes));
    }
}

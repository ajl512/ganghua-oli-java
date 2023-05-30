package com.gangyunshihua.utils;

import java.security.MessageDigest;
import java.util.Random;

public class StringUtil {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     */
    public static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++)
            sb.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(new Random().nextInt(62)));
        return sb.toString();
    }

    /**
     * md5加密
     */
    public static String getMD5(String string) throws Exception {
        return encode("md5", string);
    }

    private static String encode(String algorithm, String value) throws Exception {
        if (value == null) return null;
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.update(value.getBytes());
        return getFormattedText(messageDigest.digest());
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
}
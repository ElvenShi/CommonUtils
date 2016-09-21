package com.syz.example.utils;

import java.util.Locale;

/**
 * Created by SYZ on 16/9/21.
 * 进制转换工具
 */
public class HexUtils {

    /**
     * 将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase(Locale.CHINA);
//            String hv = Integer.toHexString(v).toLowerCase(Locale.CHINA);
            if (hv.length() == 1) {
                // 此加0目的配合下的getMessure固定取值2
                stringBuilder.append("0").append(hv);
            } else {
                stringBuilder.append(hv);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * int类型的数组转换成十六进制
     * @param src
     * @return
     */
    public static String int2HexString(int[] src) {
        if(src == null || src.length == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i : src) {
            String hv = Integer.toHexString(i);
            if (hv.length() == 1) {
                // 此加0目的配合下的getMessure固定取值2
                stringBuilder.append("0").append(hv);
            } else {
                stringBuilder.append(hv);
            }
        }
        //转大写String
        return stringBuilder.toString().toUpperCase(Locale.getDefault());
        //转小写String
//        return stringBuilder.toString().toLowerCase(Locale.getDefault());
    }

    /**
     * 16进制转换为10进制
     *
     * @param hexStr
     * @return
     */
    private static Integer hexToInt(String hexStr) {
        int int_ch = 0; // / 两位16进制数转化后的10进制数
        for (int i = 0; i < hexStr.length(); i++) {
            char hex_char1 = hexStr.charAt(i); // //两位16进制数中的第一位(高位*16)
            int int_ch1;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch1 = (hex_char1 - 48) * 16; // // 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch1 = (hex_char1 - 55) * 16; // // A 的Ascll - 65
            else
                int_ch1 = (hex_char1 - 87) * 16; // // a 的Ascll - 97
            i++;

            char hex_char2 = hexStr.charAt(i); // /两位16进制数中的第二位(低位)
            int int_ch2;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch2 = (hex_char2 - 48); // // 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch2 = hex_char2 - 55; // // A 的Ascll - 65
            else
                int_ch2 = hex_char2 - 87; // // a 的Ascll - 97

            int_ch = int_ch1 + int_ch2;
        }
        return int_ch;
    }

    /**
     * 16进制字符串转成int值
     */
    public static int hexStr2Int(String hex) {
        int result = 0;
        for (char c : hex.toCharArray()) {
            result = result * 16 + Character.digit(c, 16);
        }
        return result;
    }
}

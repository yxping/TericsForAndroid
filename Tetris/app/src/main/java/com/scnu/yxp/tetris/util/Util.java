package com.scnu.yxp.tetris.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yxp on 2015/10/23.
 */
public class Util {

    public static byte[] intToByteArray(int n){
        byte[] array = new byte[4];
        for (int i = 0; i < 4; i++) {
            array[i] = (byte) (n >> 8 * i & 0xFF);
        }
        return array;
    }

    public static int byteArrayToInt(byte[] array) {
        int result = 0;
        byte bLoop;
        for (int i = 0; i < array.length; i++) {
            bLoop = array[i];
            result += (bLoop & 0xFF) << (8 * i);
        }
        return result;
    }

    /**
     * 判断是否为合法IP
     * @return the ip
     */
    public static boolean isIp(String ipAddress)
    {
        String ip = "([1-9]|[1-9]//d|1//d{2}|2[0-4]//d|25[0-5])(//.(//d|[1-9]//d|1//d{2}|2[0-4]//d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }
}

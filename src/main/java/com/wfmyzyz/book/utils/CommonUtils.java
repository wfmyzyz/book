package com.wfmyzyz.book.utils;

import java.util.concurrent.ThreadLocalRandom;

public class CommonUtils {
    /**
     * 生成指定范围内的随机整数
     * @param min
     * @param max
     * @return
     */
    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * 字符数字转换成int，例如01 -> 1
     * @param string
     * @return
     */
    public static Integer stringToNum(String string) {
        int num = 0;
        if(string.startsWith("0")) {
            string = string.substring(1);
        }
        num = Integer.parseInt(string);
        return num;
    }
}

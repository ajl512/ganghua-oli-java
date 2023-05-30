package com.gangyunshihua.utils;

import java.util.List;

public class ListUtil {

    public static boolean judgeContain(Integer[] array, Integer judgeInteger) {
        for (Integer integer : array) if (integer.intValue() == judgeInteger) return true;
        return false;
    }

    public static boolean judgeContain(String[] array, String judgeString) {
        for (String string : array) if (string.equals(judgeString)) return true;
        return false;
    }

    public static boolean judgeContain(List<Integer> list, Integer judgeInteger) {
        for (Integer integer : list) if (integer.intValue() == judgeInteger) return true;
        return false;
    }

    public static boolean judgeContain(List<String> list, String judgeString) {
        for (String string : list) if (string.equals(judgeString)) return true;
        return false;
    }
}

package com.tk.lyin.hiprint.utils.collect;

import com.tk.lyin.hiprint.utils.string.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ArrayUtils {

    public static Integer[] string2IntArray(String str) {
        if (StringUtils.isNotEmpty(str)) {
            String[] strArr = str.split("\\D+");
            Integer[] data = new Integer[strArr.length];

            for (int i = 0; i < strArr.length; ++i) {
                data[i] = Integer.parseInt(strArr[i]);
            }

            return data;
        } else {
            throw new RuntimeException("要被转换成数组的字符串不能为空!");
        }
    }

    public static String[] listToArray(List<String> data) {
        if (CollectionUtils.isNotEmpty(data)) {
            int length = data.size();
            String[] t = new String[length];

            for (int i = 0; i < data.size(); ++i) {
                t[i] = data.get(i);
            }

            return t;
        } else {
            return null;
        }
    }

    public static String[] setToArray(Set<String> data) {
        if (CollectionUtils.isNotEmpty(data)) {
            String[] t = new String[data.size()];
            data.toArray(t);
            return t;
        } else {
            return null;
        }
    }

    public static Integer[] listToIntArray(List<Integer> data) {
        if (CollectionUtils.isNotEmpty(data)) {
            int length = data.size();
            Integer[] t = new Integer[length];

            for (int i = 0; i < data.size(); ++i) {
                t[i] = data.get(i);
            }

            return t;
        } else {
            return null;
        }
    }

    public static <T> List<T> arrayToList(T[] array) {
        List<T> list = new ArrayList<>();
        if (isNotEmpty(array)) {
            Collections.addAll(list, array);
        }
        return list;
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }
}

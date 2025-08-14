//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tk.lyin.hiprint.utils.string;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

public class StringUtils {
    public StringUtils() {
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean hasEmpty(String... str) {
        if (str != null && str.length != 0) {
            String[] var1 = str;
            int var2 = str.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                String foo = var1[var3];
                if (isEmpty(foo)) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public static String emptyDefault(String str, String defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    public static void emptyDo(String str, Consumer<String> consumer) {
        if (!isEmpty(str)) {
            consumer.accept((String) null);
        }
    }

    public static boolean hasText(String... str) {
        if (str != null && str.length != 0) {
            String[] var1 = str;
            int var2 = str.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                String s = var1[var3];
                if (isNotEmpty(s)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static boolean include(String src, String... arrays) {
        if (arrays != null && arrays.length != 0) {
            String[] var2 = arrays;
            int var3 = arrays.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                String arr = var2[var4];
                boolean isEq = equals(src, arr);
                if (isEq) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static boolean exclude(String src, String... arrays) {
        return !include(src, arrays);
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        } else {
            return str1 != null && str2 != null && str1.equals(str2);
        }
    }

    public static boolean notEquals(String str1, String str2) {
        return !equals(str1, str2);
    }

    public static String byteToHexStr(byte mByte) {
        char[] Digit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[]{Digit[mByte >>> 4 & 15], Digit[mByte & 15]};
        return new String(tempArr);
    }

    public static String byteToStr(byte[] byteArray) {
        StringBuilder buffer = new StringBuilder();
        byte[] var2 = byteArray;
        int var3 = byteArray.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            byte aByteArray = var2[var4];
            buffer.append(byteToHexStr(aByteArray));
        }

        return buffer.toString();
    }

    public static String decodeByUTF8(String str) {
        if (isEmpty(str)) {
            return str;
        } else {
            try {
                return URLDecoder.decode(URLDecoder.decode(str, "utf-8"), "utf-8");
            } catch (UnsupportedEncodingException var2) {
                var2.printStackTrace();
                throw new RuntimeException("无法解析的字符串或错误的编码!" + str);
            }
        }
    }

    public static String encodeByUTF8(String str) {
        if (isEmpty(str)) {
            return null;
        } else {
            try {
                return new String(str.getBytes("iso-8859-1"), "utf-8");
            } catch (UnsupportedEncodingException var2) {
                var2.printStackTrace();
                throw new RuntimeException("无法解析的字符串或错误的编码!" + str);
            }
        }
    }

    public static int getStringLength(String str) {
        int length = 0;
        byte[] bytes = null;
        char[] var3 = str.toCharArray();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            char cn = var3[var5];

            try {
                bytes = String.valueOf(cn).getBytes("GBK");
            } catch (UnsupportedEncodingException var8) {
            }

            if (bytes != null && bytes.length <= 2 && bytes.length > 0) {
                if (bytes.length == 1) {
                    ++length;
                } else if (bytes.length == 2) {
                    length += 2;
                }
            } else {
                length += 2;
            }
        }

        return length;
    }

    public static String join(String[] value, String split) {
        if (value != null && value.length >= 1) {
            StringBuilder builder = new StringBuilder();
            String[] var3 = value;
            int var4 = value.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String v = var3[var5];
                builder.append(split).append(v);
            }

            return builder.substring(split.length());
        } else {
            return "";
        }
    }

    public static String join(Collection<String> value, String split) {
        if (value != null && !value.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            Iterator var3 = value.iterator();

            while (var3.hasNext()) {
                String v = (String) var3.next();
                builder.append(split).append(v);
            }

            return builder.substring(split.length());
        } else {
            return "";
        }
    }


    public static String subLast(String str, String prefix) {
        if (hasEmpty(str, prefix)) {
            return str;
        } else {
            int index = str.lastIndexOf(prefix);
            return index == -1 ? str : str.substring(index + 1);
        }
    }
}

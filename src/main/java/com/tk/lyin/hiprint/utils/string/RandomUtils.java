//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tk.lyin.hiprint.utils.string;

import java.util.Random;

import org.springframework.util.Assert;

public class RandomUtils {
    private static final char[] chars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static String generate(int length) {
        Assert.isTrue(length > 0 && length < 100, "长度只能是0-99之间!");
        StringBuilder v = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < length; ++i) {
            v.append(chars[random.nextInt(62)]);
        }
        return v.toString();
    }

    public static String number(int length) {
        Assert.isTrue(length > 0 && length < 33, "长度只能是1-32之间!");
        StringBuilder v = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < length; ++i) {
            v.append(random.nextInt(10));
        }
        return v.toString();
    }
}

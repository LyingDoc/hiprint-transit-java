//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tk.lyin.hiprint.utils.gson.converter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.springframework.util.Assert;

import java.io.IOException;

public class IntegerConverter extends TypeAdapter<Integer> {
    public IntegerConverter() {
    }

    public void write(JsonWriter out, Integer value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value);
        }
    }

    public Integer read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            String str = in.nextString();
            if (str != null && !str.trim().equals("")) {
                Assert.isTrue(str.matches("^-?\\d+$"), "数据格式错误！非法的数字格式！" + str);
                return Integer.parseInt(str);
            } else {
                return null;
            }
        }
    }
}

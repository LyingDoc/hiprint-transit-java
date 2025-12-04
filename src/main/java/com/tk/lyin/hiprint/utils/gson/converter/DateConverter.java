//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tk.lyin.hiprint.utils.gson.converter;


import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.tk.lyin.hiprint.utils.date.DateUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter extends TypeAdapter<Date> {
    private String pattern = "yyyy-MM-dd HH:mm:ss";


    public DateConverter() {
    }

    public DateConverter(String pattern) {
        if (pattern != null) {
            this.pattern = pattern;
        }

    }

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.getTime());
        }
    }


    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            String json = in.nextString();
            if (json.matches("-?\\d+")) {
                return new Date(Long.parseLong(json));
            } else {
                if (json.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
                    this.pattern = "yyyy-MM-dd HH:mm:ss";
                } else if (json.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}")) {
                    this.pattern = "yyyy-MM-dd HH:mm";
                } else if (json.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
                    this.pattern = "yyyy-MM-dd";
                } else if (json.matches("\\d{2}:\\d{2}:\\d{2}")) {
                    this.pattern = "HH:mm:ss";
                } else if (json.matches("\\d{2}:\\d{2}")) {
                    this.pattern = "HH:mm";
                }
                Date date = null;
                try {
                    date = (new SimpleDateFormat(this.pattern)).parse(json);
                } catch (ParseException var5) {
                    date = DateUtils.parse(json);
                }

                return date;
            }
        }
    }
}

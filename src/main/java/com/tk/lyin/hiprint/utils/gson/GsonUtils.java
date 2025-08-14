package com.tk.lyin.hiprint.utils.gson;


import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tk.lyin.hiprint.utils.gson.converter.DateConverter;
import com.tk.lyin.hiprint.utils.gson.converter.DoubleConverter;
import com.tk.lyin.hiprint.utils.gson.converter.FloatConverter;
import com.tk.lyin.hiprint.utils.gson.converter.IntegerConverter;
import com.tk.lyin.hiprint.utils.io.IOUtils;
import com.tk.lyin.hiprint.utils.string.StringUtils;


import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;

public class GsonUtils {

    private static final Gson COMMON_GSON = createGson((String[]) null, (String[]) null);

    private static Gson createGson(String[] inclusionFields, String[] exclusionFields) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateConverter());
        builder.registerTypeAdapter(java.sql.Date.class, new DateConverter());
        builder.registerTypeAdapter(Integer.class, new IntegerConverter());
        builder.registerTypeAdapter(Float.class, new FloatConverter());
        builder.registerTypeAdapter(Double.class, new DoubleConverter());
        builder.serializeNulls();


        if (exclusionFields != null && exclusionFields.length > 0) {
            GsonExclusion gsonFilter = new GsonExclusion();
            gsonFilter.addExclusionField(exclusionFields);
            builder.setExclusionStrategies(new ExclusionStrategy[]{gsonFilter});
        }

        if (inclusionFields != null && inclusionFields.length > 0) {
            GsonInclusion gsonFilter = new GsonInclusion();
            gsonFilter.addInclusionFields(inclusionFields);
            builder.setExclusionStrategies(new ExclusionStrategy[]{gsonFilter});
        }

        return builder.create();
    }

    public static String toJson(Object obj) {
        return COMMON_GSON.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return COMMON_GSON.fromJson(json, clazz);
    }


    public static <T> T wrapDataToEntity(HttpServletRequest request, Class<T> clazz, String... excludeFields) {
        if (request != null && clazz != null) {
            Gson gson = COMMON_GSON;
            if (excludeFields != null && excludeFields.length > 0) {
                gson = createGson((String[]) null, excludeFields);
            }

            String data = null;

            try {
                ServletInputStream inputStream = request.getInputStream();
                if (inputStream != null) {
                    data = IOUtils.toString(inputStream, "utf-8");
                    if (StringUtils.isNotEmpty(data)) {
                        request.setAttribute("_CachedInputStreamData", data.getBytes("utf-8"));
                    }
                }

                if ((inputStream == null || StringUtils.isEmpty(data)) && request.getAttribute("_CachedInputStreamData") == null) {
                    Enumeration<String> enumeration = request.getParameterNames();
                    JsonObject jsonObject = new JsonObject();

                    while (true) {
                        String key;
                        do {
                            if (!enumeration.hasMoreElements()) {
                                return gson.fromJson(jsonObject, clazz);
                            }

                            key = (String) enumeration.nextElement();
                        } while (excludeFields != null && Arrays.binarySearch(excludeFields, key) != -1);

                        String value = request.getParameter(key);
                        if (StringUtils.isNotEmpty(value)) {
                            jsonObject.addProperty(key, StringUtils.decodeByUTF8(value));
                        }
                    }
                }

                if (StringUtils.isEmpty(data)) {
                    Object cachedData = request.getAttribute("_CachedInputStreamData");
                    if (cachedData != null) {
                        data = IOUtils.toString((byte[]) ((byte[]) cachedData), "utf-8");
                    }
                }
            } catch (IOException var10) {
                var10.printStackTrace();
            }

            if (data == null) {
                return null;
            } else {
                data = data.replaceAll("(:\\[\\])|(:\\{\\})", ":null");
                return gson.fromJson(data, clazz);
            }
        } else {
            throw new InvalidParameterException("参数不能为空！");
        }
    }


}

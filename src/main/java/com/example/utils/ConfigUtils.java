package com.example.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ConfigUtils {

    private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    private static final String url = "config";
    private static Map<String, String> config = new LinkedHashMap<>();

    public static void load() {
        ResourceBundle resource = ResourceBundle.getBundle(url);
        for (String key: resource.keySet()) {
            try {
                config.put(key, new String(resource.getString(key).getBytes("ISO-8859-1"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getStr(String key) {
        return config.get(key);
    }

    public static String[] getArr(String key) {
        return config.get(key).split(",");
    }

    public static String formatDate(Object date) {
        return sf.format(date);
    }

    public static void main(String[] args) {
        load();
    }
}

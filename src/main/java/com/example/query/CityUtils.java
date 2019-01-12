package com.example.query;

import com.example.env.URLConfig;
import com.example.utils.HttpUtils;
import net.dongliu.requests.RawResponse;

import java.util.HashMap;
import java.util.Map;

public class CityUtils {

    static Map<String, String> citys = new HashMap<>();

    public static String code2Name(String code) {

        return citys.get(code);
    }

    public static String name2Code(String name) {
        for(Map.Entry<String, String> en: citys.entrySet()) {
            if(en.getValue().equals(name)) {
                return en.getKey();
            }
        }
        return null;
    }

    public static void load() {
        Map<String, String> params = new HashMap<>();
        params.put("station_version", "1.9040");
        RawResponse resp = HttpUtils.send(URLConfig.CITY, params);
        String[] temp = resp.readToText().split("@");
        for(int i = 1; i < temp.length; i++) {
            String[] t = temp[i].split("\\|");
            citys.put(t[2], t[1]);
        }
    }

    public static void main(String[] args) {
        load();
        load();
        System.out.println(name2Code("绩溪县"));
        System.out.println(code2Name("VAP"));

    }

}

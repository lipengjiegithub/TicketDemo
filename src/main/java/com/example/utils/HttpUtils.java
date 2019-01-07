package com.example.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.example.env.URLConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpUtils {

    private static Log log = LogFactory.getLog(HttpUtils.class);
    private static Connection conn = Jsoup.connect(URLConfig.INIT);

    static {
        conn = conn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0")
                   .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                   .validateTLSCertificates(false)
                   .followRedirects(false)
                   .ignoreContentType(true);
    }

    public static Connection.Response send(String url, Map<String, String> params){

        if(params == null) params = new LinkedHashMap<>();

        Connection.Response resp = null;
        try {
//            if (url == URLConfig.GET_CODE) {
//                conn = conn.header("Content-Type", "image/jpeg");
//            }else {
//                conn = conn.header("Content-Type", "application/json;charset=UTF-8");
//            }
            resp = conn.url(url).data(params).execute();
            conn = conn.cookies(resp.cookies());
        } catch (IOException e) {
            e.printStackTrace();
            log.error(String.format("请求%s异常",url));
        }

        return resp;
    }

    public static JSONObject resp2JsonObj(Connection.Response resp) {
        return JSONObject.parseObject(resp.header("Content-Type", "application/json;charset=UTF-8").body());
    }


    public static JSONArray resp2JsonArr(Connection.Response resp) {
        return JSONArray.parseArray(resp.header("Content-Type", "application/json;charset=UTF-8").body());
    }

    public static void showImage(InputStream in) throws Exception{
        JLabel image = new JLabel(new ImageIcon(ImageIO.read(in)));
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(image);

        JFrame frame = new JFrame();
        frame.setSize(300, 230);
        frame.add(mainPanel);
        frame.setVisible(true);
    }


    public static String decodeStr(String str) {
        try {
            return URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("解码失败", e);
            return "";
        }
    }

}

package com.example.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.env.ReqInfo;
import com.example.env.URLConfig;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.RequestBuilder;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpUtils {

    private static Log log = LogFactory.getLog(HttpUtils.class);
    private static Session session = Requests.session();
    private static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.put("Content-Length", "0");
    }

    public static RawResponse send(ReqInfo info, Map<String, String> params){

        if(params == null) params = new LinkedHashMap<>();

        RawResponse response = null;

        try {
            RequestBuilder builder = null;
            switch (info.method) {
                case "GET":
                    builder = session.get(info.url);
                    if (params != null) builder.params(params);
                    break;
                case "POST":
                    builder = session.post(info.url);
                    if (params != null) builder.body(params);
                    break;
            }

            response = builder
                    .acceptCompress(false)
                    .charset(Charset.forName("UTF-8"))
                    .followRedirect(false)
                    .verify(false)
                    .requestCharset(Charset.forName("UTF-8"))
                    .keepAlive(true)
                    .send();

            if(response.statusCode() == 302) {
                String loaction = response.getHeader("Location");
                info.url = loaction;
                send(info, params);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(String.format("请求%s异常",info.url));
        }
        return response;
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

    public static void main(String[] args) {
        RawResponse resp = HttpUtils.send(URLConfig.CITY, null);
        System.out.println(resp.readToText());
    }

}

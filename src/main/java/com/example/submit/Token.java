package com.example.submit;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.OrderInfo;
import com.example.env.URLConfig;
import com.example.utils.HttpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Token {

    private static Log log = LogFactory.getLog(Token.class);
    private String html = "";

    public Token() {
        log.debug("获取Token页面");
        try {
//            this.html = testHTML();
            this.html = HttpUtils.send(URLConfig.DC, null).parse().html();
            this.html = HttpUtils.send(URLConfig.DC, null).parse().html();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取Token页面异常", e);
        }
        log.debug("Token页面加载完毕");
    }

    public String getRepeatToken() {
        String reg = "var +globalRepeatSubmitToken ?= ?'(.*)'";
        return reg(reg);
    }

    public OrderInfo getTicketInfo() {
        String reg = "var +ticketInfoForPassengerForm ?= ?(.*);";
        return JSONObject.parseObject(reg(reg), OrderInfo.class);
    }

    private String reg(String reg) {
        try {
            Matcher matcher = Pattern.compile(reg).matcher(this.html);
            if(matcher.find() && matcher.groupCount() > 0) {
                return matcher.group(1);
            };
        } catch (Exception e) {
            e.printStackTrace();
            log.error(String.format("解析正则%s异常", reg), e);
            return "";
        }
        return "";
    }


    public String testHTML() throws Exception{
        File file = new File("/Users/tony/workspace/HTML/token.html");

        InputStream in = new FileInputStream(file);
        StringBuffer sb = new StringBuffer();
        byte[] b = new byte[1024];
        while(in.read(b) > 0) {
            sb.append(new String(new String(b, "gbk")));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception{
        Token token = new Token();
        String result = token.getRepeatToken();
        System.out.println(result);
        OrderInfo orderInfo = token.getTicketInfo();
        System.out.println(orderInfo);


    }
}

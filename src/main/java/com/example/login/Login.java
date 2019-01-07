package com.example.login;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.User;
import com.example.env.URLConfig;
import com.example.login.captcha.Captcha;
import com.example.utils.Assert;
import com.example.utils.HttpUtils;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.Map;

public class Login {

    private String username;
    private String password;
    private Captcha captcha = new Captcha();

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * 用户登录
     * @return flag
     */
    public boolean login() {
        boolean flag = captcha.get().answer().check();
        return Assert.flag(flag, "登录成功!", "登录失败!");
    }

    /**
     * 用户初始化
     * @return 用户信息
     */
    public User init() {
        Map<String, String> params = new HashMap<String, String>();
        Connection.Response resp = HttpUtils.send(URLConfig.INIT, params);
        JSONObject result = HttpUtils.resp2JsonObj(resp);
        boolean flag = result.getBoolean("status");
        flag = Assert.flag(flag, "初始化成功!", "初始化失败!");
        User user = new User();
        if(flag) {
            user = result.getJSONObject("data").toJavaObject(User.class);
        }
        return user;
    }

    public void myOrder() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("_json_att", "");
        Connection.Response resp = HttpUtils.send(URLConfig.MY_ORDER, params);
        JSONObject obj = HttpUtils.resp2JsonObj(resp);

        boolean status = obj.getBoolean("status");
        String[] message = (String[])obj.get("messages");
        JSONObject data = obj.getJSONObject("data");
    }
}

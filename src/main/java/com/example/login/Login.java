package com.example.login;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.User;
import com.example.env.URLConfig;
import com.example.login.captcha.Captcha;
import com.example.utils.Assert;
import com.example.utils.HttpUtils;
import com.example.utils.TrainUtils;
import net.dongliu.requests.RawResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Login {

    private static Log log = LogFactory.getLog(Login.class);

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
        Captcha.Result result = captcha.get().answer().check();
        Assert.flag(result.success, "验证码识别成功!", "验证码识别失败!");
        if(!result.success) return false;
        Map<String, String> params = new LinkedHashMap<>();

        params.put("username", this.username);
        params.put("password", this.password);
        params.put("appid", "otn");
        params.put("answer", result.position);
        RawResponse resp = HttpUtils.send(URLConfig.LOGIN, params);
        JSONObject obj = JSONObject.parseObject(resp.readToText());


        if(obj.getIntValue("result_code") == 0) {
            Login.verify();

            return true;
        }else {
            log.info(String.format("登录失败,信息:%s", obj.getString("messages")));
            return false;
        }
    }

    /**
     * 验证
     */
    public static void verify() {
        Map<String, String> params = new LinkedHashMap<>();
        // 验证
        params.put("appid", "otn");
        RawResponse resp = HttpUtils.send(URLConfig.UAMTK, params);
        JSONObject obj = JSONObject.parseObject(resp.readToText());
        // {"result_message":"验证通过","result_code":0,"apptk":null,"newapptk":"t3c0lZ6ehFihHJjfX8Ekxh_xPwBUrXlBwPzpLYEaOyo641210"}


        String tk = obj.getString("newapptk");

        params.clear();
        params.put("tk", tk);
        resp = HttpUtils.send(URLConfig.UAMAUTH_CLIENT, params);
        obj = JSONObject.parseObject(resp.readToText());
        // {"apptk":"V32PLPcw1pccUfH6OBF2BkqCAeB5EViwNFxg5zheJ4Yaf1210","result_message":"验证通过","result_code":0,"username":"李鹏杰"}

        log.info(String.format("%s,欢迎:%s", obj.getString("result_message"), obj.getString("username")));
        resp = HttpUtils.send(URLConfig.INIT_API, null);
    }

    /**
     * 用户初始化
     * @return 用户信息
     */
    public User init() {
        RawResponse resp = HttpUtils.send(URLConfig.INIT, null);
        JSONObject result = JSONObject.parseObject(resp.readToText());
        System.out.println(result.toJSONString());
        boolean flag = result.getBoolean("status");
        flag = Assert.flag(flag, "初始化成功!", "初始化失败!");
        User user = new User();
        if(flag) {
            user = result.getJSONObject("data").toJavaObject(User.class);
        }
        return user;
    }

    public static JSONObject myOrder() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("_json_att", "");
        RawResponse resp = HttpUtils.send(URLConfig.MY_ORDER, params);
        JSONObject obj = TrainUtils.parse(resp);
        if(obj != null&& !obj.isEmpty()) {
            log.info(obj);
        }else {
            log.info("当前没有未支付订单");
        }
        return obj;
    }
}

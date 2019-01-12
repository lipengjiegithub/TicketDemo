package com.example.login.captcha;

import com.alibaba.fastjson.JSONObject;
import com.example.env.URLConfig;
import com.example.utils.HttpUtils;
import net.dongliu.requests.RawResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;

import java.util.HashMap;
import java.util.Map;

public class Captcha {

    Log log = LogFactory.getLog(Captcha.class);
    byte[] data = null;
    String answer = null;

    public Captcha get() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("login_site", "E");
        params.put("module", "login");
        params.put("rand", "sjrand");
        RawResponse resp = HttpUtils.send(URLConfig.GET_CODE, params);
        this.data = resp.readToBytes();
        return this;
    }

    public Captcha answer() {
        new AutoRecognize().recognize(this.data);
        this.answer = new ManualRecognize().recognize(this.data);
        return this;
    }

    public Result check() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("answer", this.answer);
        params.put("login_site", "E");
        params.put("rand", "sjrand");
        RawResponse resp = HttpUtils.send(URLConfig.CHECK_CODE, params);
        JSONObject result = JSONObject.parseObject(resp.readToText());
        log.error(result.get("result_message"));
        return new Result("4".equals(result.get("result_code")), this.answer);
    }

    public static class Result {
        public boolean success;
        public String position;

        public Result(boolean success, String position) {
            this.success = success;
            this.position = position;
        }
    }

}

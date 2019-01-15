package com.example.login.captcha;

import com.alibaba.fastjson.JSONObject;
import com.mchange.v3.decode.Decoder;
import net.dongliu.commons.Hexes;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipCompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;


// 自动识别
public class AutoRecognize implements Recognize {

    private static Log log = LogFactory.getLog(AutoRecognize.class);

    private static Map<String, String> headers = new HashMap<>();

    private static Session session = Requests.session();

    static {
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.put("Referer", "http://60.205.200.159/");
    }

    @Override
    public String recognize(byte[] data) {

        String base64 = new BASE64Encoder().encode(data);
        Map<String, String> params = new HashMap<>();
        params.put("base64", base64);

        log.debug("正在调用自动识别");

        RawResponse resp = session.post("http://60.205.200.159/api").jsonBody(params).send();
//
        JSONObject result = JSONObject.parseObject(resp.readToText());
        String check = "";
        if(result.getBoolean("success")&&result.getString("check") != null) {
            check = result.getString("check");
        }

        params.clear();
        params.put("=", "");
        params.put("check", check);
        params.put("img_buf", URLEncoder.encode(base64));
        params.put("logon", "1");
        params.put("type", "D");


        headers.clear();
        headers.put("Content-Type", "text/plain");
        resp = session.post("http://check.huochepiao.360.cn/img_vcode").body(params).headers(headers).send();
        log.debug(resp.readToText());

//        JSONObject obj = JSONObject.parseObject(resp.readToText());

//        resp = requests.session().post('http://60.205.200.159/api',
//                headers={'Content-Type': 'application/json'},
//        data=dumps(data))
//        print('调用自动识别成功')
//        resp = loads(resp.content)
//        if resp['success']:
//        check_data = {
//                '=': '""',
//                'check': resp['check'],
//                'img_buf': data['base64'],
//                'logon': 1,
//                'type': 'D'
//
//        }
//        print('正在获取坐标')
//        resp = requests.session().post('http://check.huochepiao.360.cn/img_vcode',
//                headers={'Content-Type': 'text/plain'},
//        data=dumps(check_data))
//        print('坐标获取成功',resp.content)
//        answer = [];
//        position = []
//        # print('数据',resp.content)
//        temp = loads(resp.content)['res'].replace('(', '').replace(')', '').split(',')
//        i = 0
//        while i < len(temp):
//        answer.append(int(temp[i]) // 70 + int(temp[i+1]) // 70 * 4)
//        i += 2
//        print('识别%s'%answer)
//        for pos in answer:
//        position.append(center_position[int(pos)])
//        # 正确验证码的坐标拼接成字符串，作为网络请求时的参数
//        # auto_choose(position, code_base64)
//        return ','.join(position)



        StringBuffer position = new StringBuffer();
        return position.toString();
    }
}

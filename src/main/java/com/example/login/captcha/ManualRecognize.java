package com.example.login.captcha;

import com.example.utils.HttpUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

// 手动识别
public class ManualRecognize implements Recognize {

    @Override
    public String recognize(byte[] data) {

        InputStream inputStream = new ByteArrayInputStream(data);
        show(inputStream);

        String[] question = verify();
        StringBuffer position = new StringBuffer();
        for (int i = 0; i < question.length; i++) {
            position.append(CENTER_POSITION[Integer.parseInt(question[i])]);
            if(i != question.length - 1) {
                position.append(",");
            }
        }
        return position.toString();
    }

    private String[] verify() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入坐标,用,分割:");
        String in = scanner.next();
        String[] position = in.split(",");
        return position;
    }

    private void show(InputStream in) {
        try {
            HttpUtils.showImage(in);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("验证码显示异常");
        }
    }
}

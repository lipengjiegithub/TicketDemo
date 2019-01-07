package com.example.login.captcha;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public interface Recognize {

    // 由于12306官方验证码是验证正确验证码的坐标范围,我们取每个验证码中点的坐标(大约值)
    final static String[] CENTER_POSITION = {"35,35", "105,35", "175,35", "245,35", "35,105", "105,105", "175,105", "245,105"};

    Log log = LogFactory.getLog(Recognize.class);

    String recognize(byte[] data);

}

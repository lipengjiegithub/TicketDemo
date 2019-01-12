package com.example.env;


public class URLConfig {

    public final static ReqInfo
            GET_CODE = new ReqInfo("https://kyfw.12306.cn/passport/captcha/captcha-image", "GET", ""),

    CITY = new ReqInfo("https://kyfw.12306.cn/otn/resources/js/framework/station_name.js", "GET", ""),
    ORDER_INFO = new ReqInfo("https://kyfw.12306.cn/otn/confirmPassenger/initDc", "GET", ""),
    QUERY = new ReqInfo("https://kyfw.12306.cn/otn/leftTicket/queryZ", "GET", "https://kyfw.12306.cn/otn/leftTicket/init"),

    CHECK_CODE = new ReqInfo("https://kyfw.12306.cn/passport/captcha/captcha-check", "POST", "https://kyfw.12306.cn/otn/leftTicket/init"),

    LOGIN = new ReqInfo("https://kyfw.12306.cn/passport/web/login", "POST", "https://kyfw.12306.cn/otn/resources/login.html"),

    UAMTK = new ReqInfo("https://kyfw.12306.cn/passport/web/auth/uamtk", "POST", "https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin"),


    UAMAUTH_CLIENT = new ReqInfo("https://kyfw.12306.cn/otn/uamauthclient", "POST", "https://kyfw.12306.cn/otn/passport?redirect=/otn/login/userLogin"),

    //  https://kyfw.12306.cn/otn/login/conf

    INIT_API = new ReqInfo("https://kyfw.12306.cn/otn/index/initMy12306Api", "POST", "https://kyfw.12306.cn/otn/view/index.html"),


    LOGINOUT = new ReqInfo("https://kyfw.12306.cn/otn/login/loginOut", "POST", "https://kyfw.12306.cn/otn/leftTicket/init"),

    CHECK_USER = new ReqInfo("https://kyfw.12306.cn/otn/login/checkUser", "POST", ""),
    INIT = new ReqInfo("https://kyfw.12306.cn/otn/index/initMy12306Api", "GET", ""),


    SUBMIT_ORDER = new ReqInfo("https://kyfw.12306.cn/otn/leftTicket/submitOrderRequest", "POST", "https://kyfw.12306.cn/otn/leftTicket/init"),
    PASSENGERS = new ReqInfo("https://kyfw.12306.cn/otn/confirmPassenger/getPassengerDTOs", "POST", ""),
    CHECK_ORDER = new ReqInfo("https://kyfw.12306.cn/otn/confirmPassenger/checkOrderInfo", "POST", "https://kyfw.12306.cn/otn/confirmPassenger/initDc"),

    GET_QUEUE_COUNT = new ReqInfo("https://kyfw.12306.cn/otn/confirmPassenger/getQueueCount", "POST", "https://kyfw.12306.cn/otn/confirmPassenger/initDc"),

    CONFIRM_FOR_QUEUE = new ReqInfo("https://kyfw.12306.cn/otn/confirmPassenger/confirmSingleForQueue", "POST", "https://kyfw.12306.cn/otn/confirmPassenger/initDc"),

    ORDER_WAIT_TIME = new ReqInfo("https://kyfw.12306.cn/otn/confirmPassenger/queryOrderWaitTime", "GET", "https://kyfw.12306.cn/otn/confirmPassenger/initDc"),

    RESULT_FOR_ORDER = new ReqInfo("https://kyfw.12306.cn/otn/confirmPassenger/resultOrderForDcQueue", "POST", "https://kyfw.12306.cn/otn/confirmPassenger/initDc"),

    MY_ORDER = new ReqInfo("https://kyfw.12306.cn/otn/queryOrder/queryMyOrderNoComplete", "POST", "https://kyfw.12306.cn/otn/queryOrder/initNoComplete"),

    RECONGIZE = new ReqInfo("http://60.205.200.159/api", "POST", ""),

    RECONGIZE_RESULT = new ReqInfo("http://check.huochepiao.360.cn/img_vcode", "POST", "");

}


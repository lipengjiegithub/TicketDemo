package com.example.env;

public class ReqInfo {

    public String method,
            url,
            referer;

    public ReqInfo(String url, String method, String referer) {
        this.method = method;
        this.url = url;
        this.referer = referer;
    }
}

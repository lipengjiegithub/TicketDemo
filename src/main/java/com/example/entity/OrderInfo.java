package com.example.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderInfo {

    public int maxTicketNum; // 余票总数量,不分类型
    public String purpose_codes;
    public String tour_flag;
    public String train_location;
    public String key_check_isChange;
    public String leftTicketStr;
    public QueryLeftNewDetailDTO queryLeftNewDetailDTO;
    public QueryLeftTicketRequestDTO queryLeftTicketRequestDTO;
    public JSONObject limitBuySeatTicketDTO;  //可以买的票


    /**
     * 获取能够买的座位类型
     * @return
     */
    public List<String> getCanBySeatType() {
        List<String> seats = new ArrayList<>();
        if(maxTicketNum != 0) {
            JSONArray temp = limitBuySeatTicketDTO.getJSONArray("seat_type_codes");
            for (int i = 0; i < temp.size(); i++) {
                seats.add(((JSONObject) temp.get(i)).getString("id"));

            }
        }
        return seats;
    }

    public String getTranDate() {
        SimpleDateFormat sf = new SimpleDateFormat();
        sf.applyPattern("yyyyMMdd");
        Date date = null;
        try {
            date = sf.parse(queryLeftTicketRequestDTO.train_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT+0800' (中国标准时间)", Locale.ENGLISH);
        return sf.format(date);
    }

    private class SeatType {
        private String id, value;
    }

}

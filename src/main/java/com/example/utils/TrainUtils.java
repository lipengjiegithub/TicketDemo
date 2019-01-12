package com.example.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.Ticket;
import com.example.query.CityUtils;
import com.example.submit.Submit;
import net.dongliu.requests.RawResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class TrainUtils {

    private static Log log = LogFactory.getLog(Submit.class);

    public static JSONObject parse(RawResponse resp) {
        JSONObject obj = new JSONObject();
        try {
            obj = JSONObject.parseObject(resp.readToText());

            if(obj == null) return new JSONObject();

            boolean status = obj.getBoolean("status");
            JSONArray message = obj.getJSONArray("messages");

            if(status) return obj.getJSONObject("data");

            log.info(message.toString());

        }catch (JSONException e) {
            log.error(obj.get("data"));
            obj = new JSONObject();
        }
        return obj;
    }

    public static void print(List<Ticket> tickets) {
        int width = 20;
        String[] titles = {"车次", "起始站", "终点站", "出发时间", "到达时间", "历时", "二等座", "高级软卧", "一等软卧"};
        PrintUtils.print(width, titles);
        tickets.forEach(ticket -> {
            PrintUtils.print(width,
                    ticket.trainNo,
                    CityUtils.code2Name(ticket.startStationCode),
                    CityUtils.code2Name(ticket.endStationCode),
                    ticket.leaveTime,
                    ticket.arriveTime,
                    ticket.totalConsume,
                    ticket.secondClassSeat,
                    ticket.advancedSoftSleep,
                    ticket.softSleep);
        });
        PrintUtils.printFooter(width, titles.length);
    }
}

package com.example.submit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.OrderInfo;
import com.example.entity.Passenger;
import com.example.entity.SeatType;
import com.example.entity.Ticket;
import com.example.env.ConfigKey;
import com.example.env.URLConfig;
import com.example.query.CityUtils;
import com.example.utils.ConfigUtils;
import com.example.utils.HttpUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;

import java.util.*;
import java.util.function.Predicate;

public class Submit {

    private Ticket ticket = new Ticket();
    private static Log log = LogFactory.getLog(Submit.class);

    public Submit(Ticket ticket) {
        this.ticket = ticket;
    }
    private List<Passenger> passengers = new ArrayList<>();
    private OrderInfo orderInfo = new OrderInfo();
    private Passenger passenger = new Passenger();

    /**
     * 检查当前用户
     */
    public void checkUser() {
        log.debug("检查当前用户");
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_json_att", "");
        HttpUtils.send(URLConfig.CHECK_USER, params);
    }

    /**
     * 预购
     */
    public void submitOrderRequest() {

        log.debug("预订车票");
        Map<String, String> params = new LinkedHashMap<>();
        params.put("secretStr", HttpUtils.decodeStr(this.ticket.secretStr));
        params.put("train_date", ConfigUtils.getStr(ConfigKey.TRAIN_DATE));
        params.put("back_train_date", ConfigUtils.formatDate(new Date()));
        params.put("tour_flag", this.orderInfo.tour_flag);
        params.put("purpose_codes", this.ticket.passengerType);
        params.put("query_from_station_name", CityUtils.code2Name(this.ticket.fromStationCode));
        params.put("query_to_station_name", CityUtils.code2Name(this.ticket.toStationCode));
        params.put("undefined", "");
        Connection.Response resp = HttpUtils.send(URLConfig.SUBMIT_ORDER, params);
        JSONObject obj = HttpUtils.resp2JsonObj(resp);

        boolean status = obj.getBoolean("status");
        String[] message = (String[])obj.get("messages");

    }

    /**
     * 获得乘客信息
     * @param repeatToken 防重复提交TOKEN
     */
    private List<Passenger> queryPassenger(String repeatToken) {

        log.debug("获取乘客信息");
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_json_att", "");
        params.put("REPEAT_SUBMIT_TOKEN", repeatToken);
        Connection.Response resp = HttpUtils.send(URLConfig.PASSENGERS, params);
        JSONObject obj = HttpUtils.resp2JsonObj(resp);

        boolean status = obj.getBoolean("status");
        String[] message = (String[])obj.get("messages");

        if(status) {
            JSONArray array = obj.getJSONObject("data").getJSONArray("normal_passengers");
            parsePassenger(array);
        }else {
            log.error(String.valueOf(message));
        }

        return this.passengers;
    }

    /**
     * 转换乘客信息为易操作的对象
     * @param passengers 乘客信息
     */
    private void parsePassenger(JSONArray passengers) {
        this.passengers = passengers.toJavaList(Passenger.class);
    }

    private void choosePassenger(String id) {
        passengers.removeIf(new Predicate<Passenger>() {
            @Override
            public boolean test(Passenger passenger) {
                return !(passenger.passenger_id_no == id);
            }
        });
        if(passengers.size() > 0) this.passenger = passengers.get(0);
    }

    private void checkOrderInfo(String repeatToken) {

        log.debug("检查订单信息");
        Map<String, String> params = new LinkedHashMap<>();
        params.put("cancel_flag", "2");
        params.put("bed_level_order_num", "000000000000000000000000000000");
        params.put("passengerTicketStr", generatePassengerTicketStr());
        params.put("oldPassengerStr", generateOldPassengerStr());
        params.put("tour_flag", this.orderInfo.tour_flag);
        params.put("randCode", "");
        params.put("whatsSelect", "1");
        params.put("_json_att", "");
        params.put("REPEAT_SUBMIT_TOKEN", repeatToken);

        Connection.Response resp = HttpUtils.send(URLConfig.CHECK_ORDER, params);

        JSONObject obj = HttpUtils.resp2JsonObj(resp);

        boolean status = obj.getBoolean("status");
        String[] message = (String[])obj.get("messages");
        int submitStatus = obj.getJSONObject("data").getIntValue("submitStatus");
        int ticket = obj.getJSONObject("data").getIntValue("ticket");

    }

    private void getQueueCount(String repeatToken) throws Exception{

        log.debug("查询排队信息");
        Map<String, String> params = new LinkedHashMap<>();
        params.put("train_date", this.orderInfo.getTranDate());
        params.put("train_no", this.orderInfo.queryLeftNewDetailDTO.train_no);
        params.put("stationTrainCode", this.orderInfo.queryLeftNewDetailDTO.station_train_code);
        params.put("seatType", this.ticket.seatType.getCode());
        params.put("fromStationTelecode", this.orderInfo.queryLeftNewDetailDTO.from_station_telecode);
        params.put("toStationTelecode", this.orderInfo.queryLeftNewDetailDTO.to_station_telecode);
        params.put("leftTicket", this.orderInfo.leftTicketStr);
        params.put("purpose_codes", this.orderInfo.purpose_codes);
        params.put("train_location", this.orderInfo.train_location);
        params.put("_json_att", "");
        params.put("REPEAT_SUBMIT_TOKEN", repeatToken);

        Connection.Response resp = HttpUtils.send(URLConfig.QUEUE, params);
        JSONObject obj = HttpUtils.resp2JsonObj(resp);

        boolean status = obj.getBoolean("status");
        String[] message = (String[])obj.get("messages");
        int count = obj.getJSONObject("data").getIntValue("count");
        int ticket = obj.getJSONObject("data").getIntValue("ticket");
    }

    private String generatePassengerTicketStr() {
        return String.format("%s,0,%s,%s,%s,%s,%s,N",
                this.ticket.seatType.getCode(),
                1,
                passenger.passenger_name,
                passenger.passenger_id_type_code,
                passenger.passenger_id_no,
                passenger.mobile_no
        );
    }

    private String generateOldPassengerStr() {
        return String.format("%s,%s,%s,1_",
                this.ticket.seatType.getCode(),
                1,
                passenger.passenger_name,
                passenger.passenger_id_type_code,
                passenger.passenger_id_no
        );
    }

    private void chooseSeat() {
        if (this.orderInfo.getCanBySeatType().size() == 0) {
            log.error("当前车次无可预订的车票");
            Thread.currentThread().interrupt();
        }else {
            boolean flag = false;
            for(String seat: this.orderInfo.getCanBySeatType()) {
                if(seat == this.ticket.seatType.getCode()) {
                    flag = true;
                }
            }
            if(!flag) {
                log.fatal(String.format("您选的%s在车次%s中无剩余车票", this.ticket.seatType.getName(), this.ticket.trainNo));
                Thread.currentThread().interrupt();
            }
        }
    }

    public void submit() {

        try {
            checkUser();

            submitOrderRequest();

            Token token = new Token();

            this.orderInfo = token.getTicketInfo();

            List<Passenger> passengers = queryPassenger(token.getRepeatToken());

            checkOrderInfo(token.getRepeatToken());

            getQueueCount(token.getRepeatToken());

        }catch (Exception e) {
            log.error("订单提交异常", e);
            e.printStackTrace();
        }

    }

}

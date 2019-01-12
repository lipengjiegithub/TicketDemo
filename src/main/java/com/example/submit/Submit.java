package com.example.submit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.OrderInfo;
import com.example.entity.Passenger;
import com.example.entity.Ticket;
import com.example.env.ConfigKey;
import com.example.env.URLConfig;
import com.example.query.CityUtils;
import com.example.utils.ConfigUtils;
import com.example.utils.HttpUtils;
import com.example.utils.TrainUtils;
import net.dongliu.requests.RawResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.sun.tools.doclint.Entity.or;
import static com.sun.tools.doclint.Entity.para;

public class Submit {

    private static Log log = LogFactory.getLog(Submit.class);

    /**
     * 检查当前用户
     */
    public boolean checkUser() {
        log.debug("检查当前用户");
        Map<String, String> params = new LinkedHashMap<>();
        params.put("_json_att", "");
        try {
            RawResponse resp = HttpUtils.send(URLConfig.CHECK_USER, params);
            JSONObject obj = TrainUtils.parse(resp);
            return true;
        }catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    /**
     * 预购
     */
    public boolean submitOrderRequest(Ticket ticket) {

        log.debug("预订车票");
        Map<String, String> params = new LinkedHashMap<>();
        params.put("secretStr", HttpUtils.decodeStr(ticket.secretStr));
        params.put("train_date", ConfigUtils.getStr(ConfigKey.TRAIN_DATE));
        params.put("back_train_date", ConfigUtils.formatDate(new Date()));
        params.put("tour_flag", ConfigUtils.getStr(ConfigKey.TOUR_FLAG));
        params.put("purpose_codes", ticket.passengerType);
        params.put("query_from_station_name", CityUtils.code2Name(ticket.fromStationCode));
        params.put("query_to_station_name", CityUtils.code2Name(ticket.toStationCode));
        params.put("undefined", "");
        RawResponse resp = HttpUtils.send(URLConfig.SUBMIT_ORDER, params);

        JSONObject obj = TrainUtils.parse(resp);

        if(!obj.isEmpty()) {
            // TODO需要打印结果
        }
        return true;

    }

    /**
     * 获得乘客信息
     * @param repeatToken 防重复提交TOKEN
     */
    private List<Passenger> queryPassenger(String repeatToken) {

        log.debug("获取乘客信息");

        List<Passenger> passengers = new ArrayList<>();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("_json_att", "");
        params.put("REPEAT_SUBMIT_TOKEN", repeatToken);
        RawResponse resp = HttpUtils.send(URLConfig.PASSENGERS, params);

        JSONObject obj = TrainUtils.parse(resp);

        if(!obj.isEmpty()) {
            JSONArray array = obj.getJSONArray("normal_passengers");
            if(array != null) passengers = parsePassenger(array);
        }
        return passengers;
    }

    /**
     * 转换乘客信息为易操作的对象
     * @param passengers 乘客信息
     */
    private List<Passenger> parsePassenger(JSONArray passengers) {
        return passengers.toJavaList(Passenger.class);
    }

    /**
     * 选择乘客
     * @param passengers 乘客列表
     * @return 目标乘客
     */
    private Passenger choosePassenger(List<Passenger> passengers) {

        String passengerId = ConfigUtils.getStr(ConfigKey.PASSENGERS_ID);

        if(StringUtils.isBlank(passengerId)) {
            log.warn("请先配置PASSENGERS_ID");
        }

        log.debug(String.format("选择乘客%s", passengerId));

        Passenger passenger = new Passenger();

        passengers = passengers.stream()
                  .filter(e -> e.passenger_id_no.equalsIgnoreCase(passengerId))
                  .collect(Collectors.toList());
        if(!passengers.isEmpty()) return passengers.get(0);
        log.warn("未找到匹配的乘客,请核对身份证");
        return null;
    }

    private boolean checkOrderInfo(Ticket ticket, OrderInfo orderInfo, Passenger passenger, String repeatToken) {

        log.debug("检查订单信息");
        Map<String, String> params = new LinkedHashMap<>();
        params.put("cancel_flag", "2");
        params.put("bed_level_order_num", "000000000000000000000000000000");
        params.put("passengerTicketStr", generatePassengerTicketStr(ticket, passenger));
        params.put("oldPassengerStr", generateOldPassengerStr(ticket, passenger));
        params.put("tour_flag", ConfigUtils.getStr(ConfigKey.TOUR_FLAG));
        params.put("randCode", "");
        params.put("whatsSelect", "1");
        params.put("_json_att", "");
        params.put("REPEAT_SUBMIT_TOKEN", repeatToken);

        RawResponse resp = HttpUtils.send(URLConfig.CHECK_ORDER, params);

        JSONObject obj = TrainUtils.parse(resp);

        if(!obj.isEmpty()) {
            int submitStatus = obj.getIntValue("submitStatus");

//            canChooseBeds: "N"
//            canChooseSeats: "Y"
//            choose_Seats: "OM"
//            ifShowPassCode: "N"
//            ifShowPassCodeTime: "1"
//            isCanChooseMid: "N"
//            smokeStr: ""
//            submitStatus: true

        }

        return true;

    }

    /**
     * 查询排队人数
     * @param ticket 需要预定的车票
     * @param orderInfo 订单信息
     * @param repeatToken repeatToken
     * @throws Exception
     */
    private void getQueueCount(Ticket ticket, OrderInfo orderInfo, String repeatToken) {

        log.debug("查询排队信息");
        Map<String, String> params = new LinkedHashMap<>();
        params.put("train_date", orderInfo.getTranDate());
        params.put("train_no", orderInfo.queryLeftNewDetailDTO.train_no);
        params.put("stationTrainCode", orderInfo.queryLeftNewDetailDTO.station_train_code);
        params.put("seatType", ticket.seatType.getCode());
        params.put("fromStationTelecode", orderInfo.queryLeftNewDetailDTO.from_station_telecode);
        params.put("toStationTelecode", orderInfo.queryLeftNewDetailDTO.to_station_telecode);
        params.put("leftTicket", orderInfo.leftTicketStr);
        params.put("purpose_codes", orderInfo.purpose_codes);
        params.put("train_location", orderInfo.train_location);
        params.put("_json_att", "");
        params.put("REPEAT_SUBMIT_TOKEN", repeatToken);

        RawResponse resp = HttpUtils.send(URLConfig.GET_QUEUE_COUNT, params);

        JSONObject obj = TrainUtils.parse(resp);

        if(!obj.isEmpty()) {
            int count = obj.getIntValue("count");
            int tic = obj.getIntValue("ticket");

            // ticket: "738,168"
            log.debug(String.format("当前排队人数:%s, 剩余车票数量:%s", count, tic));
        }

    }

    private String generatePassengerTicketStr(Ticket ticket, Passenger passenger) {
        return String.format("%s,0,%s,%s,%s,%s,%s,N",
                ticket.seatType.getCode(),
                1,
                passenger.passenger_name,
                passenger.passenger_id_type_code,
                passenger.passenger_id_no,
                passenger.mobile_no
        );
    }

    private String generateOldPassengerStr(Ticket ticket, Passenger passenger) {
        return String.format("%s,%s,%s,1_",
                ticket.seatType.getCode(),
                1,
                passenger.passenger_name,
                passenger.passenger_id_type_code,
                passenger.passenger_id_no
        );
    }

    private boolean canChooseSeat(Ticket ticket, OrderInfo orderInfo) {
        if (orderInfo.getCanBySeatType().isEmpty()) {
            log.error("当前车次无可预订的车票");
            return false;
        }else {

            boolean flag = orderInfo.getCanBySeatType()
                                    .stream()
                                    .anyMatch(seat -> seat.equalsIgnoreCase(ticket.seatType.getCode()));

            if(flag) return true;

            log.fatal(String.format("您选的%s在车次%s中无剩余车票", ticket.seatType.getName(), ticket.trainNo));
            return false;

        }
    }

    public void submit(Ticket ticket) {

        boolean ok = true;

        try {
            ok = checkUser();
            if(!ok) return;

            ok = submitOrderRequest(ticket);
            if(!ok) return;

            Token token = new Token();
            String html = token.getHtml();
            String repeatToken = token.getRepeatToken(html);
            OrderInfo orderInfo = token.getTicketInfo(html);

            if(orderInfo == null) return;
            List<Passenger> passengers = queryPassenger(repeatToken);

            Passenger passenger = choosePassenger(passengers);
            if(passenger == null) return;

            ok = canChooseSeat(ticket, orderInfo);
            if(!ok) return;

            ok = checkOrderInfo(ticket, orderInfo, passenger, repeatToken);
            if(!ok) return;

            getQueueCount(ticket, orderInfo, repeatToken);
            if(!ok) return;

            confirmSingleQueue(ticket, orderInfo, passenger, repeatToken);

            String orderId = wait(repeatToken);

            if (StringUtils.isNotBlank(orderId)) {
                resultOrderForDcQueue(orderId, repeatToken);
            }

        }catch (Exception e) {
            log.error("订单提交异常", e);
        }

    }

    private String wait(String repeatToken) {
        log.info("正在排队获取订单!");
        while (true) {
            JSONObject obj = queryOrderWaitTime(repeatToken);
            if(!obj.isEmpty()) {
                log.info("正在等待...");
                int waitTime = obj.getIntValue("waitTime");
                String orderId = obj.getString("orderId");
                if(waitTime < 0) {
                    if(StringUtils.isNoneBlank(orderId)) {
                        log.info(String.format("订单提交成功,订单编号:%s", orderId));
                        return orderId;
                    }else {
                        log.info(obj.toJSONString());
                        return "";
                    }
                }
                log.info(String.format("未出票,订单排队中...预计等待时间:%s%s", waitTime > 60? waitTime/60: waitTime, waitTime > 60? "分钟": "秒"));
                try {
                    Thread.sleep(waitTime % 60 - 1);
                } catch (InterruptedException e) {
                    log.error(e);
                }
            }
        }
    }

    private void resultOrderForDcQueue(String orderId, String repeatToken) {

        log.info("查询排队信息");
        Map<String, String> params = new LinkedHashMap<>();
        params.put("orderSequence_no" ,orderId);
        params.put("_json_att" ,"");
        params.put("REPEAT_SUBMIT_TOKEN" ,repeatToken);

        RawResponse resp = HttpUtils.send(URLConfig.RESULT_FOR_ORDER, params);

        JSONObject obj = TrainUtils.parse(resp);

        if(!obj.isEmpty()) {

        }
    }

    private JSONObject queryOrderWaitTime(String repeatToken) {

        Map<String, String> params = new LinkedHashMap<>();

        params.put("random", String.valueOf(new Date().getTime()));
        params.put("tourFlag", ConfigUtils.getStr(ConfigKey.TOUR_FLAG));
        params.put("_json_att", "");
        params.put("REPEAT_SUBMIT_TOKEN" ,repeatToken);
        RawResponse resp = HttpUtils.send(URLConfig.ORDER_WAIT_TIME, params);
        JSONObject obj = TrainUtils.parse(resp);
        return obj;
    }

    private void confirmSingleQueue(Ticket ticket, OrderInfo orderInfo, Passenger passenger, String repeatToken) {

        Map<String, String> params = new LinkedHashMap<>();
        params.put("passengerTicketStr", generatePassengerTicketStr(ticket, passenger));
        params.put("oldPassengerStr", generateOldPassengerStr(ticket, passenger));
        params.put("randCode", "");
        params.put("purpose_codes", orderInfo.purpose_codes);
        params.put("key_check_isChange", orderInfo.key_check_isChange);
        params.put("leftTicketStr", orderInfo.leftTicketStr);
        params.put("train_location", orderInfo.train_location);
        params.put("choose_seats", "");
        params.put("seatDetailType", "000");
        params.put("whatsSelect", "1");
        params.put("roomType", "00");
        params.put("dwAll", "N");
        params.put("_json_att", "");
        params.put("REPEAT_SUBMIT_TOKEN" ,repeatToken);

        RawResponse resp = HttpUtils.send(URLConfig.CONFIRM_FOR_QUEUE, params);

        JSONObject obj = TrainUtils.parse(resp);

        if(!obj.isEmpty()) {
            int count = obj.getIntValue("count");
            int tic = obj.getIntValue("ticket");
            boolean submitStatus = obj.getBoolean("submitStatus");

            // ticket: "738,168"
            log.debug(String.format("当前排队人数:%s, 剩余车票数量:%s", count, tic));
        }
    }

}

package com.example.query;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.PassengerType;
import com.example.entity.SeatType;
import com.example.entity.Ticket;
import com.example.env.ConfigKey;
import com.example.env.URLConfig;
import com.example.utils.ConfigUtils;
import com.example.utils.HttpUtils;
import com.example.utils.TrainUtils;
import net.dongliu.requests.RawResponse;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.function.Predicate;

public class Query {

    private static final int INDEX_TRAIN_NO = 3,
    INDEX_TRAIN_START_STATION_CODE = 4,
    INDEX_TRAIN_END_STATION_CODE = 5,
    INDEX_TRAIN_FROM_STATION_CODE = 6,
    INDEX_TRAIN_TO_STATION_CODE = 7,
    INDEX_TRAIN_LEAVE_TIME = 8,
    INDEX_TRAIN_ARRIVE_TIME = 9,
    INDEX_TRAIN_TOTAL_CONSUME = 10,
    INDEX_TRAIN_HAS = 11,
    INDEX_TRAIN_BUSINESS_SEAT = 32,
    INDEX_TRAIN_FIRST_CLASS_SEAT = 31,
    INDEX_TRAIN_SECOND_CLASS_SEAT = 30,
    INDEX_TRAIN_ADVANCED_SOFT_SLEEP = 21,
    INDEX_TRAIN_SOFT_SLEEP = 23,
    INDEX_TRAIN_MOVE_SLEEP = 33,
    INDEX_TRAIN_HARD_SLEEP = 28,
    INDEX_TRAIN_SOFT_SEAT = 24,
    INDEX_TRAIN_HARD_SEAT = 29,
    INDEX_TRAIN_NO_SEAT = 28,
    INDEX_TRAIN_OTHER = 22,
    INDEX_TRAIN_MARK = 1,
    INDEX_SECRET_STR = 0,
    INDEX_START_DATE = 13;


    /**
     * 查询
     * @param startStation 起始站
     * @param endStation 终点站
     * @param startTime 发车时间
     * @param passengerType 乘客类型
     * @return 列表
     */
    public static List<Ticket> query(String startStation, String endStation, String startTime, PassengerType passengerType) {

        Map<String, String> params = new LinkedHashMap<>();

        params.put("leftTicketDTO.train_date", startTime);
        params.put("leftTicketDTO.from_station", CityUtils.name2Code(startStation));
        params.put("leftTicketDTO.to_station", CityUtils.name2Code(endStation));
        params.put("purpose_codes", passengerType.getCode());
        RawResponse resp = HttpUtils.send(URLConfig.QUERY, params);
        JSONObject obj = JSONObject.parseObject(resp.readToText());
        return parse(obj, passengerType);
    }

    /**
     * 解析请求的查询,转换为易操作的对象
     * @param obj 查询的数据
     * @param passengerType 乘客类型
     * @return 票集合
     */
    private static List<Ticket> parse(JSONObject obj, PassengerType passengerType) {
        List<Ticket> tickets = new ArrayList<>();
        JSONArray result = obj.getJSONObject("data").getJSONArray("result");
        for (String item: result.toJavaList(String.class)) {
            String[] info = item.split("\\|");
            Ticket ticket = new Ticket();
            ticket.passengerType = passengerType.getCode();
            ticket.trainNo = info[INDEX_TRAIN_NO];
            ticket.startStationCode = info[INDEX_TRAIN_START_STATION_CODE];
            ticket.endStationCode = info[INDEX_TRAIN_END_STATION_CODE];
            ticket.fromStationCode = info[INDEX_TRAIN_FROM_STATION_CODE];
            ticket.toStationCode = info[INDEX_TRAIN_TO_STATION_CODE];
            ticket.leaveTime = info[INDEX_TRAIN_LEAVE_TIME];
            ticket.arriveTime = info[INDEX_TRAIN_ARRIVE_TIME];
            ticket.totalConsume = info[INDEX_TRAIN_TOTAL_CONSUME];
            ticket.has = info[INDEX_TRAIN_HAS];
            ticket.businessSeat = info[INDEX_TRAIN_BUSINESS_SEAT];
            ticket.firstClassSeat = info[INDEX_TRAIN_FIRST_CLASS_SEAT];
            ticket.secondClassSeat = info[INDEX_TRAIN_SECOND_CLASS_SEAT];
            ticket.advancedSoftSleep = info[INDEX_TRAIN_ADVANCED_SOFT_SLEEP];
            ticket.softSleep = info[INDEX_TRAIN_SOFT_SLEEP];
            ticket.moveSleep = info[INDEX_TRAIN_MOVE_SLEEP];
            ticket.hardSleep = info[INDEX_TRAIN_HARD_SLEEP];
            ticket.softSeat = info[INDEX_TRAIN_SOFT_SEAT];
            ticket.hardSeat = info[INDEX_TRAIN_HARD_SEAT];
            ticket.noSeat = info[INDEX_TRAIN_NO_SEAT];
            ticket.other = info[INDEX_TRAIN_OTHER];
            ticket.mark = info[INDEX_TRAIN_MARK];
            ticket.startStation = CityUtils.name2Code(ticket.startStationCode);
            ticket.endStation = CityUtils.name2Code(ticket.endStationCode);
            ticket.fromStation = CityUtils.name2Code(ticket.fromStationCode);
            ticket.toStation = CityUtils.name2Code(ticket.toStationCode);
            ticket.secretStr = info[INDEX_SECRET_STR];
            ticket.startDate = info[INDEX_START_DATE];
            tickets.add(ticket);
        }
        return tickets;
    }

    /**
     * 控制台打印
     * @param tickets 票集合
     */
    public static void printTickets(List<Ticket> tickets) {
        for (Ticket ticket: tickets) {
            System.out.println(ticket.toString());
        }
    }

    /**
     * 过滤需要的车次
     * @param tickets 票
     * @return 需要的票
     */
    public static List<Ticket> filter(List<Ticket> tickets, String[] trainNos, String[] seatTypes) {

        // 移除不需要的
        tickets.removeIf(new Predicate<Ticket>() {
            @Override
            public boolean test(Ticket ticket) {

                return !(ArrayUtils.contains(trainNos, ticket.trainNo) && ticket.has.equals("Y"));
            }
        });
        return tickets;
    }

    /**
     * 选座
     * @param ticket 票
     * @param seatTypes 座位类型
     */
    public static boolean chooseSeat(Ticket ticket, String[] seatTypes) {
        boolean flag = false;
        for (String seatType: seatTypes) {
            switch (seatType) {
                case "9":
                    flag = !("".equals(ticket.businessSeat) && "无".equals(ticket.businessSeat));
                    break;
                case "P":
                    flag = !("".equals(ticket.moveSleep) && "无".equals(ticket.moveSleep));
                    break;
                case "M":
                    flag = !("".equals(ticket.firstClassSeat) && "无".equals(ticket.firstClassSeat));
                    break;
                case "O":
                    flag = !("".equals(ticket.secondClassSeat) && "无".equals(ticket.secondClassSeat));
                    break;
                case "6":
                    flag = !("".equals(ticket.advancedSoftSleep) && "无".equals(ticket.advancedSoftSleep));
                    break;
                case "4":
                    flag = !("".equals(ticket.softSleep) && "无".equals(ticket.softSleep));
                    break;
                case "3":
                    flag = !("".equals(ticket.hardSleep) && "无".equals(ticket.hardSleep));
                    break;
                case "2":
                    flag = !("".equals(ticket.softSeat) && "无".equals(ticket.softSeat));
                    break;
                case "1":
                    flag = !("".equals(ticket.hardSeat) && "无".equals(ticket.hardSeat));
                    break;
                case "0":
                    flag = !("".equals(ticket.noSeat) && "无".equals(ticket.noSeat));
                    break;
                default:
                    flag = false;
                    break;
            }
            if(flag == true) {
                ticket.seatType = SeatType.find(seatType);
                break;
            }
        }
        return flag;
    }

    /**
     * 测试使用
     */
    public static void loop() {

        CityUtils.load();
        ConfigUtils.load();

        final String startStation = ConfigUtils.getStr(ConfigKey.FROM_STATION);
        final String endStation = ConfigUtils.getStr(ConfigKey.TO_STATION);
        final String startTime = ConfigUtils.getStr(ConfigKey.TRAIN_DATE);
        final PassengerType passengerType = PassengerType.ADULT;

        final String[] trainNos = ConfigUtils.getArr(ConfigKey.TRAINS_NO);
        final String[] seatTypes = ConfigUtils.getArr(ConfigKey.SEAT_TYPE_CODE);

        long INTERVAL = Long.parseLong(ConfigUtils.getStr(ConfigKey.QUERY_TICKET_REFERSH_INTERVAL));

        while (true) {

            List<Ticket> tickets = Query.query(startStation, endStation, startTime, passengerType);

            tickets = Query.filter(tickets, trainNos, seatTypes);

            if(!tickets.isEmpty()) {

                for (Ticket ticket: tickets) {
                    Query.chooseSeat(ticket, seatTypes);
                }

                TrainUtils.print(tickets);
            }

            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Query.loop();
    }

}

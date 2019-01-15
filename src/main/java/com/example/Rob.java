package com.example;

import com.example.entity.PassengerType;
import com.example.entity.Ticket;
import com.example.env.ConfigKey;
import com.example.login.Login;
import com.example.query.CityUtils;
import com.example.query.Query;
import com.example.submit.Submit;
import com.example.utils.ConfigUtils;
import com.example.utils.PrintUtils;
import com.example.utils.TrainUtils;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Rob {


    static final String username;
    static final String password ;
    static final int robCount = 20;

    static final String[] trainNos;
    static final String[] seatTypes;

    static final String startStation;
    static final String endStation;
    static final String startTime;
    static final PassengerType passengerType = PassengerType.ADULT;

    static ConcurrentLinkedQueue<Ticket> concurrentTickets = new ConcurrentLinkedQueue<>();

    static long INTERVAL;

    static {
        CityUtils.load();
        ConfigUtils.load();

        username = ConfigUtils.getStr(ConfigKey.USER_NAME);
        password = ConfigUtils.getStr(ConfigKey.USER_PWD);

        trainNos = ConfigUtils.getArr(ConfigKey.TRAINS_NO);
        seatTypes = ConfigUtils.getArr(ConfigKey.SEAT_TYPE_CODE);

        startStation = ConfigUtils.getStr(ConfigKey.FROM_STATION);
        endStation = ConfigUtils.getStr(ConfigKey.TO_STATION);
        startTime = ConfigUtils.getStr(ConfigKey.TRAIN_DATE);
        INTERVAL = Long.parseLong(ConfigUtils.getStr(ConfigKey.QUERY_TICKET_REFERSH_INTERVAL));
    }

    public static boolean login() {
        Login login = new Login(username, password);
        while (!login.login()){
            System.out.println("重新登录");
        }
        return true;
    }

    public static void loop() {
        List<Ticket> tickets = Query.query(startStation, endStation, startTime, passengerType);


        tickets = Query.filter(tickets, trainNos, seatTypes);

        if(!tickets.isEmpty()) {

            concurrentTickets.addAll(tickets);

//            Query.printTickets(tickets);
            TrainUtils.print(tickets);
        }

        try {
            Thread.sleep(INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void submit() {
        ExecutorService executors = Executors.newFixedThreadPool(robCount);

        while (true) {
            executors.execute(new Thread(() -> {
                Ticket ticket = concurrentTickets.poll();
                if(ticket != null&& ticket.trainNo != null) new Submit().submit(ticket);
            }));
        }
    }

    public static void query() {
        Executors.newScheduledThreadPool(10).scheduleAtFixedRate(new Thread(() -> Rob.loop()), 0, INTERVAL, TimeUnit.MILLISECONDS);
    }

    public static void verify() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Thread(() -> Login.verify()), 0, 6000, TimeUnit.MILLISECONDS);
    }

    public static void myOrder() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Thread(() -> Login.myOrder()), 0, 6000, TimeUnit.MILLISECONDS);
    }


    public static void main(String[] args) {
        Rob.login();

        Rob.query();

        Rob.submit();

        Rob.verify();

        Rob.myOrder();
    }

}

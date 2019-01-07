package com.example;

import com.example.entity.PassengerType;
import com.example.entity.Ticket;
import com.example.env.ConfigKey;
import com.example.login.Login;
import com.example.query.CityUtils;
import com.example.query.Query;
import com.example.submit.Submit;
import com.example.utils.ConfigUtils;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Rob {


    final String username = ConfigUtils.getStr(ConfigKey.USER_NAME);
    final String password = ConfigUtils.getStr(ConfigKey.USER_PWD);
    final int robCount = 20;

    final String[] trainNos = ConfigUtils.getArr(ConfigKey.TRAINS_NO);
    final String[] seatTypes = ConfigUtils.getArr(ConfigKey.SEAT_TYPE_CODE);

    ConcurrentLinkedQueue<Ticket> concurrentTickets = new ConcurrentLinkedQueue<>();

    long INTERVAL = Long.parseLong(ConfigUtils.getStr(ConfigKey.QUERY_TICKET_REFERSH_INTERVAL));

    static {
        CityUtils.load();
        ConfigUtils.load();
    }

    public Rob() {

        Login login = new Login(username, password);
        login.login();
    }

    public void loop() {
        final String startStation = ConfigUtils.getStr(ConfigKey.FROM_STATION);
        final String endStation = ConfigUtils.getStr(ConfigKey.TO_STATION);
        final String startTime = ConfigUtils.getStr(ConfigKey.TRAIN_DATE);
        final PassengerType passengerType = PassengerType.ADULT;



        while (true) {

            List<Ticket> tickets = Query.query(startStation, endStation, startTime, passengerType);

            tickets = Query.filter(tickets, trainNos, seatTypes);

            if(!tickets.isEmpty()) {

                concurrentTickets.addAll(tickets);

                Query.printTickets(tickets);

            }

            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void submit() {
        ExecutorService executors = Executors.newFixedThreadPool(robCount);

        while (true) {
            executors.execute(new Thread(() -> {
                Ticket ticket = concurrentTickets.poll();
                if(ticket != null) new Submit(ticket).submit();
            }));
        }
    }


    public static void main(String[] args) {
        Rob rob = new Rob();
        new Thread(() -> rob.loop()).start();
        rob.submit();
    }

}

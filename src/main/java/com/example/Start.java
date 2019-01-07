package com.example;

import com.example.entity.Ticket;
import com.example.login.Login;

import java.util.ArrayList;
import java.util.List;

public class Start {

    public static void main(String[] args) {

        Rob rob = new Rob();

        rob.loop();

        rob.submit();

//        doSubmit();

    }

    static boolean doLogin() {
        String username = "13162631762";
        String password = "mima0225";
        Login login = new Login(username, password);
        return login.login();
    }

    static List<Ticket> doQuery() {
        List<Ticket> tickets = new ArrayList<>();

        return tickets;
    }

    static void doSubmit(Ticket ticket) {

    }
}

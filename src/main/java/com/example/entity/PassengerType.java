package com.example.entity;

public enum PassengerType {
    ADULT("ADULT", "成人"),
    STUDENT("0X00", "学生");

    private final String CODE;
    private final String NAME;


    private PassengerType(String code, String name) {
        this.CODE = code;
        this.NAME = name;
    }

    public String getCode() {
        return this.CODE;
    }

    public String getName() {
        return this.NAME;
    }

}

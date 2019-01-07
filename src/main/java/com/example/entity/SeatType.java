package com.example.entity;

public enum SeatType {

    BUSINESS_SEAT("9", "商务座"),
    SPECIAL_SEAT("P", "特等座"),
    FIRST_CLASS_SEAT("M", "一等座"),
    SECOND_CLASS_SEAT("O", "二等座"),
    ADVANCED_SOFT_SLEEP("6", "高级软卧"),
    SOFT_SLEEP("4", "软卧"),
    HARD_SLEEP("3", "硬卧"),
    SOFT_SEAT("2", "软座"),
    HARD_SEAT("1", "硬座"),
    NO_SEAT("0", "无座");

    private final String CODE;
    private final String NAME;


    private SeatType(String code, String name) {
        this.CODE = code;
        this.NAME = name;
    }

    public String getCode() {
        return this.CODE;
    }

    public String getName() {
        return this.NAME;
    }

    public static SeatType find(String code) {
        for (SeatType seatType: SeatType.values()) {
            if(code.equals(seatType.getCode())) {
                return seatType;
            }
        }
        return SeatType.SECOND_CLASS_SEAT;
    }

}

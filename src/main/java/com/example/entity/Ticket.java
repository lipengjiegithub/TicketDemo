package com.example.entity;

public class Ticket {

    public String trainNo;
    public String passengerType;
    public String startStationCode;
    public String endStationCode;
    public String fromStationCode;
    public String toStationCode;
    public String leaveTime;
    public String arriveTime;
    public String totalConsume;
    public String has;
    public String businessSeat;
    public String firstClassSeat;
    public String secondClassSeat;
    public String advancedSoftSleep;
    public String softSleep;
    public String moveSleep;
    public String hardSleep;
    public String softSeat;
    public String hardSeat;
    public String noSeat;
    public String other;
    public String mark;
    public String startStation;
    public String endStation;
    public String fromStation;
    public String toStation;
    public String secretStr;
    public String startDate;
    public SeatType seatType = SeatType.SECOND_CLASS_SEAT;


    @Override
    public String toString() {
        return "Ticket{" +
                "trainNo='" + trainNo + '\'' +
                ", passengerType='" + passengerType + '\'' +
                ", startStationCode='" + startStationCode + '\'' +
                ", endStationCode='" + endStationCode + '\'' +
                ", fromStationCode='" + fromStationCode + '\'' +
                ", toStationCode='" + toStationCode + '\'' +
                ", leaveTime='" + leaveTime + '\'' +
                ", arriveTime='" + arriveTime + '\'' +
                ", totalConsume='" + totalConsume + '\'' +
                ", has='" + has + '\'' +
                ", businessSeat='" + businessSeat + '\'' +
                ", firstClassSeat='" + firstClassSeat + '\'' +
                ", secondClassSeat='" + secondClassSeat + '\'' +
                ", advancedSoftSleep='" + advancedSoftSleep + '\'' +
                ", softSleep='" + softSleep + '\'' +
                ", moveSleep='" + moveSleep + '\'' +
                ", hardSleep='" + hardSleep + '\'' +
                ", softSeat='" + softSeat + '\'' +
                ", hardSeat='" + hardSeat + '\'' +
                ", noSeat='" + noSeat + '\'' +
                ", other='" + other + '\'' +
                ", mark='" + mark + '\'' +
                ", startStation='" + startStation + '\'' +
                ", endStation='" + endStation + '\'' +
                ", fromStation='" + fromStation + '\'' +
                ", toStation='" + toStation + '\'' +
                ", secretStr='" + secretStr + '\'' +
                ", startDate='" + startDate + '\'' +
                '}';
    }
}

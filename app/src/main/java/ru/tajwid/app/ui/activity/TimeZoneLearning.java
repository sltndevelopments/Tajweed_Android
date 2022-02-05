package ru.tajwid.app.ui.activity;

public class TimeZoneLearning {

    String name, hour1, hour2, day1, day2;

    public TimeZoneLearning() {
    }

    public TimeZoneLearning ( String name, String hour1, String hour2, String day1, String day2) {
        this.name = name;
        this.hour1 = hour1;
        this.hour2 = hour2;
        this.day1 = day1;
        this.day2 = day2;
    }


    public String getName() {
        return name;
    }

    public String getHour1() {
        return hour1;
    }

    public String getHour2() {
        return hour2;
    }

    public String getDay1() {
        return day1;
    }

    public String getDay2() {
        return day2;
    }
}

package com.example.biogenapp;

import java.io.Serializable;

class ParameterItem {
    private String parameter;
    private boolean defined;
    private double value;
    private String date, time;

    ParameterItem(String Parameter, double Value, String Date, String Time){
        parameter = Parameter;
        defined = true;
        value = Value;
        date = Date;
        time = Time;
    }

    public String getKey(){
        return parameter;
    }

    public double getValue(){
        return value;
    }

    public String getDate(){
        return date;
    }

    public String getTime(){
        return time;
    }


    public int compareTo(ParameterItem PI){
        int compDate = (date.compareTo(PI.date));
        if (compDate != 0) return compDate;
        return (time.compareTo(PI.time));
    }
}

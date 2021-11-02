package com.example.biogenapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExperimentItem implements Serializable {
    private String name;
    private List<ParameterItem> fields, compFields, toAdapter;
    private long startTime = 0;
    private long lastPause;
    private long stopTime;
    private boolean startFlag = false;
    private boolean pauseFlag = false;
    private boolean compMode = false;

    ExperimentItem(String Name){
        name = Name;
        fields = new ArrayList<>();
        compFields = new ArrayList<>();
        toAdapter = new ArrayList<>();
        lastPause = 0;
        stopTime = 0;
    }

    public void updateCompFields(){
        compFields.clear();
        for (ParameterItem i : fields){
            boolean flag = true;
            for (ParameterItem j : compFields){
                if (i.getKey().equals(j.getKey())) {
                    flag = false;
                    break;
                }
            }
            if (flag) compFields.add(i);
        }
    }

    public boolean isStarted(){
        return startFlag;
    }

    public boolean isPause(){
        return pauseFlag;
    }

    public boolean isCompMode(){
        return compMode;
    }

    public void switchPause(){
        pauseFlag = !pauseFlag;
    }

    public void switchMode(){
        compMode = !compMode;
        toAdapter.clear();
        if (compMode) toAdapter.addAll(compFields);
        else toAdapter.addAll(fields);
    }


    public long getStartTime(){
        return startTime;
    }

    public long getAllStopTime(){
        if (isPause()) return startTime + stopTime - lastPause;
        return startTime + stopTime;
    }

    public void Start(long currentTime){
        startTime = currentTime;
        startFlag = true;
        pauseFlag = true;
    }

    public List<ParameterItem> getParameterList(){
        return toAdapter;
    }

    public String getName(){
        return name;
    }

    public void addToStopTime(long time){
        stopTime += time - lastPause;
    }

    public void setLastPause(long time){
        lastPause = time;
    }

    private void sort(){
        Comparator<ParameterItem> comparator = (o1, o2) -> -o1.compareTo(o2);
        Collections.sort(fields, comparator);
    }

    public void add(String key, double value, String date, String time){
        fields.add(new ParameterItem(key, value, date, time));
        sort();
        updateCompFields();
        toAdapter.clear();
        if (compMode) toAdapter.addAll(compFields);
        else toAdapter.addAll(fields);
    }

    public void remove(int pos){
        ParameterItem element = toAdapter.get(pos);
        fields.remove(element);
        if (compMode){
            updateCompFields();
            toAdapter = compFields;
        }
        else{
            toAdapter.remove(element);
            updateCompFields();
        }
    }
}

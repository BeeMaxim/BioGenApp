package com.example.biogenapp;

import android.media.MediaMetadataRetriever;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ExperimentItem implements Serializable {
    private String time;
    private String temperature;
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
        time = "";
        temperature = "";
        fields = new ArrayList<ParameterItem>();
        compFields = new ArrayList<ParameterItem>();
        toAdapter = new ArrayList<ParameterItem>();
        lastPause = 0;
        stopTime = 0;
    }

    public void updateCompFields(){
        compFields.clear();
        for (ParameterItem i : fields){
            boolean flag = true;
            for (ParameterItem j : compFields){
                if (i.getKey().equals(j.getKey())) flag = false;
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

    public long getStopTime(){
        return stopTime;
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

    public void setName(String Name){
        name = Name;
    }

    public List<ParameterItem> getParameterList(){
        return toAdapter;
    }

    public String getTime(){
        return time;
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
        Comparator<ParameterItem> comparator = new Comparator<ParameterItem>() {
            @Override
            public int compare(ParameterItem o1, ParameterItem o2) {
                return -o1.compareTo(o2);
            }
        };
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

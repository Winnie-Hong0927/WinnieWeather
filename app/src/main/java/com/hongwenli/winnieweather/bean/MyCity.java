package com.hongwenli.winnieweather.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class MyCity {

    @NonNull
    @PrimaryKey
    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Ignore
    public MyCity(String cityName) {
        this.cityName = cityName;
    }

    public MyCity() {}
}


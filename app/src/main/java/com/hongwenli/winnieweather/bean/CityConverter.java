package com.hongwenli.winnieweather.bean;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//转换器
//在一张表里面又插入了一张表，不用这个转换器就会报错。
public class CityConverter {

    @TypeConverter
    public List<Province.City> stringToObject(String value) {
        Type userListType = new TypeToken<ArrayList<Province.City>>() {}.getType();
        return new Gson().fromJson(value, userListType);
    }

    @TypeConverter
    public String objectToString(List<Province.City> list) {
        return new Gson().toJson(list);
    }
}


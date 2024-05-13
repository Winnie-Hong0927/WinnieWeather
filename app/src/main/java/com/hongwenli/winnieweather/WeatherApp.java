package com.hongwenli.winnieweather;

import com.baidu.location.LocationClient;
import com.hongwenli.library.BaseApplication;
import com.hongwenli.library.network.api.NetworkApi;
import com.hongwenli.winnieweather.db.AppDatabase;
import com.hongwenli.winnieweather.personalCenter.db.DatabaseHelper;
import com.hongwenli.winnieweather.utils.MVUtils;
import com.tencent.mmkv.MMKV;

public class WeatherApp extends BaseApplication {

    //数据库
    private static AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        //使用定位需要同意隐私合规政策
        LocationClient.setAgreePrivacy(true);
        //初始化网络框架
        NetworkApi.init(new NetworkRequiredInfo(this));
        //MMKV初始化
        MMKV.initialize(this);
        //工具类初始化
        MVUtils.getInstance();
        //初始化Room数据库
        db = AppDatabase.getInstance(this);
    }

    public static AppDatabase getDb() {
        return db;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DatabaseHelper helper = DatabaseHelper.getInstance(this);
        helper.closeDB();
    }
}


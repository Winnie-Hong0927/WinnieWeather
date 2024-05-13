package com.hongwenli.library;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import lombok.Getter;


public class BaseApplication extends Application {//管理整个工程，让WeatherApp继承于它

    private static ActivityManager activityManager;
    @SuppressLint("StaticFieldLeak")
    private static BaseApplication application;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //声明Activity管理
        activityManager = new ActivityManager();
        context = getApplicationContext();
        application = this;

    }

    public static ActivityManager getActivityManager() {
        return activityManager;
    }

    public static void setActivityManager(ActivityManager activityManager) {
        BaseApplication.activityManager = activityManager;
    }

    public static BaseApplication getApplication() {
        return application;
    }

    public static void setApplication(BaseApplication application) {
        BaseApplication.application = application;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        BaseApplication.context = context;
    }
}

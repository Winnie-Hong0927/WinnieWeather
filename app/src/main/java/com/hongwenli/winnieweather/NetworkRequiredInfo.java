package com.hongwenli.winnieweather;

import android.app.Application;

import com.baidu.location.pb.BuildConfig;
import com.hongwenli.library.network.INetworkRequiredInfo;

public class NetworkRequiredInfo implements INetworkRequiredInfo {

    private final Application application;

    public NetworkRequiredInfo(Application application){
        this.application = application;
    }

    /**
     * 版本名
     */
    @Override
    public String getAppVersionName() {
        return "1.0";
    }

    /**
     * 版本号
     */
    @Override
    public String getAppVersionCode() {
        return String.valueOf("1.0.0");
    }

    /**
     * 是否为debug
     */
    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     * 应用全局上下文
     */
    @Override
    public Application getApplicationContext() {
        return application;
    }
}

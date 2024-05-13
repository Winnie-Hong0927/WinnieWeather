package com.hongwenli.library.network;

import android.app.Application;

public interface INetworkRequiredInfo {//用于获取App的版本名、版本号、运行状态、全局上下文参数
    /**
     * 获取App版本名
     */
    String getAppVersionName();

    /**
     * 获取App版本号
     */
    String getAppVersionCode();

    /**
     * 判断是否为Debug模式
     */
    boolean isDebug();

    /**
     * 获取全局上下文参数
     */
    Application getApplicationContext();
}

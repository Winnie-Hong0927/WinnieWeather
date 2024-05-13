package com.hongwenli.library.network.interceptor;

import android.annotation.SuppressLint;

import com.hongwenli.library.network.INetworkRequiredInfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RequestInterceptor implements Interceptor {
    //在网络数据交互的时候有请求和返回，那么在两个过程中我们可以获取一些信息，就需要拦截器
    //请求拦截器
    /**
     * 网络请求信息
     */
    private final INetworkRequiredInfo iNetworkRequiredInfo;

    public RequestInterceptor(INetworkRequiredInfo iNetworkRequiredInfo) {
        this.iNetworkRequiredInfo = iNetworkRequiredInfo;
    }

    /**
     * 拦截
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        //构建器
        Request.Builder builder = chain.request().newBuilder();
        //添加使用环境
        builder.addHeader("os", "android");
        //添加版本号
        builder.addHeader("appVersionCode", this.iNetworkRequiredInfo.getAppVersionCode());
        //添加版本名
        builder.addHeader("appVersionName", this.iNetworkRequiredInfo.getAppVersionName());
        //添加日期时间
        builder.addHeader("datetime", getNowDateTime());
        //返回
        return chain.proceed(builder.build());
    }

    public static String getNowDateTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}


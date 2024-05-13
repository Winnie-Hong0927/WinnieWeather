package com.hongwenli.winnieweather.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

//将定位功能封装一下，现在定位的大部分在代码在MainActivity中，这样不太合理
/**
 * 首先是一个单例，然后通过类构造方法，对定位进行初始化，
 * 初始化的同时调用内部类进行定位结果处理，
 * 最后再通过接口回调出去，
 * 注意一点我在获取定位结果之后就停止定位了，如果不停止的话会造成下一次定位没有结果返回。
 * 这个类的核心方法就三个：初始化、实现定位回调、开始定位
 * */
public class GoodLocation {
    private static volatile GoodLocation mInstance;
    @SuppressLint("StaticFieldLeak")
    private static LocationClient mLocationClient = null;
    //定位监听
    private GoodLocationListener goodLocationListener;
    //定位回调接口
    private static LocationCallback callback;
    public GoodLocation(Context context) {
        initLocation(context);
    }

    public static GoodLocation getInstance(Context context) {
        if (mInstance == null) {
            synchronized (GoodLocation.class) {
                if (mInstance == null) {
                    mInstance = new GoodLocation(context);
                }
            }
        }
        return mInstance;
    }
    /**
     * 初始化定位
     */
    private void initLocation(Context context) {
        try {
            goodLocationListener = new GoodLocationListener();
            mLocationClient = new LocationClient(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mLocationClient != null) {
            mLocationClient.registerLocationListener(goodLocationListener);
            LocationClientOption option = new LocationClientOption();
            option.setIsNeedAddress(true);
            option.setNeedNewVersionRgc(true);
            mLocationClient.setLocOption(option);
        }
    }
    /**
     * 需要定位的页面调用此方法进行接口回调处理
     */
    public void setCallback(LocationCallback callback) {
        GoodLocation.callback = callback;
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        if (mLocationClient != null) {
            mLocationClient.start();
        }
    }
    /**
     * 请求定位
     */
    private static void requestLocation() {
        if (mLocationClient != null) {
            mLocationClient.requestLocation();
        }
    }
    /**
     * 停止定位
     */
    private static void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }
    /**
     * 内部类实现百度定位结果接收
     */
    public static class GoodLocationListener extends BDAbstractLocationListener {
        private final String TAG = GoodLocationListener.class.getSimpleName();
        //该方法用于接受百度定位后返回的数据，即定位信息
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //定位数据为空
            if (bdLocation == null) return;
            //定位的区/县数据为空
            if (bdLocation.getDistrict() == null) {
                Log.e(TAG, "onReceiveLocation: 未获取区/县数据，您可以重新断开连接网络再尝试定位。");
                requestLocation();
            }
            //每次获取定位结果之后需要停止定位，否则会造成下一次定位没有结果返回。
            stopLocation();
            if (callback == null) {
                Log.e(TAG, "callback is Null!");
                return;
            }
            callback.onReceiveLocation(bdLocation);
        }
    }
}


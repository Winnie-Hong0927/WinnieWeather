package com.hongwenli.winnieweather.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.lifecycle.ViewModelProvider;

import com.hongwenli.library.base.NetworkActivity;
import com.hongwenli.winnieweather.Constant;
import com.hongwenli.winnieweather.bean.Province;
import com.hongwenli.winnieweather.databinding.ActivitySplashBinding;
import com.hongwenli.winnieweather.utils.*;
import com.hongwenli.winnieweather.viewmodel.SplashViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 首先从上面开始，沉浸式、然后viewModel绑定，
 * 检查是否第一次启动，是就读取本地城市数据，保存到数据库中，不是就跳过，
 * 最后延迟1秒进入MainActivity，里面有一个关于今天第一次启动的方法，目前还用不到，后面会用到，
 * EasyDate中没有getTodayTwelveTimestamp()方法，给它添加一个
 * */
//启动页
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends NetworkActivity<ActivitySplashBinding> {
    private SplashViewModel viewModel;
    private final String TAG = SplashActivity.class.getSimpleName();
    @Override
    protected void onCreate() {
        setFullScreenImmersion();
        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        //检查启动
        checkingStartup();
        //准备就绪后跳转到主界面
        new Handler().postDelayed(() -> jumpActivityFinish(MainActivity.class), 1000);
    }
//    检查启动
    private void checkingStartup() {
        if (MVUtils.getBoolean(Constant.FIRST_RUN, false)) return;
        //第一次运行，获取城市数据，没有就会去加载到数据库中
        viewModel.getAllCityData();
        MVUtils.put(Constant.FIRST_RUN, true);
    }
    @Override
    protected void onObserveData() {
        //城市数据返回
        viewModel.listMutableLiveData.observe(this, provinceList -> {
            if (provinceList.size() == 0) {
                Log.d(TAG, "onObserveData: 第一次添加数据");
                //没有保存过数据，只需要保存一次即可。
                List<Province> provinces = loadCityData();
                if (provinces != null) viewModel.addCityData(provinces);
            } else {
                Log.d(TAG, "onObserveData: 有数据了");
            }
        });
    }
//    加载本地的城市数据
    private List<Province> loadCityData() {
        List<Province> provinceList = new ArrayList<>();
        //读取城市数据
        InputStream inputStream;
        try {
            inputStream = getResources().getAssets().open("city.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String lines = bufferedReader.readLine();
            while (lines != null) {
                stringBuffer.append(lines);
                lines = bufferedReader.readLine();
            }
            final JSONArray jsonArray = new JSONArray(stringBuffer.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                Province province = new Province();
                List<Province.City> cityList = new ArrayList<>();
                //得到省份对象
                JSONObject provinceJsonObject = jsonArray.getJSONObject(i);
                province.setProvinceName(provinceJsonObject.getString("name"));
                //得到省份下的市数组
                JSONArray cityJsonArray = provinceJsonObject.getJSONArray("city");
                for (int j = 0; j < cityJsonArray.length(); j++) {
                    Province.City city = new Province.City();
                    List<Province.City.Area> areaList = new ArrayList<>();
                    //得到市对象
                    JSONObject cityJsonObject = cityJsonArray.getJSONObject(j);
                    city.setCityName(cityJsonObject.getString("name"));
                    //得到市下的区/县数组
                    JSONArray areaJsonArray = cityJsonObject.getJSONArray("area");
                    for (int k = 0; k < areaJsonArray.length(); k++) {
                        areaList.add(new Province.City.Area(areaJsonArray.getString(k)));
                    }
                    cityList.add(city);
                    city.setAreaList(areaList);
                }
                provinceList.add(province);
                province.setCityList(cityList);
            }
            return provinceList;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

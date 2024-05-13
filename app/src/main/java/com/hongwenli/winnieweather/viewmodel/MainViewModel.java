package com.hongwenli.winnieweather.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.hongwenli.library.base.BaseViewModel;
import com.hongwenli.winnieweather.bean.AirResponse;
import com.hongwenli.winnieweather.bean.DailyResponse;
import com.hongwenli.winnieweather.bean.HourlyResponse;
import com.hongwenli.winnieweather.bean.LifestyleResponse;
import com.hongwenli.winnieweather.bean.MyCity;
import com.hongwenli.winnieweather.bean.NowResponse;
import com.hongwenli.winnieweather.bean.Province;
import com.hongwenli.winnieweather.bean.SearchCityResponse;
import com.hongwenli.winnieweather.repository.CityRepository;
import com.hongwenli.winnieweather.repository.SearchCityRepository;
import com.hongwenli.winnieweather.repository.WeatherRepository;

import java.util.List;

public class MainViewModel extends BaseViewModel {
    public MutableLiveData<SearchCityResponse> searchCityResponseMutableLiveData = new MutableLiveData<>();
    public void searchCity(String cityName, boolean isExact) {
        new SearchCityRepository().searchCity(searchCityResponseMutableLiveData, failed, cityName, isExact);
    }

    //在ViewModel中去调用刚才所写的nowWeather()方法，因为都是在MainActivity中请求数据，那么理应将代码写在MainViewModel中
    public MutableLiveData<NowResponse> nowResponseMutableLiveData = new MutableLiveData<>();

    public void nowWeather(String cityId) {
        WeatherRepository.getInstance().nowWeather(nowResponseMutableLiveData,failed, cityId);
    }

    //添加对WeatherRepository中dailyWeather()方法的调用
    public MutableLiveData<DailyResponse> dailyResponseMutableLiveData = new MutableLiveData<>();
    //天气预报
    public void dailyWeather(String cityId) {
        WeatherRepository.getInstance().dailyWeather(dailyResponseMutableLiveData, failed, cityId);
    }

    //生活质量
    public MutableLiveData<LifestyleResponse> lifestyleResponseMutableLiveData = new MutableLiveData<>();

    public void lifestyle(String cityId) {
        WeatherRepository.getInstance().lifestyle(lifestyleResponseMutableLiveData, failed, cityId);
    }

    //获取城市数据
    public MutableLiveData<List<Province>> cityMutableLiveData = new MutableLiveData<>();

    public void getAllCity() {
        CityRepository.getInstance().getCityData(cityMutableLiveData);
    }

//    逐小时返回一个响应数据
    public MutableLiveData<HourlyResponse> hourlyResponseMutableLiveData = new MutableLiveData<>();

    public void hourlyWeather(String cityId) {
        WeatherRepository.getInstance().hourlyWeather(hourlyResponseMutableLiveData, failed, cityId);
    }

//    空气质量
    public MutableLiveData<AirResponse> airResponseMutableLiveData = new MutableLiveData<>();

    public void airWeather(String cityId) {
        WeatherRepository.getInstance().airWeather(airResponseMutableLiveData, failed, cityId);
    }

    /**
     * 添加我的城市数据，在定位之后添加数据
     */
    public void addMyCityData(String cityName) {
        MyCity myCity = new MyCity(cityName);
        CityRepository.getInstance().addMyCityData(myCity);
    }
}


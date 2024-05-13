package com.hongwenli.winnieweather.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.hongwenli.library.base.BaseViewModel;
import com.hongwenli.winnieweather.bean.Province;
import com.hongwenli.winnieweather.repository.CityRepository;

import java.util.List;

public class SplashViewModel extends BaseViewModel {

    public MutableLiveData<List<Province>> listMutableLiveData = new MutableLiveData<>();

    /**
     * 添加城市数据
     */
    public void addCityData(List<Province> provinceList) {
        CityRepository.getInstance().addCityData(provinceList);
    }

    /**
     * 获取所有城市数据
     */
    public void getAllCityData() {
        CityRepository.getInstance().getCityData(listMutableLiveData);
    }
}

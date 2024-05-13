package com.hongwenli.winnieweather.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.hongwenli.library.network.api.ApiType;
import com.hongwenli.library.network.BaseObserver;
import com.hongwenli.library.network.api.NetworkApi;
import com.hongwenli.winnieweather.api.ApiService;
import com.hongwenli.winnieweather.Constant;
import com.hongwenli.winnieweather.bean.SearchCityResponse;

/*
* 这里就是用到了网络框架，OKHttp做网络请求，Retrofit做接口封装和解析，RxJava做线程切换调度。
* 拿到数据之后我们在通过LiveData进行发送
* */
@SuppressLint("CheckResult")
public class SearchCityRepository {
    private static final class SearchCityRepositoryHolder {
             private static final SearchCityRepository mInstance = new SearchCityRepository();
    }

    public static SearchCityRepository getInstance() {
             return SearchCityRepositoryHolder.mInstance;
    }
    private static final String TAG = SearchCityRepository.class.getSimpleName();
    public void searchCity(MutableLiveData<SearchCityResponse> responseLiveData,
                           MutableLiveData<String> failed, String cityName, boolean isExact) {
        String type = "查询城市-->";
        NetworkApi.createService(ApiService.class, ApiType.SEARCH).searchCity(cityName,
                        isExact ? Constant.EXACT : Constant.FUZZY)
                .compose(NetworkApi.applySchedulers(new BaseObserver<SearchCityResponse>() {
                    @Override
                    public void onSuccess(SearchCityResponse searchCityResponse) {
                        if (searchCityResponse == null) {
                            failed.postValue("搜索城市数据为null，请检查城市名称是否正确。");
                            return;
                        }
                        if (Constant.SUCCESS.equals(searchCityResponse.getCode())) {
                            responseLiveData.postValue(searchCityResponse);
                        } else {
                            failed.postValue(type + searchCityResponse.getCode());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        failed.postValue(e.getMessage());
                    }
                }));
    }
}


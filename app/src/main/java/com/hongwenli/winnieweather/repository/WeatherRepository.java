package com.hongwenli.winnieweather.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.hongwenli.library.network.BaseObserver;
import com.hongwenli.library.network.api.ApiType;
import com.hongwenli.library.network.api.NetworkApi;
import com.hongwenli.winnieweather.Constant;
import com.hongwenli.winnieweather.api.ApiService;
import com.hongwenli.winnieweather.bean.AirResponse;
import com.hongwenli.winnieweather.bean.DailyResponse;
import com.hongwenli.winnieweather.bean.HourlyResponse;
import com.hongwenli.winnieweather.bean.LifestyleResponse;
import com.hongwenli.winnieweather.bean.NowResponse;

import okhttp3.Callback;
import retrofit2.Call;
import retrofit2.Response;

//请求接口拿到返回的数据再通过LiveData传递出去，
// 这里我在请求失败的时候加了一个type，这样我们就可以很清楚的知道是那个接口有问题，
// 那么同样需要修改一下SearchCityRepository类中searchCity()方法
@SuppressLint("CheckResult")
public class WeatherRepository {
    private static final class WeatherRepositoryHolder {
        private static final WeatherRepository mInstance = new WeatherRepository();
    }
    public static WeatherRepository getInstance() {
        return WeatherRepositoryHolder.mInstance;
    }
    private static final String TAG = WeatherRepository.class.getSimpleName();
    /**
     * 实况天气
     *
     * @param responseLiveData 成功数据
     * @param failed           错误信息
     * @param cityId           城市ID
     */
    public void nowWeather(MutableLiveData<NowResponse> responseLiveData,
                           MutableLiveData<String> failed, String cityId) {
        String type = "实时天气-->";
        NetworkApi.createService(ApiService.class, ApiType.WEATHER).nowWeather(cityId)
                .compose(NetworkApi.applySchedulers(new BaseObserver<NowResponse>() {
                    @Override
                    public void onSuccess(NowResponse nowResponse) {
                        if (nowResponse == null) {
                            failed.postValue("实况天气数据为null，请检查城市ID是否正确。");
                            return;
                        }
                        if (Constant.SUCCESS.equals(nowResponse.getCode())) {
                            responseLiveData.postValue(nowResponse);
                        } else {
                            failed.postValue(type + nowResponse.getCode());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        failed.postValue(type + e.getMessage());
                    }
                }));
    }
    /**七天的天气预报*/
    public void dailyWeather(MutableLiveData<DailyResponse> responseLiveData,
                             MutableLiveData<String> failed, String cityId) {
        String type = "天气预报-->";
        NetworkApi.createService(ApiService.class, ApiType.WEATHER).dailyWeather(cityId)
                .compose(NetworkApi.applySchedulers(new BaseObserver<DailyResponse>() {
                    @Override
                    public void onSuccess(DailyResponse dailyResponse) {
                        if (dailyResponse == null) {
                            failed.postValue("天气预报数据为null，请检查城市ID是否正确。");
                            return;
                        }
                        if (Constant.SUCCESS.equals(dailyResponse.getCode())) {
                            responseLiveData.postValue(dailyResponse);
                        } else {
                            failed.postValue(type + dailyResponse.getCode());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        failed.postValue(type + e.getMessage());
                    }
                }));
    }
    //生活指数
    public void lifestyle(MutableLiveData<LifestyleResponse> responseLiveData,
                          MutableLiveData<String> failed, String cityId) {
        String type = "生活指数-->";
        NetworkApi.createService(ApiService.class, ApiType.WEATHER).lifestyle("1,2,3,4,5,6,7,8,9", cityId)
                .compose(NetworkApi.applySchedulers(new BaseObserver<LifestyleResponse>() {
                    @Override
                    public void onSuccess(LifestyleResponse lifestyleResponse) {
                        if (lifestyleResponse == null) {
                            failed.postValue("生活指数数据为null，请检查城市ID是否正确。");
                            return;
                        }
                        //请求接口成功返回数据，失败返回状态码
                        if (Constant.SUCCESS.equals(lifestyleResponse.getCode())) {
                            responseLiveData.postValue(lifestyleResponse);
                        } else {
                            failed.postValue(type + lifestyleResponse.getCode());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        failed.postValue(type + e.getMessage());
                    }
                }));
    }
    //逐小时天气
    public void hourlyWeather(MutableLiveData<HourlyResponse> responseLiveData,
                              MutableLiveData<String> failed, String cityId) {
        String type = "逐小时天气预报-->";
        NetworkApi.createService(ApiService.class, ApiType.WEATHER).hourlyWeather(cityId)
                .compose(NetworkApi.applySchedulers(new BaseObserver<HourlyResponse>() {
                    @Override
                    public void onSuccess(HourlyResponse hourlyResponse) {
                        if (hourlyResponse == null) {
                            failed.postValue("逐小时天气预报数据为null，请检查城市ID是否正确。");
                            return;
                        }
                        //请求接口成功返回数据，失败返回状态码
                        if (Constant.SUCCESS.equals(hourlyResponse.getCode())) {
                            responseLiveData.postValue(hourlyResponse);
                        } else {
                            failed.postValue(type + hourlyResponse.getCode());
                        }
                    }
                    @Override
                    public void onFailure(Throwable e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        failed.postValue(type + e.getMessage());
                    }
                }));
    }
    /**
     * 空气质量天气预报
     *
     * @param responseLiveData 成功数据
     * @param failed           错误信息
     * @param cityId           城市ID
     */
    public void airWeather(MutableLiveData<AirResponse> responseLiveData,
                           MutableLiveData<String> failed, String cityId) {
        String type = "空气质量天气预报-->";
        NetworkApi.createService(ApiService.class, ApiType.WEATHER).airWeather(cityId)
                .compose(NetworkApi.applySchedulers(new BaseObserver<AirResponse>() {
                    @Override
                    public void onSuccess(AirResponse airResponse) {
                        if (airResponse == null) {
                            failed.postValue("空气质量预报数据为null，请检查城市ID是否正确。");
                            return;
                        }
                        //请求接口成功返回数据，失败返回状态码
                        if (Constant.SUCCESS.equals(airResponse.getCode())) {
                            responseLiveData.postValue(airResponse);
                        } else {
                            failed.postValue(type + airResponse.getCode());
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        Log.e(TAG, "onFailure: " + e.getMessage());
                        failed.postValue(type + e.getMessage());
                    }
                }));
//        NetworkApi.createService(ApiService.class, ApiType.WEATHER)
//                .dailyWeather("15d", location)
//                .enqueue(new Callback<DailyResponse>() {
//                    @Override
//                    public void onResponse(Call<DailyResponse> call, Response<DailyResponse> response) {
//                        if (response.isSuccessful()) {
//                            DailyResponse dailyResponse = response.body();
//                            // 在这里处理返回的 dailyResponse
//                        } else {
//                            // 请求失败，处理失败情况
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<DailyResponse> call, Throwable t) {
//                        // 请求失败，处理异常情况
//                    }
//                });

    }


}

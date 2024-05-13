package com.hongwenli.winnieweather.api;

import static com.hongwenli.winnieweather.Constant.API_KEY;

import com.hongwenli.winnieweather.bean.AirResponse;
import com.hongwenli.winnieweather.bean.DailyResponse;
import com.hongwenli.winnieweather.bean.HourlyResponse;
import com.hongwenli.winnieweather.bean.LifestyleResponse;
import com.hongwenli.winnieweather.bean.NowResponse;
import com.hongwenli.winnieweather.bean.SearchCityResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * 搜索城市  模糊搜索，国内范围 返回10条数据
     *
     * @param location 城市名
     * @param mode     exact 精准搜索  fuzzy 模糊搜索
     * @return NewSearchCityResponse 搜索城市数据返回
     */
    @GET("/v2/city/lookup?key=" + API_KEY + "&range=cn")
    Observable<SearchCityResponse> searchCity(@Query("location") String location,
                                              @Query("mode") String mode);
    /**实况天气*/
    @GET("/v7/weather/now?key=" + API_KEY)
    Observable<NowResponse> nowWeather(@Query("location") String location);

    /**获取七天的天气*/
    @GET("/v7/weather/7d?key=" + API_KEY)
    Observable<DailyResponse> dailyWeather(@Query("path")String type,@Query("location") String location);
    @GET("/v7/weather/7d?key=" + API_KEY)
    Observable<DailyResponse> dailyWeather(@Query("location") String location);


    /**获取生活指数*/
    @GET("/v7/indices/1d?key=" + API_KEY)
    Observable<LifestyleResponse> lifestyle(@Query("type") String type, @Query("location") String location);

    /**逐小时响应一个数据*/
    @GET("/v7/weather/24h?key=" + API_KEY)
    Observable<HourlyResponse> hourlyWeather(@Query("location") String location);

    /**空气质量*/
    @GET("/v7/air/now?key=" + API_KEY)
    Observable<AirResponse> airWeather(@Query("location") String location);

}


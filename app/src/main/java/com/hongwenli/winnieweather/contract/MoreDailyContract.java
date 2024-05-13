package com.hongwenli.winnieweather.contract;

import com.hongwenli.library.base.BaseView;
import com.hongwenli.library.net.NetCallBack;
import com.hongwenli.library.net.ServiceGenerator;
import com.hongwenli.winnieweather.api.ApiService;
import com.hongwenli.winnieweather.bean.DailyResponse;
import com.hongwenli.library.base.BasePresenter;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 更多天气预报订阅器
 */
public class MoreDailyContract {

    public static class MoreDailyPresenter extends BasePresenter<IMoreDailyView> {

        /**
         * 更多天气预报  V7
         * @param location  城市id
         */
        public void worldCity(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class,3);
            Observable<DailyResponse> dailyResponseObservable = service.dailyWeather("15d",location);
            dailyResponseObservable.subscribeOn(Schedulers.io()) // 在 IO 线程执行网络请求
                    .observeOn(AndroidSchedulers.mainThread()) // 在主线程处理响应
                    .subscribe(new Observer<DailyResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            // 这里可以处理订阅时的逻辑
                        }

                        @Override
                        public void onNext(DailyResponse dailyResponse) {
                            // 这里处理网络请求成功的逻辑
                            if (getView() != null) {
                                Response<DailyResponse> response = Response.success(dailyResponse);
                                getView().getMoreDailyResult(response);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            // 这里处理网络请求失败的逻辑
                            if (getView() != null) {
                                getView().getDataFailed();
                            }
                        }

                        @Override
                        public void onComplete() {
                            // 这里处理网络请求完成的逻辑
                        }
                    });
        }

    }

    public interface IMoreDailyView extends BaseView {

        //更多天气预报返回数据 V7
        void getMoreDailyResult(Response<DailyResponse> response);

        //错误返回
        void getDataFailed();
    }
}


package com.hongwenli.winnieweather.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.hongwenli.library.mvp.MvpActivity;
import com.hongwenli.winnieweather.R;
import com.hongwenli.winnieweather.bean.DailyResponse;
import com.hongwenli.winnieweather.contract.MoreDailyContract;
import com.hongwenli.winnieweather.ui.adapter.MoreDailyAdapter;
import com.hongwenli.winnieweather.utils.CodeToStringUtils;
import com.hongwenli.winnieweather.utils.Constant;
import com.hongwenli.winnieweather.utils.DateUtils;
import com.hongwenli.winnieweather.utils.RecyclerViewScrollHelper;
import com.hongwenli.winnieweather.utils.StatusBarUtil;
import com.hongwenli.winnieweather.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Response;

/**
 * 更多天气预报
 */
public class MoreDailyActivity extends MvpActivity<MoreDailyContract.MoreDailyPresenter> implements MoreDailyContract.IMoreDailyView {

    Toolbar toolbar;
    TextView tvTitle;
    RecyclerView rv;

    List<DailyResponse.DailyBean> mList = new ArrayList<>();//数据实体
    MoreDailyAdapter mAdapter;//适配器

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_more_daily);
//        toolbar = findViewById(R.id.toolbar);
//        tvTitle = findViewById(R.id.tv_title);
//        rv = findViewById(R.id.rv);
//        StatusBarUtil.transparencyBar(this);//透明状态栏
//        showLoadingDialog();
//        initList();
//        Back(toolbar);
//    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initData(savedInstanceState);
//    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_daily);
        toolbar = findViewById(R.id.toolbar);
        tvTitle = findViewById(R.id.tv_title);
        rv = findViewById(R.id.rv);
        initData(savedInstanceState);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        StatusBarUtil.transparencyBar(this);//透明状态栏
        showLoadingDialog();
        initList();
//        Back(toolbar);
    }

    /**
     * 初始化列表
     */
    private void initList() {
        mAdapter = new MoreDailyAdapter(R.layout.item_more_daily_list, mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rv);//滚动对齐，使RecyclerView像ViewPage一样，一次滑动一项,居中
        rv.setAdapter(mAdapter);
        tvTitle.setText(getIntent().getStringExtra("cityName"));
        mPresent.worldCity(getIntent().getStringExtra("locationId"));
        System.out.println("成功初始化完列表");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_more_daily;
    }

    @Override
    protected MoreDailyContract.MoreDailyPresenter createPresent() {
        return new MoreDailyContract.MoreDailyPresenter();
    }

    /**
     * 更多预报天气返回值
     *
     * @param response
     */
    @Override
    public void getMoreDailyResult(Response<DailyResponse> response) {
        dismissLoadingDialog();
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<DailyResponse.DailyBean> data = response.body().getDaily();
            if (data != null && data.size() > 0) {//判空处理
                mList.clear();//添加数据之前先清除
                mList.addAll(data);//添加数据
                mAdapter.notifyDataSetChanged();//刷新列表

                for (int i = 0; i < data.size(); i++) {
                    if(data.get(i).getFxDate().equals(DateUtils.getNowDate())){
                        RecyclerViewScrollHelper.scrollToPosition(rv,i);//渲染完成后，定位到今天，因为和风天气预报有时候包括了昨天，有时候又不包括，搞得我很被动
                    }
                }

            } else {
                ToastUtils.showShortToast(this, "天气预报数据为空");
            }
        } else {//异常状态码返回
            ToastUtils.showShortToast(this, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 其他异常返回
     */
    @Override
    public void getDataFailed() {
        dismissLoadingDialog();
        ToastUtils.showShortToast(this, "更多天气数据获取异常");
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_more_daily); // 设置布局文件
//        toolbar = findViewById(R.id.toolbar);
//        tvTitle = findViewById(R.id.tv_title);
//        rv = findViewById(R.id.rv);
//        StatusBarUtil.transparencyBar(this);//透明状态栏
////        initBeforeView(savedInstanceState);
//        initData(savedInstanceState); // 调用初始化数据的方法
////        Back(toolbar);
//    }
//
//    @Override
//    public void initData(Bundle savedInstanceState) {
//        StatusBarUtil.transparencyBar(this);//透明状态栏
//        showLoadingDialog();
//        initList();
//    }
//
//    /**
//     * 初始化列表
//     */
//    private void initList() {
//        mAdapter = new MoreDailyAdapter(R.layout.item_more_daily_list, mList);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        rv.setLayoutManager(linearLayoutManager);
//        PagerSnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(rv);//滚动对齐，使RecyclerView像ViewPage一样，一次滑动一项,居中
//        rv.setAdapter(mAdapter);
//        tvTitle.setText(getIntent().getStringExtra("cityName"));
//        mPresent = createPresent();
//        mPresent.worldCity(getIntent().getStringExtra("locationId"));
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_more_daily;
//    }
//
//    @Override
//    protected MoreDailyContract.MoreDailyPresenter createPresent() {
//        return new MoreDailyContract.MoreDailyPresenter();
//    }
//
//    /**
//     * 更多预报天气返回值
//     *
//     * @param response
//     */
//    @Override
//    public void getMoreDailyResult(Response<DailyResponse> response) {
//        dismissLoadingDialog();
//        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
//            List<DailyResponse.DailyBean> data = response.body().getDaily();
//            if (data != null && data.size() > 0) {//判空处理
//                mList.clear();//添加数据之前先清除
//                mList.addAll(data);//添加数据
//                mAdapter.notifyDataSetChanged();//刷新列表
//
//                for (int i = 0; i < data.size(); i++) {
//                    if(data.get(i).getFxDate().equals(DateUtils.getNowDate())){
//                        RecyclerViewScrollHelper.scrollToPosition(rv,i);//渲染完成后，定位到今天，因为和风天气预报有时候包括了昨天，有时候又不包括，搞得我很被动
//                    }
//                }
//
//            } else {
//                ToastUtils.showShortToast(this, "天气预报数据为空");
//            }
//        } else {//异常状态码返回
//            ToastUtils.showShortToast(this, CodeToStringUtils.WeatherCode(response.body().getCode()));
//        }
//    }
//
//    /**
//     * 其他异常返回
//     */
//    @Override
//    public void getDataFailed() {
//        dismissLoadingDialog();
//        ToastUtils.showShortToast(this, "更多天气数据获取异常");
//    }
}


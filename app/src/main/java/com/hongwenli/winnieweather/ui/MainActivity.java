package com.hongwenli.winnieweather.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDLocation;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hongwenli.library.base.NetworkActivity;
import com.hongwenli.winnieweather.Constant;
import com.hongwenli.winnieweather.R;
import com.hongwenli.winnieweather.bean.AirResponse;
import com.hongwenli.winnieweather.bean.HourlyResponse;
import com.hongwenli.winnieweather.databinding.DialogDailyDetailBinding;
import com.hongwenli.winnieweather.databinding.DialogHourlyDetailBinding;
import com.hongwenli.winnieweather.location.GoodLocation;
import com.hongwenli.winnieweather.ui.adapter.DailyAdapter;
import com.hongwenli.winnieweather.ui.adapter.HourlyAdapter;
import com.hongwenli.winnieweather.ui.adapter.LifestyleAdapter;
import com.hongwenli.winnieweather.bean.DailyResponse;
import com.hongwenli.winnieweather.bean.LifestyleResponse;
import com.hongwenli.winnieweather.bean.LocationBean;
import com.hongwenli.winnieweather.bean.NowResponse;
import com.hongwenli.winnieweather.databinding.ActivityMainBinding;
import com.hongwenli.winnieweather.location.LocationCallback;
import com.hongwenli.winnieweather.utils.CityDialog;
import com.hongwenli.winnieweather.utils.EasyDate;
import com.hongwenli.winnieweather.utils.MVUtils;
import com.hongwenli.winnieweather.utils.ToastUtils;
import com.hongwenli.winnieweather.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends NetworkActivity<ActivityMainBinding>
        implements LocationCallback ,CityDialog.SelectedCityCallback,View.OnScrollChangeListener{

    //权限数组
    private final String[] permissions = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求权限意图
    private ActivityResultLauncher<String[]> requestPermissionIntent;
    private MainViewModel viewModel;

    //城市弹窗
    private CityDialog cityDialog;

    //定位
    private GoodLocation goodLocation;

    /**
     * 菜单是显示在ActionBar上面的，
     * ifRoom表示，如果空间足够就显示出来，那么我们一开始是不需要显示出来的，切换城市后显示出来，点击之后搜索城市返回数据时再隐藏。
     * */
    //菜单
            //需要动态修改菜单项，所以声明这个mMenu
    private Menu mMenu;
    //城市信息来源标识  0: 定位， 1: 切换城市
    private int cityFlag = 0;
    //城市名称，定位和切换城市都会重新赋值。
    private String mCityName;
    //是否正在刷新
    private boolean isRefresh;

    //逐小时响应对象的列表
    private final List<HourlyResponse.HourlyBean> hourlyBeanList = new ArrayList<>();
    //对应的适配器
    private final HourlyAdapter hourlyAdapter = new HourlyAdapter(hourlyBeanList);


    //让MainActivity知晓ManageCityActivity活动界面里选择的城市并进行一个展示
    //在点击item时将结果返回给MainActivity
    private ActivityResultLauncher<Intent> jumpActivityIntent;
//    private TextView tvMoreDaily = findViewById(R.id.tv_more_daily);//更多天气预报
//    private TextView tvMoreAir = findViewById(R.id.tv_more_air);//更多空气质量
    private TextView tvMoreDaily;
    /**
     * 注册意图
     */
    @Override
    public void onRegister() {
        //请求权限意图
        requestPermissionIntent = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            boolean fineLocation = Boolean.TRUE.equals(result.get(android.Manifest.permission.ACCESS_FINE_LOCATION));
            boolean writeStorage = Boolean.TRUE.equals(result.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
            if (fineLocation && writeStorage) {
                //权限已经获取到，开始定位
                startLocation();
            }
        });
        //跳转到ManageCityActivity
        //对页面返回传递的值进行判断处理
        jumpActivityIntent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                //获取上个页面返回的数据
                String city = result.getData().getStringExtra(Constant.CITY_RESULT);
                //检查返回的城市 , 如果返回的城市是当前定位城市，并且当前定位标志为0，则不需要请求
                if (city.equals(MVUtils.getString(Constant.LOCATION_CITY)) && cityFlag == 0) {
                    Log.d("TAG", "onRegister: 管理城市页面返回不需要进行天气查询");
                    return;
                }
                //反之就直接调用选中城市的方法进行城市天气搜索
                Log.d("TAG", "onRegister: 管理城市页面返回进行天气查询");
                selectedCity(city);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate() {
        //全屏
        setFullScreenImmersion();
        //初始化定位
        initLocation();
        //请求权限
        requestPermission();
        //初始化视图
        initView();
        //绑定viewmodel
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //获取城市数据
        viewModel.getAllCity();

        tvMoreDaily = findViewById(R.id.tv_more_daily);
        tvMoreDaily.setOnClickListener(v -> {
           goToMore(MoreDailyActivity.class);
        });
    }
    private String  locationId;
    /**
     * 数据观察
     */
    @Override
    protected void onObserveData() {
        if (viewModel != null) {
            //城市数据返回
            viewModel.searchCityResponseMutableLiveData.observe(this, searchCityResponse -> {
                List<LocationBean> location = (ArrayList<LocationBean>) searchCityResponse.getLocation();
                if (location != null && location.size() > 0) {
                    String id = location.get(0).getId();
                    locationId = id;
                    //根据cityFlag判断是否显示重新定位的标志
                    mMenu.findItem(R.id.item_relocation).setVisible(cityFlag==1);
//                    在搜索城市的返回中，判断当前是否在刷新
                    //检查正在刷新
                    if(isRefresh){
                        showMsg("刷新完成");
                        binding.layRefresh.setRefreshing(false);
                        isRefresh=false;
                    }
                    //获取到城市的ID
                    if (id != null) {
                        //通过城市ID查询城市实时天气
                        viewModel.nowWeather(id);
                        //根据ID查询多天天气预报
                        viewModel.dailyWeather(id);
                        //通过城市ID查询生活指数
                        viewModel.lifestyle(id);
                        //通过城市ID查询逐小时的天气
                        viewModel.hourlyWeather(id);
                        //通过ID查询空气质量
                        viewModel.airWeather(id);
                    }
                }
            });
            //实况天气返回
            viewModel.nowResponseMutableLiveData.observe(this, nowResponse -> {
                NowResponse.NowBean now = nowResponse.getNow();
                if (now != null) {
                    binding.tvWeatherInfo.setText(now.getText());
                    binding.tvTemp.setText(now.getTemp());
                    //修改更新时间的字符格式
                    String time = EasyDate.updateTime(nowResponse.getUpdateTime());
                    binding.tvUpdateTime.setText(String.format("最近更新时间：%s%s", EasyDate.showTimeInfo(time), time));
                    binding.tvUpdateTime.setText("最近更新时间：" + nowResponse.getUpdateTime());
                    binding.tvWindDirection.setText("风向     " + now.getWindDir());//风向
                    binding.tvWindPower.setText("风力     " + now.getWindScale() + "级");//风力
                    binding.wwBig.startRotate();//大风车开始转动
                    binding.wwSmall.startRotate();//小风车开始转动

                }
            });
            //天气预报返回
            viewModel.dailyResponseMutableLiveData.observe(this, dailyResponse -> {
                List<DailyResponse.DailyBean> daily = dailyResponse.getDaily();
                if (daily != null) {
                    if (dailyBeanList.size() > 0) {
                        dailyBeanList.clear();
                    }
                    dailyBeanList.addAll(daily);
                    dailyAdapter.notifyDataSetChanged();
                    //设置当天的最高温和最低温
                    binding.tvHeight.setText(String.format("%s℃", daily.get(0).getTempMax()));
                    binding.tvLow.setText(String.format(" / %s℃", daily.get(0).getTempMin()));

                }
            });
            //生活指数返回
            viewModel.lifestyleResponseMutableLiveData.observe(this, lifestyleResponse -> {
                List<LifestyleResponse.DailyBean> daily = lifestyleResponse.getDaily();
                if (daily != null) {
                    if (lifestyleList.size() > 0) {
                        lifestyleList.clear();
                    }
                    lifestyleList.addAll(daily);
                    lifestyleAdapter.notifyDataSetChanged();
                }
            });

            viewModel.cityMutableLiveData.observe(this, provinces -> {
                //城市弹窗初始化
                cityDialog = CityDialog.getInstance(MainActivity.this, provinces);
                cityDialog.setSelectedCityCallback(this);
            });
            //逐小时天气请求数据的返回
            viewModel.hourlyResponseMutableLiveData.observe(this, hourlyResponse -> {
                List<HourlyResponse.HourlyBean> hourly = hourlyResponse.getHourly();
                if (hourly != null) {
                    if (hourlyBeanList.size() > 0) {
                        hourlyBeanList.clear();
                    }
                    hourlyBeanList.addAll(hourly);
                    hourlyAdapter.notifyDataSetChanged();
                }
            });
            //空气质量响应
            viewModel.airResponseMutableLiveData.observe(this, airResponse -> {
                dismissLoadingDialog();//在空气质量完成响应之后就隐藏加载界面
                AirResponse.NowBean now = airResponse.getNow();
                if (now == null) return;
                binding.rpbAqi.setMaxProgress(300);//最大进度，用于计算
                binding.rpbAqi.setMinText("0");//设置显示最小值
                binding.rpbAqi.setMinTextSize(32f);
                binding.rpbAqi.setMaxText("300");//设置显示最大值
                binding.rpbAqi.setMaxTextSize(32f);
                binding.rpbAqi.setProgress(Float.parseFloat(now.getAqi()));//当前进度
                binding.rpbAqi.setArcBgColor(getColor(R.color.arc_bg_color));//圆弧的颜色
                binding.rpbAqi.setProgressColor(getColor(R.color.arc_progress_color));//进度圆弧的颜色
                binding.rpbAqi.setFirstText(now.getCategory());//空气质量描述 取值范围：优，良，轻度污染，中度污染，重度污染，严重污染
                binding.rpbAqi.setFirstTextSize(44f);//第一行文本的字体大小
                binding.rpbAqi.setSecondText(now.getAqi());//空气质量值
                binding.rpbAqi.setSecondTextSize(64f);//第二行文本的字体大小
                binding.rpbAqi.setMinText("0");
                binding.rpbAqi.setMinTextColor(getColor(R.color.arc_progress_color));

                binding.tvAirInfo.setText(String.format("空气%s", now.getCategory()));

                binding.tvPm10.setText(now.getPm10());//PM10
                binding.tvPm25.setText(now.getPm2p5());//PM2.5
                binding.tvNo2.setText(now.getNo2());//二氧化氮
                binding.tvSo2.setText(now.getSo2());//二氧化硫
                binding.tvO3.setText(now.getO3());//臭氧
                binding.tvCo.setText(now.getCo());//一氧化碳
            });


            //错误信息返回
            viewModel.failed.observe(this, this::showLongMsg);
        }
    }


    /**
     * 请求权限
     */
    private void requestPermission() {
        //因为项目的最低版本API是23，所以肯定需要动态请求危险权限，只需要判断权限是否拥有即可
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //开始权限请求
            requestPermissionIntent.launch(permissions);
            return;
        }
        //开始定位
        startLocation();
    }


    /**
     * 初始化定位
     */
    private void initLocation() {
        goodLocation = GoodLocation.getInstance(this);
        goodLocation.setCallback(this);
    }

    /**
     * 开始定位
     */
    private void startLocation() {
        cityFlag = 0;
        goodLocation.startLocation();
    }

    /**
     * 接收定位信息
     *
     * @param bdLocation 定位数据
     */
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        showLoadingDialog();//显示加载的界面
        double latitude = bdLocation.getLatitude();    //获取纬度信息
        double longitude = bdLocation.getLongitude();    //获取经度信息
        float radius = bdLocation.getRadius();    //获取定位精度，默认值为0.0f
        String coorType = bdLocation.getCoorType();
        //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
        int errorCode = bdLocation.getLocType();//161  表示网络定位结果
        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
        String addr = bdLocation.getAddrStr();    //获取详细地址信息
        String country = bdLocation.getCountry();    //获取国家
        String province = bdLocation.getProvince();    //获取省份
        String city = bdLocation.getCity();    //获取城市
        String district = bdLocation.getDistrict();    //获取区县
        String street = bdLocation.getStreet();    //获取街道信息
        String locationDescribe = bdLocation.getLocationDescribe();    //获取位置描述信息
        binding.tvCity.setText(district);//设置文本显示
        if (viewModel != null && district != null) {
            mCityName=district;
            //保存当前定位的城市名
            MVUtils.put(Constant.LOCATION_CITY,mCityName);
            //保存到我的城市数据表中
            viewModel.addMyCityData(mCityName);
            //显示当前定位城市
            binding.tvCity.setText(district);
            //搜索城市
            viewModel.searchCity(district,false);
        } else {
            Log.e("TAG", "district: " + district);
        }
    }


    private final List<DailyResponse.DailyBean> dailyBeanList = new ArrayList<>();
    private final DailyAdapter dailyAdapter = new DailyAdapter(dailyBeanList);
    private final List<LifestyleResponse.DailyBean> lifestyleList = new ArrayList<>();
    private final LifestyleAdapter lifestyleAdapter = new LifestyleAdapter(lifestyleList);
    private void initView() {
        //自定义ToolBar图标
        setToolbarMoreIconCustom(binding.tvTitleMain);
        //天气预报列表
        binding.rvDaily.setLayoutManager(new LinearLayoutManager(this));
        dailyAdapter.setOnClickItemCallback(position ->
                showDailyDetailDialog(dailyBeanList.get(position)));
        binding.rvDaily.setAdapter(dailyAdapter);
        binding.rvLifestyle.setLayoutManager(new LinearLayoutManager(this));
        binding.rvLifestyle.setAdapter(lifestyleAdapter);
        //逐小时天气预报列表
        //设置了横向滑动
        LinearLayoutManager hourlyLayoutManager = new LinearLayoutManager(this);
        hourlyLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvHourly.setLayoutManager(hourlyLayoutManager);
        hourlyAdapter.setOnClickItemCallback(position -> showHourlyDetailDialog(hourlyBeanList.get(position)));
        binding.rvHourly.setAdapter(hourlyAdapter);

        //下拉刷新监听
        binding.layRefresh.setOnRefreshListener(() -> {
            if (mCityName == null) {
                binding.layRefresh.setRefreshing(false);
                return;
            }
            //设置正在刷新
            isRefresh = true;
            //搜索城市
            viewModel.searchCity(mCityName,false);
        });
        //滑动监听
        binding.scrollView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                //getMeasuredHeight() 表示控件的绘制高度
                if (scrollY > binding.layScrollHeight.getMeasuredHeight()) {
                    binding.tvTitleMain.setTitle((mCityName == null ? "城市天气" : mCityName));
                }
            } else if (scrollY < oldScrollY) {
                if (scrollY < binding.layScrollHeight.getMeasuredHeight()) {
                    //改回原来的
                    binding.tvTitleMain.setTitle("城市天气");
                }
            }
        });

        binding.scrollView.setOnScrollChangeListener(this);
    }

    public void setToolbarMoreIconCustom(Toolbar toolbar) {
        if (toolbar == null) return;
        toolbar.setTitle("城市天气");
        Drawable moreIcon = ContextCompat.getDrawable(toolbar.getContext(), R.drawable.ic_round_add_32);
        if (moreIcon != null) toolbar.setOverflowIcon(moreIcon);
        setSupportActionBar(toolbar);

    }


    //创建菜单的，已经菜单中点击Item事件的处理
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;
        //根据cityFlag设置重新定位菜单项是否显示
        mMenu.findItem(R.id.item_relocation).setVisible(cityFlag == 1);
        return true;
    }

    //选择不同的Item就跳转到不同的界面
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId==R.id.item_switching_cities){
            if (cityDialog != null) cityDialog.show();
        }else if(itemId==R.id.item_relocation){
            startLocation();//点击重新定位item时，再次定位一下。
        }else if(itemId==R.id.item_manage_city){//管理城市
            //跳转页面
            jumpActivityIntent.launch(new Intent(mContext, ManageCityActivity.class));
        }
        return true;
    }

    @Override
    public void selectedCity(String cityName) {
        cityFlag = 1;//切换城市
        mCityName = cityName;
        //搜索城市
        viewModel.searchCity(cityName,false);
        //显示所选城市
        binding.tvCity.setText(cityName);
    }

//    展示每天的详情信息
    private void showDailyDetailDialog(DailyResponse.DailyBean dailyBean) {
        BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
        DialogDailyDetailBinding detailBinding = DialogDailyDetailBinding.inflate(LayoutInflater.from(MainActivity.this), null, false);
        //关闭弹窗
        detailBinding.ivClose.setOnClickListener(v -> dialog.dismiss());
        //设置数据显示
        detailBinding.toolbarDaily.setTitle(String.format("%s   %s", dailyBean.getFxDate(), EasyDate.getWeek(dailyBean.getFxDate())));
        detailBinding.toolbarDaily.setSubtitle("天气预报详情");
        detailBinding.tvTmpMax.setText(String.format("%s℃", dailyBean.getTempMax()));
        detailBinding.tvTmpMin.setText(String.format("%s℃", dailyBean.getTempMin()));
        detailBinding.tvUvIndex.setText(dailyBean.getUvIndex());
        detailBinding.tvCondTxtD.setText(dailyBean.getTextDay());
        detailBinding.tvCondTxtN.setText(dailyBean.getTextNight());
        detailBinding.tvWindDeg.setText(String.format("%s°", dailyBean.getWind360Day()));
        detailBinding.tvWindDir.setText(dailyBean.getWindDirDay());
        detailBinding.tvWindSc.setText(String.format("%s级", dailyBean.getWindScaleDay()));
        detailBinding.tvWindSpd.setText(String.format("%s公里/小时", dailyBean.getWindSpeedDay()));
        detailBinding.tvCloud.setText(String.format("%s%%", dailyBean.getCloud()));
        detailBinding.tvHum.setText(String.format("%s%%", dailyBean.getHumidity()));
        detailBinding.tvPres.setText(String.format("%shPa", dailyBean.getPressure()));
        detailBinding.tvPcpn.setText(String.format("%smm", dailyBean.getPrecip()));
        detailBinding.tvVis.setText(String.format("%skm", dailyBean.getVis()));
        dialog.setContentView(detailBinding.getRoot());
        dialog.show();
    }

    //显示逐小时的详细信息
    private void showHourlyDetailDialog(HourlyResponse.HourlyBean hourlyBean) {
        BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);
        DialogHourlyDetailBinding detailBinding = DialogHourlyDetailBinding.inflate(LayoutInflater.from(MainActivity.this), null, false);
        //关闭弹窗
        detailBinding.ivClose.setOnClickListener(v -> dialog.dismiss());
        //设置数据显示
        String time = EasyDate.updateTime(hourlyBean.getFxTime());
        detailBinding.toolbarHourly.setTitle(EasyDate.showTimeInfo(time) + time);
        detailBinding.toolbarHourly.setSubtitle("逐小时预报详情");
        detailBinding.tvTmp.setText(String.format("%s℃", hourlyBean.getTemp()));
        detailBinding.tvCondTxt.setText(hourlyBean.getText());
        detailBinding.tvWindDeg.setText(String.format("%s°", hourlyBean.getWind360()));
        detailBinding.tvWindDir.setText(hourlyBean.getWindDir());
        detailBinding.tvWindSc.setText(String.format("%s级", hourlyBean.getWindScale()));
        detailBinding.tvWindSpd.setText(String.format("公里/小时%s", hourlyBean.getWindSpeed()));
        detailBinding.tvHum.setText(String.format("%s%%", hourlyBean.getHumidity()));
        detailBinding.tvPres.setText(String.format("%shPa", hourlyBean.getPressure()));
        detailBinding.tvPop.setText(String.format("%s%%", hourlyBean.getPop()));
        detailBinding.tvDew.setText(String.format("%s℃", hourlyBean.getDew()));
        detailBinding.tvCloud.setText(String.format("%s%%", hourlyBean.getCloud()));
        dialog.setContentView(detailBinding.getRoot());
        dialog.show();
    }

    /**
     * 滑动监听
     * @param v The view whose scroll position has changed.
     * @param scrollX Current horizontal scroll origin.
     * @param scrollY Current vertical scroll origin.
     * @param oldScrollX Previous horizontal scroll origin.
     * @param oldScrollY Previous vertical scroll origin.
     */
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY > oldScrollY) {
            Log.e("onScroll", "上滑");
            //laySlideArea.getMeasuredHeight() 表示控件的绘制高度
            if(scrollY > binding.laySlideArea.getMeasuredHeight()){
                String tx = binding.tvCity.getText().toString();
                if(tx.contains("定位中")){//因为存在网络异常问题，总不能你没有城市，还给你改变UI吧
                    binding.tvTitleMain.setTitle("城市天气");
                }else {
                    binding.tvTitleMain.setTitle(tx);//改变TextView的显示文本
                }
            }
        }
        if (scrollY < oldScrollY) {
            Log.e("onScroll", "下滑");
            if(scrollY < binding.laySlideArea.getMeasuredHeight()){
                binding.tvTitleMain.setTitle("城市天气");//改回原来的
            }
        }
    }
    /**
     * 进入更多数据页面
     * @param clazz  要进入的页面
     */
    private void goToMore(Class<?> clazz) {
        if (locationId == null) {
            ToastUtils.showShortToast(this, "很抱歉，未获取到相关更多信息");
            System.out.println("跳转失败");
        } else {
            System.out.println("成功跳转！！！");
            Intent intent = new Intent(this, clazz);
            intent.putExtra("locationId", locationId);
            intent.putExtra("cityName", binding.tvCity.getText().toString());
            startActivity(intent);
        }

    }

}

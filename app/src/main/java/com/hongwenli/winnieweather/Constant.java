package com.hongwenli.winnieweather;

public class Constant {//存储常量

    /*
    * 和风天气的key
    * */
    public final static String API_KEY="2550993cb78d4aa3b86e26e2e513e81a";
    /**
     * 和风天气接口请求成功状态码
     */
    public static final String SUCCESS = "200";

    /**
     * 搜索类型：精准搜索
     */
    public static final String EXACT = "exact";

    /**
     * 搜索类型：模糊搜索
     */
    public static final String FUZZY = "fuzzy";
    /**
     * 程序第一次运行
     */
    public static final String FIRST_RUN = "firstRun";

    /**
     * 今天第一次启动时间
     */
    public static final String FIRST_STARTUP_TIME_TODAY = "firstStartupTimeToday";
    /**
     * 当前定位城市
     */
    public static final String LOCATION_CITY = "locationCity";

    /**
     * 页面返回城市结果
     */
    public static final String CITY_RESULT = "cityResult";
    /**
     * 推荐城市数组
     */
    public static final String[] CITY_ARRAY = {"北京", "上海", "广州", "深圳", "天津", "武汉", "长沙", "重庆", "沈阳", "杭州",
            "南京", "沈阳", "哈尔滨", "长春", "呼和浩特", "石家庄", "银川", "乌鲁木齐", "呼和浩特", "拉萨", "西宁", "西安", "兰州", "太原",
            "昆明", "南宁", "成都", "济南", "南昌", "厦门", "扬州", "岳阳", "赣州", "苏州", "福州", "贵阳", "桂林", "海口", "三亚", "香港",
            "澳门", "台北"};


}

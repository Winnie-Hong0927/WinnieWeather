package com.hongwenli.library;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityManager {//用于管理所有的Activity
    //保存所有创建的Activity
    private final List<Activity> allActivities = new ArrayList<>();

    /**
     * 添加Activity到管理器
     *
     * @param activity activity
     */
    public void addActivity(Activity activity) {
        if (activity != null) {
            allActivities.add(activity);
        }
    }


    /**
     * 从管理器移除Activity
     *
     * @param activity activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            allActivities.remove(activity);
        }
    }

    /**
     * 关闭所有Activity
     */
    public void finishAll() {
        for (Activity activity : allActivities) {
            activity.finish();
        }

    }
    public Activity getTaskTop() {
        return allActivities.get(allActivities.size() - 1);
    }
}

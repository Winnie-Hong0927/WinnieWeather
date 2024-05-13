package com.hongwenli.library.base;

import android.os.Bundle;

public interface UiCallBack {
    //初始化savedInstanceState
    void initBeforeView(Bundle savedInstanceState);

    //初始化
    void initData(Bundle savedInstanceState);

    //布局
    int getLayoutId();
}

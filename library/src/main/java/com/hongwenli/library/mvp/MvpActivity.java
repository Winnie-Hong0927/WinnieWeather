package com.hongwenli.library.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.hongwenli.library.base.BaseActivity;
import com.hongwenli.library.base.BasePresenter;
import com.hongwenli.library.base.BaseView;

/**
 * 适用于需要访问网络接口的Activity
 *
 * @author llw
 */

public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {
    protected P mPresent;

    @Override
    public void initBeforeView(Bundle savedInstanceState) {
        mPresent = createPresent();
        mPresent.attach((BaseView) this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresent = createPresent();
        mPresent.attach((BaseView) this);
    }

    protected abstract P createPresent();

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

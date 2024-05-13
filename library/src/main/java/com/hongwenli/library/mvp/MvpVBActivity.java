package com.hongwenli.library.mvp;

import android.os.Bundle;

import androidx.viewbinding.ViewBinding;

import com.hongwenli.library.base.BasePresenter;
import com.hongwenli.library.base.BaseVBActivity;
import com.hongwenli.library.base.BaseView;


/**
 * 适用于需要访问网络接口的Activity
 *
 * @author llw
 */

public abstract class MvpVBActivity<T extends ViewBinding, P extends BasePresenter> extends BaseVBActivity<T> {

    protected P mPresent;

    @Override
    public void initBeforeView(Bundle savedInstanceState) {
        mPresent = createPresent();
        mPresent.attach((BaseView) this);
    }

    protected abstract P createPresent();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresent.detach((BaseView) this);
    }

}

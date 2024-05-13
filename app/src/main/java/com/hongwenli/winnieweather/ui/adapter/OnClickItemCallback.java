package com.hongwenli.winnieweather.ui.adapter;

/**
 * 查看详情的话就可以通过点击列表的item来触发，首先我们需要让列表能够监听点击事件。
 * */
public interface OnClickItemCallback {

    /**
     * item点击
     * @param position 点击位置
     */
    void onItemClick(int position);
}


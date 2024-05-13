package com.hongwenli.winnieweather.music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hongwenli.winnieweather.R;

import java.util.List;


public class MusicAdapter extends BaseAdapter {
    private List<String> mList;
    private Context mContext;
    public MusicAdapter(Context mContext,List<String> mList){
        this.mContext = mContext;
        this.mList = mList;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_music, null);
        TextView tv = v.findViewById(R.id.tv_music);
        tv.setText(mList.get(position));
        return v;
    }
}

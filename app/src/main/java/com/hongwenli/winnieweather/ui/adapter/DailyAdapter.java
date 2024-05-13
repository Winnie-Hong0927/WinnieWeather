package com.hongwenli.winnieweather.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hongwenli.winnieweather.bean.DailyResponse;
import com.hongwenli.winnieweather.databinding.ItemDailyRvBinding;
import com.hongwenli.winnieweather.utils.EasyDate;
import com.hongwenli.winnieweather.utils.WeatherUtil;

import java.util.List;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.ViewHolder> {
    private final List<DailyResponse.DailyBean> dailyBeans;
    public DailyAdapter(List<DailyResponse.DailyBean> dailyBeans) {
        this.dailyBeans = dailyBeans;
    }
    //添加了天气预报列表的item视图点击监听，实现了天气列表的点击监听
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDailyRvBinding binding = ItemDailyRvBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        ViewHolder holder = new ViewHolder(binding);
        //添加点击回调
        binding.getRoot().setOnClickListener(v -> {
            if (onClickItemCallback != null) {
                onClickItemCallback.onItemClick(holder.getAdapterPosition());
            }
        });
        return holder;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyResponse.DailyBean dailyBean = dailyBeans.get(position);
        holder.binding.tvDate.setText(EasyDate.dateSplit(dailyBean.getFxDate())
                + EasyDate.getDayInfo(dailyBean.getFxDate()));
        WeatherUtil.changeIcon(holder.binding.ivStatus, Integer.parseInt(dailyBean.getIconDay()));
        holder.binding.tvHeight.setText(dailyBean.getTempMax() + "℃");
        holder.binding.tvLow.setText(" / " + dailyBean.getTempMin() + "℃");
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ItemDailyRvBinding binding;

        public ViewHolder(@NonNull ItemDailyRvBinding itemTextRvBinding) {
            super(itemTextRvBinding.getRoot());
            binding = itemTextRvBinding;
        }
    }
    private OnClickItemCallback onClickItemCallback;
    public void setOnClickItemCallback(OnClickItemCallback onClickItemCallback) {
        this.onClickItemCallback = onClickItemCallback;
    }
    @Override
    public int getItemCount() {
        return dailyBeans.size();
    }
}


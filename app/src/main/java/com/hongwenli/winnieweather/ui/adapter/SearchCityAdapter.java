package com.hongwenli.winnieweather.ui.adapter;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hongwenli.winnieweather.R;
import com.hongwenli.winnieweather.WeatherApp;
import com.hongwenli.winnieweather.bean.LocationBean;
import com.hongwenli.winnieweather.databinding.ItemSearchCityRvBinding;

import java.util.List;

public class SearchCityAdapter extends RecyclerView.Adapter<SearchCityAdapter.ViewHolder> {

    private final List<LocationBean> beans;

    private OnClickItemCallback onClickItemCallback;//视图点击

    //关键字
    private String targetStr;

    public SearchCityAdapter(List<LocationBean> cities) {
        this.beans = cities;
    }

    public void setOnClickItemCallback(OnClickItemCallback onClickItemCallback) {
        this.onClickItemCallback = onClickItemCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchCityRvBinding binding = ItemSearchCityRvBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ViewHolder viewHolder = new ViewHolder(binding);
        //添加视图点击事件
        binding.getRoot().setOnClickListener(v -> {
            if (onClickItemCallback != null) {
                onClickItemCallback.onItemClick(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationBean bean = beans.get(position);
        String result = bean.getName() + " , " + bean.getAdm2() + " , " + bean.getAdm1() + " , " + bean.getCountry();
        if (targetStr != null && targetStr.length() > 0) {
            holder.binding.tvCityName.setText(matcherSearchText(result, targetStr));
        } else {
            holder.binding.tvCityName.setText(result);
        }
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    /**
     * 改变颜色
     *
     * @param content 输入的文本
     */
    public void changTxColor(String content) {
        targetStr = content;
        notifyDataSetChanged();
    }


    /**
     * 改变一段文本中第一个关键字的文字颜色
     *
     * @param string  文本字符串
     * @param keyWord 关键字
     * @return
     */

    public static CharSequence matcherSearchText(String string, String keyWord) {
        //SpannableStringBuilder ，通过这个可以设置一行文字多种颜色，其他的地方都是基本使用。
        SpannableStringBuilder builder = new SpannableStringBuilder(string);
        int indexOf = string.indexOf(keyWord);
        if (indexOf != -1) {
            builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(WeatherApp.getContext(), R.color.yellow)), indexOf, indexOf + keyWord.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ItemSearchCityRvBinding binding;

        public ViewHolder(@NonNull ItemSearchCityRvBinding itemSearchCityRvBinding) {
            super(itemSearchCityRvBinding.getRoot());
            binding = itemSearchCityRvBinding;
        }
    }
}



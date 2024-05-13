package com.hongwenli.winnieweather.personalCenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.hongwenli.winnieweather.R;
import com.hongwenli.winnieweather.personalCenter.bean.Person;
import com.hongwenli.winnieweather.personalCenter.db.DatabaseHelper;

import java.util.List;

public class AccountAdapter extends BaseAdapter {
    private List<Person> personList ;
    private Context mContext;
    private DatabaseHelper mHelper;
    public AccountAdapter(Context context){
        mContext = context;
        mHelper = DatabaseHelper.getInstance(mContext);
        mHelper.openReadLink();
        this.personList = mHelper.getAllUsers();
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return personList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_account, null);
        }
        TextView tvUsername = view.findViewById(R.id.tv_account_username);
        TextView tvName = view.findViewById(R.id.tv_account_name);
        TextView etDesc = view.findViewById(R.id.tv_account_desc);
        Person person = personList.get(position);
        tvUsername.setText(person.getUsername());
        tvName.setText(person.getName());
        etDesc.setText(person.getDesc());
        return view;
    }

}

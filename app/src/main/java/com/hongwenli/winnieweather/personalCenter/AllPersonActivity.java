package com.hongwenli.winnieweather.personalCenter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hongwenli.winnieweather.R;
import com.hongwenli.winnieweather.personalCenter.adapter.AccountAdapter;
import com.hongwenli.winnieweather.utils.ToastUtils;

public class AllPersonActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView lvAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_person);
        lvAccount = findViewById(R.id.lv_account);
        AccountAdapter adapter = new AccountAdapter(this);
        lvAccount.setAdapter(adapter);
        lvAccount.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtils.showShortToast(this,"点击了"+position);

    }
}
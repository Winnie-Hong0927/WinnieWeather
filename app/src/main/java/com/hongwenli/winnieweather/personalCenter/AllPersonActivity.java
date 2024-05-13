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
import androidx.room.util.DBUtil;

import com.hongwenli.winnieweather.R;
import com.hongwenli.winnieweather.personalCenter.adapter.AccountAdapter;
import com.hongwenli.winnieweather.personalCenter.bean.LoginedPerson;
import com.hongwenli.winnieweather.personalCenter.bean.Person;
import com.hongwenli.winnieweather.personalCenter.db.DatabaseHelper;
import com.hongwenli.winnieweather.utils.ToastUtils;

import java.util.List;

public class AllPersonActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView lvAccount;
    private DatabaseHelper mHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_person);
        lvAccount = findViewById(R.id.lv_account);
        AccountAdapter adapter = new AccountAdapter(this);
        lvAccount.setAdapter(adapter);
        lvAccount.setOnItemClickListener(this);
        mHelper = DatabaseHelper.getInstance(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<Person> allUsers = mHelper.getAllUsers();
        Person selectedPerson = allUsers.get(position);
        LoginedPerson.setLoginedPerson(selectedPerson);
        ToastUtils.showShortToast(this,"成功切换账号");
    }
}
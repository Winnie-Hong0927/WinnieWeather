package com.hongwenli.winnieweather.personalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hongwenli.winnieweather.R;
import com.hongwenli.winnieweather.personalCenter.bean.LoginedPerson;
import com.hongwenli.winnieweather.personalCenter.bean.Person;
import com.hongwenli.winnieweather.utils.ToastUtils;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {
    private Person loginedPerson;
    private TextView tvUsername;
    private TextView tvPassword;
    private TextView tvAge;
    private TextView tvName;
    private TextView tvDesc;
    private Button btnChange;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_person);
        loginedPerson = LoginedPerson.getLoginedPerson();
        if(loginedPerson.getUsername()==null){
            ToastUtils.showShortToast(this,"未登录，请先登录");
            startActivity(new Intent(PersonActivity.this,LoginActivity.class));
        }
        tvUsername = findViewById(R.id.tv_username);
        tvPassword = findViewById(R.id.tv_password);
        tvAge = findViewById(R.id.tv_age);
        tvName = findViewById(R.id.tv_name);
        tvDesc = findViewById(R.id.tv_desc);
        btnExit = findViewById(R.id.btn_exit);
        btnChange = findViewById(R.id.btn_change);

        tvUsername.setText(loginedPerson.getUsername());
        tvDesc.setText(loginedPerson.getDesc());
        tvPassword.setText(loginedPerson.getPassword());
        int age = loginedPerson.getAge();
        String ageStr = age+"";
        tvAge.setText(ageStr);
        tvName.setText(loginedPerson.getName());
        btnChange.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_change){
            Intent intent = new Intent(PersonActivity.this, AllPersonActivity.class);
            startActivity(intent);
        }else if(id == R.id.btn_exit){

        }
    }
}
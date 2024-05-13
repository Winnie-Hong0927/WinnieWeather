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
import com.hongwenli.winnieweather.personalCenter.bean.Person;

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
        Intent intent = getIntent();
        String username = intent.getStringExtra(Person.USERNAME);
        String password = intent.getStringExtra(Person.PASSWORD);
        String name = intent.getStringExtra(Person.NAME);
        String desc = intent.getStringExtra(Person.DESC);
        int age = intent.getIntExtra(Person.AGE,0);
        loginedPerson = new Person(username,name,age,desc,password);

        tvUsername = findViewById(R.id.tv_username);
        tvPassword = findViewById(R.id.tv_password);
        tvAge = findViewById(R.id.tv_age);
        tvName = findViewById(R.id.tv_name);
        tvDesc = findViewById(R.id.tv_desc);
        btnExit = findViewById(R.id.btn_exit);
        btnChange = findViewById(R.id.btn_change);

        tvUsername.setText(username);
        tvDesc.setText(desc);
        tvPassword.setText(password);
//        tvAge.setText(age);
        tvName.setText(name);
        btnChange.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
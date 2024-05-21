package com.hongwenli.winnieweather.personalCenter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hongwenli.winnieweather.R;
import com.hongwenli.winnieweather.personalCenter.bean.Person;
import com.hongwenli.winnieweather.personalCenter.db.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etUsername;
    private EditText etName;
    private EditText etPsw;
    private EditText etAge;
    private EditText etDesc;
    private Button btnConfirmRegister;
    private Button btnReturn;
    private DatabaseHelper mHelper;

    @Override
    protected void onStart() {
        super.onStart();
        mHelper = DatabaseHelper.getInstance(this);
        mHelper.openReadLink();
        mHelper.openWriteLink();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        etUsername = findViewById(R.id.et_username);
        etName = findViewById(R.id.et_name);
        etPsw = findViewById(R.id.et_psw);
        etAge = findViewById(R.id.et_age);
        etDesc = findViewById(R.id.et_desc);
        btnConfirmRegister = findViewById(R.id.btn_confirm_register);
        btnReturn = findViewById(R.id.btn_return);
        btnConfirmRegister.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Person person = createPerson();
        if(id == R.id.btn_confirm_register){
            register(person);
        }else if(id == R.id.btn_return){
            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }
    //注册
    private void register(Person person){
        if(isExited()){
            Toast.makeText(this,"用户已存在~~",Toast.LENGTH_LONG).show();
        }else{
            mHelper.insert(person);
            Toast.makeText(this,"注册成功!!",Toast.LENGTH_LONG).show();
        }
    }
    //根据用户名判断用户是否存在
    private boolean isExited(){
        boolean res = mHelper.checkUserExists(etUsername.getText().toString());
        return res;
    }

    private Person createPerson(){
        String username = etUsername.getText().toString();
        String name = etName.getText().toString();
        int age = Integer.parseInt(etAge.getText().toString());
        String desc = etDesc.getText()!=null?etDesc.getText().toString():null;
        String psw = etPsw.getText().toString();
        Person person = new Person(username,name,age,desc,psw);
        return person;
    }


}
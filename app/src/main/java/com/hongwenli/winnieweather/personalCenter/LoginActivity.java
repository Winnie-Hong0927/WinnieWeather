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
import com.hongwenli.winnieweather.personalCenter.bean.LoginedPerson;
import com.hongwenli.winnieweather.personalCenter.bean.Person;
import com.hongwenli.winnieweather.personalCenter.db.DatabaseHelper;
import com.hongwenli.winnieweather.ui.MainActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUsername ;
    private EditText etPsw;
    private Button btnLogin;
    private Button btnRegister;
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
        setContentView(R.layout.activity_login);
        etUsername = findViewById(R.id.et_login_username);
        etPsw = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_login){
            //判断是否存在该用户，如果存在就跳转到主界面，否则就提示用户先去注册
            boolean exists = mHelper.checkUserExists(etUsername.getText().toString(), etPsw.getText().toString());
            if(exists){
                //跳转到主界面//需要携带着这个用户
                Person person = mHelper.getPersonByUsername(etUsername.getText().toString());
                LoginedPerson.setLoginedPerson(person);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
//                Toast.makeText(this,"跳转到主界面",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
            }
        }else if(id == R.id.btn_register){
            //需要跳转到RegisterActivity中
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        }
    }

}
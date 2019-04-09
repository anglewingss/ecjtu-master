package com.ecjtu.assistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.app.MyApplication;
import com.ecjtu.assistant.db.StudentDb;

import java.util.List;


//
public class LoginActivity extends BaseActivity {
    private EditText tel;
    private EditText pw;
    StudentDb recordDb;

    @Override
    public void onDestroy() {
        super.onDestroy();
        recordDb.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //实例化DBHelper
        recordDb = StudentDb.getInstance();
        recordDb.open(this);
        setTitle("登录");

        tel = (EditText) findViewById(R.id.et_tel);
        pw = (EditText) findViewById(R.id.et_pw);

        // TODO: 2018/3/20
        tel.setText("123");
        pw.setText("123");

        setRightViewVisible(false);
        setLeftViewVisible(false);
        findViewById(R.id.go_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


    }

    private void login() {

        if (TextUtils.isEmpty(tel.getText().toString())) {
            showToast("请输入帐号!");
            return;
        } else if (TextUtils.isEmpty(pw.getText().toString())) {
            showToast("请输入密码!");
            return;
        }

        String telStr = tel.getText().toString();
        String pwStr = pw.getText().toString();
        List<StudentDb.Record> query = recordDb.query();
        int y = -1;
        for (int i = 0; i < query.size(); i++) {
            if (query.get(i).tel.equals(telStr)) {
                y = i;
                break;
            }
        }
        if (y != -1) {
            if (query.get(y).pw.equals(pwStr)) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                showToast("登录成功!");
                MyApplication.getInstance().student = query.get(y);
                finish();
            } else {
                showToast("密码错误!");

            }
        } else {
            showToast("帐号不存在,请去注册!");
        }

    }


}


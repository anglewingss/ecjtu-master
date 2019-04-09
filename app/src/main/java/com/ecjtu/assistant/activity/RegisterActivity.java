package com.ecjtu.assistant.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.db.StudentDb;


//
public class RegisterActivity extends BaseActivity {
    private TextView tel;
    private TextView pw;
    private TextView pw_again;
    StudentDb recordDb;

    @Override
    public void onDestroy() {
        super.onDestroy();
//        recordDb.close();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //实例化DBHelper
        recordDb = StudentDb.getInstance();
        recordDb.open(this);
        tel = (TextView) findViewById(R.id.et_tel);
        pw = (TextView) findViewById(R.id.et_pw);
        pw_again = (TextView) findViewById(R.id.et_pw_again);

        setRightViewVisible(false);
        setLeftViewVisible(false);
        setTitle("注册");

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });


    }

    private void register() {

        if (TextUtils.isEmpty(tel.getText().toString())) {
            showToast("请输入帐号!");
            return;
        } else if (TextUtils.isEmpty(pw.getText().toString())) {
            showToast("请输入密码!");
            return;
        } else if (TextUtils.isEmpty(pw_again.getText().toString())) {
            showToast("请确认密码!");
            return;
        } else if (!pw_again.getText().toString().equals(pw.getText().toString())) {
            showToast("俩次密码不一致!");
            return;
        }
        System.out.println("你好你好: "+recordDb.query(tel.getText().toString()));
        if(recordDb.query(tel.getText().toString()).size()==0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    StudentDb.Record note = new StudentDb.Record();
                    note.tel = tel.getText().toString();
                    note.pw = pw.getText().toString();
                    boolean insert = recordDb.insert(note);

                    showToast("注册成功!");
                    finish();

                }
            }, 100);
        }else{
            showToast("你已经注册过了");
            return;
        }

    }

}


package com.ecjtu.assistant.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.app.MyApplication;
import com.ecjtu.assistant.db.StudentDb;

public class ReviceActivity extends BaseActivity {

    private EditText old_pw;
    private EditText new_pw;
    private EditText new_pw_again;
    StudentDb recordDb;
    public StudentDb.Record student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revice);

        old_pw = (EditText)findViewById(R.id.old_pw);
        new_pw = (EditText)findViewById(R.id.new_pw);
        new_pw_again = (EditText)findViewById(R.id.new_pw_gain);

        recordDb = StudentDb.getInstance();
        recordDb.open(this);

        student = MyApplication.getInstance().student;


        setRightViewVisible(false);
        setLeftViewVisible(true);
        setTitle("修改密码");

        findViewById(R.id.revice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revice();
            }
        });
    }

    private void revice(){
        if (TextUtils.isEmpty(old_pw.getText().toString())) {
            showToast("请输入旧密码");
            return;
        } else if (TextUtils.isEmpty(new_pw.getText().toString())) {
            showToast("请输入新密码!");
            return;
        } else if (TextUtils.isEmpty(new_pw_again.getText().toString())) {
            showToast("请确认新密码!");
            return;
        } else if (!new_pw.getText().toString().equals(student.pw)) {
            showToast("旧密码输入不正确");
            return;
        }else if (new_pw.getText().toString().equals(old_pw.getText().toString())) {
            showToast("新密码与旧密码一致！");
            return;
        }else if (!new_pw.getText().toString().equals(new_pw_again.getText().toString())) {
            showToast("俩次新密码不一致!");
            return;
        }
        student.pw = new_pw.getText().toString();

        boolean insert = recordDb.update(student, " _id=? ",
                new String[]{student.key+""});
        if (insert) {
            MyApplication.getInstance().student=student;
            showToast("修改密码成功，请重新登录!");
            startActivity(new Intent(this,LoginActivity.class));
        } else {
            showToast("保存失败!");
        }
    }
}

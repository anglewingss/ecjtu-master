package com.ecjtu.assistant.app;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;

import com.ecjtu.assistant.db.StudentDb;

import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;

public class OtherApplication extends LitePalApplication {
    public Bitmap bit;//用户自己的头像
    public String name;
    public String school;//学院
    public String headerUrl;
    public String tel;
    public String pw;

    public  StudentDb.Record student;

    public Bitmap getBit() {
        return bit;
    }

    public void setBit(Bitmap bit) {
        this.bit = bit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    private static OtherApplication instance;

    public static OtherApplication getInstance() {
        return instance;
    }
}

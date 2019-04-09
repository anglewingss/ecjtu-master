package com.ecjtu.assistant.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ecjtu.assistant.R;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

/**
 * 基类，封装Activity通用方法.
 */

public class BaseActivity extends AppCompatActivity {

    private String LogTag = "BaseActivity:";
    protected String queueName = getClass().getName();
    public String TAG = "fdm";

    //this activity context
    protected Context mContext = null;

    //application context
    protected Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = this;
        context = getApplicationContext();

        SQLiteStudioService.instance().start(this);

        Log.i("fdm", String.format("queueName = %s", queueName));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @TargetApi(19)
    protected static void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }

        win.setAttributes(winParams);
    }

    protected void exitAllActivity() {

    }





    public void onBack(View v) {
        super.onBackPressed();
    }

    public void showToast(String s) {
        if (TextUtils.isEmpty(s))return;
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }



    public void setTitle(String title) {
        TextView textView = (TextView) findViewById(R.id.head_title);
        if (textView != null) {
            textView.setText(title == null ? getTitle() : title);//getTitle() 即是 manifest中声明的lable
        }
    }

    public void setTitle(int title) {
        TextView textView = (TextView) findViewById(R.id.head_title);
        if (textView != null) {
            textView.setText(title == 0 ? getTitle() : getString(title));
        }
    }

    /**
     * 设置右上角的文本文字
     *
     * @param text
     */
    public TextView setRightText(String text) {
        TextView textView = (TextView) findViewById(R.id.image_sure_tv);
        if (textView != null) {
            textView.setText(text);
        }
        return textView;
    }

    /**
     * 设置右上角的文本文字
     *
     * @param text
     */
    public TextView setRightText(int text) {
        TextView textView = (TextView) findViewById(R.id.image_sure_tv);
        if (textView != null) {
            textView.setText(text == 0 ? getTitle() : getString(text));
        }
        return textView;
    }


    /**
     */
    public void setRightViewVisible(boolean b) {
        TextView textView = (TextView) findViewById(R.id.image_sure_tv);
        if (b){
            textView.setVisibility(View.VISIBLE);
        }else {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     */
    public void setLeftViewVisible(boolean b) {
        if (b){
            findViewById(R.id.head_back).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.head_back).setVisibility(View.INVISIBLE);
        }
    }
}

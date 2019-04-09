package com.ecjtu.assistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.fragment.CourseFragment;
import com.ecjtu.assistant.fragment.LibraryFragment;
import com.ecjtu.assistant.fragment.MapFragment;
import com.ecjtu.assistant.fragment.MeFragment;
import com.ecjtu.assistant.fragment.NewsFragment;
import com.ecjtu.assistant.utils.ScreenUtils;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;

/**
 * 主页 HomeActivity
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private RadioButton nav_course_radio_button;
    private RadioButton newsRadioButton;
    private RadioButton libRadioButton;
    private RadioButton discoveryRadioButton;
    private RadioButton moreRadioButton;


    private FragmentManager fragmentManager;
    private long clickBackButtonTime = 0;
    private int clickToExitInterval = 2000;


    private NewsFragment newsFragment;
    private LibraryFragment libraryFragment;
    private MapFragment mapFragment;
    private CourseFragment courseFragment;
    private MeFragment moreFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(SOFT_INPUT_ADJUST_PAN | SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_home);
        initView();

        //显示状态栏
        ScreenUtils.setNomalScreen(getWindow());
    }

    private void initView() {

        nav_course_radio_button = (RadioButton) findViewById(R.id.nav_course_radio_button);
        newsRadioButton = (RadioButton) findViewById(R.id.nav_news_radio_button);
        libRadioButton = (RadioButton) findViewById(R.id.nav_lib_radio_button);
        discoveryRadioButton = (RadioButton) findViewById(R.id.nav_discovery_radio_button);
        moreRadioButton = (RadioButton) findViewById(R.id.nav_more_radio_button);


        newsRadioButton.setOnClickListener(this);
        libRadioButton.setOnClickListener(this);
        discoveryRadioButton.setOnClickListener(this);
        moreRadioButton.setOnClickListener(this);
        nav_course_radio_button.setOnClickListener(this);

        ScreenUtils.setFullscreen(getWindow());

        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);
    }

    private void setTabSelection(int index) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:

                if (newsFragment == null) {

                    newsFragment = new NewsFragment();
                    transaction.add(R.id.frame_layout, newsFragment);
                } else {
                    transaction.show(newsFragment);
                }
                break;

            case 1:

                if (libraryFragment == null) {

                    libraryFragment = new LibraryFragment();
                    transaction.add(R.id.frame_layout, libraryFragment);
                } else {
                    transaction.show(libraryFragment);
                }

                break;

            case 2:
                if (courseFragment == null) {

                    courseFragment = new CourseFragment();
                    transaction.add(R.id.frame_layout, courseFragment);
                } else {
                    transaction.show(courseFragment);
                }
                break;

            case 3:

                if (mapFragment == null) {

                    mapFragment = new MapFragment();
                    transaction.add(R.id.frame_layout, mapFragment);
                } else {
                    transaction.show(mapFragment);
                }
                break;

            case 4:

                if (moreFragment == null) {

                    moreFragment = new MeFragment();
                    transaction.add(R.id.frame_layout, moreFragment);
                } else {
                    transaction.show(moreFragment);
                }
                break;
        }

        transaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction transaction) {

        if (courseFragment != null) {
            transaction.hide(courseFragment);
        }

        if (newsFragment != null) {
            transaction.hide(newsFragment);
        }

        if (libraryFragment != null) {
            transaction.hide(libraryFragment);
        }

        if (moreFragment != null) {
            transaction.hide(moreFragment);
        }
        if (mapFragment!=null){
            transaction.hide(mapFragment);
        }
    }

    private void doubleClickToExit() {

        if ((System.currentTimeMillis() - clickBackButtonTime) > clickToExitInterval) {

            Toast.makeText(getApplicationContext(), R.string.double_click_to_exit, Toast.LENGTH_SHORT).show();
            clickBackButtonTime = System.currentTimeMillis();

        } else {
            finish();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            doubleClickToExit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.nav_news_radio_button:
                setTabSelection(0);
                break;
            case R.id.nav_lib_radio_button:
                setTabSelection(1);
                break;
            case R.id.nav_course_radio_button:
                setTabSelection(2);
                break;
            case R.id.nav_discovery_radio_button:
                setTabSelection(3);
                break;
            case R.id.nav_more_radio_button:
                setTabSelection(4);
                break;
        }

    }
}

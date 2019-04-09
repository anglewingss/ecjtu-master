package com.ecjtu.assistant.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 基类，封装Fragment通用方法
 */

public class BaseFragment extends Fragment {

    private String LogTag = "BaseFragment:";
    protected String className = getClass().getName();
    public String TAG = "fdm";

    //this activity context
    protected Context mContext = null;

    //application context
    protected Context context = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
        context = getActivity().getApplicationContext();
        mContext = getActivity();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

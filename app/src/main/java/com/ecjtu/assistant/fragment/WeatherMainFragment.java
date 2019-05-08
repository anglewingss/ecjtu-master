package com.ecjtu.assistant.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecjtu.assistant.R;
import com.ecjtu.weather.WeatherActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherMainFragment extends BaseFragment {

    private View view;


    public WeatherMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);

        mContext = getActivity();
        view = inflater.inflate(R.layout.activity_main,container,false);

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getContext());
        if(prefs.getString("weather",null)!=null)
        {
            Intent intent=new Intent(getActivity(), WeatherActivity.class);
            startActivity(intent);
        }


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
}

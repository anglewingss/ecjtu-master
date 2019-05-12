package com.ecjtu.assistant.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ecjtu.assistant.R;
import com.ecjtu.weather.WeatherActivity;
import com.ecjtu.weather.gson.AQI;
import com.ecjtu.weather.gson.Weather;
import com.ecjtu.weather.service.AutoUpdateService;
import com.ecjtu.weather.util.HttpUtil;
import com.ecjtu.weather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends BaseFragment {

    private View view;

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;

    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;

    public DrawerLayout drawerLayout;
    private Button navButton;

    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);

        mContext = getActivity();
        view = inflater.inflate(R.layout.activity_weather,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

//        if(Build.VERSION.SDK_INT>=21)
//        {
//            View decorView=getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            );
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        //setContentView(R.layout.activity_weather);
        //初始化各控件
        swipeRefresh=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        weatherLayout=(ScrollView) view.findViewById(R.id.weather_layout);
        titleCity=(TextView)view.findViewById(R.id.title_city);
        titleUpdateTime=(TextView)view.findViewById(R.id.title_update_time);
        degreeText=(TextView)view.findViewById(R.id.degree_text);
        weatherInfoText=(TextView)view.findViewById(R.id.weather_info_text);
        forecastLayout=(LinearLayout)view.findViewById(R.id.forecast_layout);
        aqiText=(TextView)view.findViewById(R.id.aqi_text);
        pm25Text=(TextView)view.findViewById(R.id.pm25_text);
        comfortText=(TextView)view.findViewById(R.id.comfort_text);
        carWashText=(TextView)view.findViewById(R.id.car_wash_text);
        sportText=(TextView)view.findViewById(R.id.sport_text);
        bingPicImg=(ImageView)view.findViewById(R.id.bing_pic_img);
        drawerLayout=(DrawerLayout)view.findViewById(R.id.drawer_layout);
        navButton=(Button)view.findViewById(R.id.nav_button);

        swipeRefresh.setColorSchemeColors(R.color.colorPrimary);
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String weatherString=prefs.getString("weather",null);
        String weatherString= "CN101120102";
        mWeatherId = "CN101120102";
        /*String aqiString=prefs.getString("aqi",null);*/



//        if(weatherString!=null)
//        {
//            //有缓存时直接解析天气数据
//            Weather weather= (Weather) Utility.handleWeatherResponse(weatherString);
//            mWeatherId=weather.getHeWeather6().get(0).getBasicX().getCid();
//
//            showWeatherInfo(weather);
//
//        }else
//        {   //无缓存时,去服务器查询天气
            //String weatherId=getIntent().getStringExtra("weather_id");
//            mWeatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
//        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        /**
         * 读取缓存在SharedPreference的pic数据,
         */
//        String bingPic=prefs.getString("bing_pic",null);
//        if(bingPic!=null)
//        {
//            Glide.with(this).load(bingPic).into(bingPicImg);
//        }
//        else
//        {
            loadBingPic();
//        }
//yt
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingpic=response.body().string();
                SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                editor.putString("bing_pic",bingpic);
                editor.apply();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getActivity()).load(bingpic).into(bingPicImg);
                    }
                });
            }
        });
    }

    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(String weatherId)
    {
        final String weatherUrl="https://free-api.heweather.com/s6/weather?location="+weatherId+"&key=5cfa71f0523045cbbc2a915848c89ad4";
        //final String aqiUrl="https://free-api.heweather.com/s6/air/now?location="+weatherId+"&key=5cfa71f0523045cbbc2a915848c89ad4";

        /**
         * 这是对基本天气的访问,但是缺了aqi这一项
         */
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"获取天气信息失败onFailure", Toast.LENGTH_LONG).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=Utility.handleWeatherResponse(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if((weather != null) && "ok".equals(weather.getHeWeather6().get(0).getStatusX()))
                        {
                            SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();

                            editor.putString("weather",responseText);
                            editor.apply();
                            mWeatherId=weather.getHeWeather6().get(0).getBasicX().getCid();
                            showWeatherInfo(weather);

                        }else
                        {
                            Toast.makeText(getActivity(), responseText, Toast.LENGTH_SHORT).show();

                        }
                        swipeRefresh.setRefreshing(false);
                    }


                });

            }
        });

//        HttpUtil.sendOkHttpRequest(aqiUrl, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity(),"获取天气信息失败onFailure", Toast.LENGTH_LONG).show();
//                        swipeRefresh.setRefreshing(false);
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String responseText=response.body().string();
//                final AQI aqi=Utility.handleAQIResponse(responseText);
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        if((aqi != null) && "ok".equals(aqi.getHeWeather6().get(0).getStatus()))
//                        {
//                            SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
//
//                            editor.putString("weather",responseText);
//                            editor.apply();
//                            mWeatherId=aqi.getHeWeather6().get(0).getBasic().getCid();
//                            showAQIInfo(aqi);
//                        }else
//                        {
//                            Toast.makeText(getActivity(), responseText, Toast.LENGTH_SHORT).show();
//
//                        }
//                        swipeRefresh.setRefreshing(false);
//                    }
//
//                });
//            }
//        });
    }

//    private void showAQIInfo(AQI aqi) {
//
//        if(aqi!=null)
//        {
//            aqiText.setText(aqi.getHeWeather6().get(0).getAir_now_city().getAqi());
//            pm25Text.setText(aqi.getHeWeather6().get(0).getAir_now_city().getPm25());
//        }
//
//    }


    private void showWeatherInfo(Weather weather) {

        String cityName=weather.getHeWeather6().get(0).getBasicX().getLocation();
        String updateTime=weather.getHeWeather6().get(0).getUpdate().getLoc();
        String degree=weather.getHeWeather6().get(0).getNowX().getTmp()+"℃";
        String weatherInfo=weather.getHeWeather6().get(0).getNowX().getCond_txt();
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();

        for(int i=0;i<3;i++ )
        {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dataText=(TextView) view.findViewById(R.id.data_text);
            TextView infoText=(TextView) view.findViewById(R.id.info_text);
            TextView maxText=(TextView)view.findViewById(R.id.max_text);
            TextView minText=(TextView)view.findViewById(R.id.min_text);

            dataText.setText(weather.getHeWeather6().get(0).getDaily_forecast().get(i).getDate());
            infoText.setText(weather.getHeWeather6().get(0).getDaily_forecast().get(i).getCond_txt_n());
            maxText.setText(weather.getHeWeather6().get(0).getDaily_forecast().get(i).getTmp_max());
            minText.setText(weather.getHeWeather6().get(0).getDaily_forecast().get(i).getTmp_min());
            forecastLayout.addView(view);
        }


        comfortText.setText("舒适度："+weather.getHeWeather6().get(0).getLifestyle().get(0).getTxt());
        carWashText.setText("洗车指数："+weather.getHeWeather6().get(0).getLifestyle().get(6).getTxt());
        sportText.setText("运动指数："+weather.getHeWeather6().get(0).getLifestyle().get(3).getTxt());

        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent=new Intent(getActivity(), AutoUpdateService.class);
        getActivity().startService(intent);
    }
}

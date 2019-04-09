package com.ecjtu.assistant.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.ecjtu.assistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LMR on 2018/5/4.
 */

public class BusLineActivity extends Activity implements OnGetBusLineSearchResultListener {

    private ListView listView = null;
    private TextView textView;
    protected String busLineId;
    private BusLineSearch mBusLineSearch = null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_dialog);
        listView = (ListView) findViewById(R.id.transitList);
        textView=(TextView)findViewById(R.id.tv_title);
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        textView.setText("线路详情");
        busLineId= getIntent().getExtras().getString("id");
        mBusLineSearch = BusLineSearch.newInstance();
        mBusLineSearch.setOnGetBusLineSearchResultListener(this);
        mBusLineSearch.searchBusLine((new BusLineSearchOption()
                .city("南昌").uid(busLineId)));
    }
    @Override
    public void onGetBusLineResult(BusLineResult busLineResult) {
        String title;
        List<BusLineResult.BusStation> stations = busLineResult.getStations();
        List<String> datas=new ArrayList<String>();
        for (BusLineResult.BusStation busStation : stations) {
            title = busStation.getTitle();
            datas.add(title);
        }
        listView.setAdapter(new ArrayAdapter<String>(BusLineActivity.this, android.R.layout.simple_list_item_1, datas));
    }
}

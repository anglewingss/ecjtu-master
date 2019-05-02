package com.ecjtu.assistant.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.ecjtu.assistant.R;
import com.ecjtu.assistant.activity.BookSearchResultActivity;
import com.ecjtu.assistant.activity.BusLineActivity;
import com.ecjtu.assistant.adapter.RouteLineAdapter;
import com.ecjtu.assistant.db.BookLibDb;
import com.ecjtu.overlayutil.OverlayManager;
import com.ecjtu.overlayutil.TransitRouteOverlay;
import com.ecjtu.overlayutil.WalkingRouteOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LMR on 2018/4/14.
 */

public class MapFragment extends BaseFragment implements BaiduMap.OnMapClickListener,OnGetPoiSearchResultListener, OnGetRoutePlanResultListener {
    private EditText start_edit, end_edit, query_edit;
    private ListView resultListView;
    private ListAdapter resultAdpter;
    private List<String> poiResultList = new ArrayList<>();
    private List<PoiInfo> poiInfoList = new ArrayList<PoiInfo>();
    private PoiSearch poiSearch = null;
    public String busLineId;
    private View view;
    private String localcity;// 记录当前城市
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    ;
    boolean useDefaultIcon = false;
    TransitRouteResult nowResultransit = null;
    boolean hasShownDialogue = false;
    boolean isFirstLoc = true; // 是否首次定位

    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    // 地图控件
    private MapView mMapView = null;
    private BaiduMap mBaidumap;
    // 搜索相关
    RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    WalkingRouteResult nowResultwalk = null;
    // 定位相关
    LocationClient mLocClient;
    //private LocationClient locationClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mContext = getActivity();
        SDKInitializer.initialize(getActivity().getApplicationContext());
        view = inflater.inflate(R.layout.fragment_map, container, false);
        initView();
        // 初始化地图
        inintmap();

        //获取定位权限
        geLocationPower();
        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;

        // 地图点击事件处理
        mapClick();

        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();

        mSearch.setOnGetRoutePlanResultListener(this);
        //POI搜索监听事件
        poiSearchResultListener();
        //mLocClient.start();
        return view;
    }

    /**
     * POI搜索监听事件
     */
    public void poiSearchResultListener(){
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                poiInfoList.clear();
                poiInfoList = poiResult.getAllPoi();
                for (PoiInfo poiInfo: poiInfoList) {
                    System.out.println(poiInfo.name);
                    poiResultList.add(poiInfo.name);
                }

                resultAdpter = new ArrayAdapter<String> (getActivity(),android.R.layout.simple_expandable_list_item_1,poiResultList);
                resultListView.setAdapter(resultAdpter);
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            }
        });
    }

    /**
     * 地图点击事件
     */
    public void mapClick(){
        mBaidumap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                Toast.makeText(getActivity(),mapPoi.getName(),Toast.LENGTH_LONG).show();
                query_edit.setText(mapPoi.getName());
                return false;
            }
        });
    }

    //获取定位权限
    public void geLocationPower() {
        List<String> permissionList = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.READ_PHONE_STATE);
//        }
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        }
    }

    public void initView() {
        //mBaidumap.setMyLocationEnabled(true);

//        start_edit = (EditText) view.findViewById(R.id.start);
        //end_edit = (EditText) view.findViewById(R.id.end);
        query_edit = (EditText) view.findViewById(R.id.edit_query);
        resultListView = (ListView) view.findViewById(R.id.resultViewList);

    }

    public void inintmap() {
        // 地图初始化
        mMapView = (MapView) view.findViewById(R.id.mTexturemap);
        mBaidumap = mMapView.getMap();

        // 不显示缩放比例尺
        mMapView.showZoomControls(false);
        // 不显示百度地图Logo
        mMapView.removeViewAt(1);
        // 开启定位图层
        mBaidumap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(mContext);
        mLocClient.registerLocationListener(myListener);
        //先检测定位权限，然后在判断系统是否开启网络定位或者GPS定位
        geLocationPower();

        if (!gPSIsOPen(mContext)) {
            MyToast("请开启系统定位服务");
            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setTitle("请打开系统定位服务");
            dialog.setMessage("为方便在正常使用该地图服务，请打开系统定位服务！");
            dialog.setPositiveButton("设置", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    Toast.makeText(getActivity(), "打开后直接点击返回键即可，若不打开返回下次将再次出现", Toast.LENGTH_LONG).show();
                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                    mBaidumap.setMyLocationEnabled(true);
                    mLocClient.start();
                }
            });
            dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            dialog.show();
        }

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps;
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(30000);
        option.setLocationNotify(false);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 0){
            mBaidumap.setMyLocationEnabled(true);
            mLocClient.start();
        }
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public final boolean gPSIsOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button button = (Button) view.findViewById(R.id.btn_search);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                poiSearch.searchInCity(new PoiCitySearchOption()
//                        .city("济南")
//                        .keyword(query_edit.getText().toString())
//                        .pageNum(10));
//            }
//        });
        changeTextListener();
//        Button button = (Button) view.findViewById(R.id.customer_go);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                route = null;
//                mBaidumap.clear();
//                // 设置起终点信息，对于tranist search 来说，城市名无意义
//                PlanNode stNode = PlanNode.withCityNameAndPlaceName(localcity, start_edit.getText().toString());
//                PlanNode enNode = PlanNode.withCityNameAndPlaceName(localcity, end_edit.getText().toString());
//                mSearch.transitSearch((new TransitRoutePlanOption())
//                        .from(stNode).city(localcity).to(enNode));
//            }
//        });
//        Button search = (Button) view.findViewById(R.id.btn_search);
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                busSearch.searchInCity((new PoiCitySearchOption()).city(
//                        localcity)
//                        .keyword(query_edit.getText().toString()));
//            }
//        });

    }

    /**
     * text文本框改变监听事件
     */
    public void changeTextListener(){

        query_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0){

                }
                poiSearch.searchInCity(new PoiCitySearchOption()
                        .city("济南")
                        .keyword(query_edit.getText().toString())
                        .pageNum(10));
            }
        });
    }


    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("提示");
            builder.setMessage("检索地址有歧义，请重新设置。\n可通过getSuggestAddrInfo()接口获得建议查询信息");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {

            if (result.getRouteLines().size() > 1) {
                nowResultwalk = result;
                if (!hasShownDialogue) {
                    MyTransitDlg myTransitDlg = new MyTransitDlg(getActivity(),
                            result.getRouteLines(),
                            RouteLineAdapter.Type.WALKING_ROUTE);
                    myTransitDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            hasShownDialogue = false;
                        }
                    });
                    myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                        public void onItemClick(int position) {
                            route = nowResultwalk.getRouteLines().get(position);
                            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
                            mBaidumap.setOnMarkerClickListener(overlay);
                            routeOverlay = overlay;
                            overlay.setData(nowResultwalk.getRouteLines().get(position));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        }

                    });
                    myTransitDlg.show();
                    hasShownDialogue = true;
                }
            } else if (result.getRouteLines().size() == 1) {
                // 直接显示
                route = result.getRouteLines().get(0);
                WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaidumap);
                mBaidumap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            } else {
                Log.d("route result", "结果数<0");
                return;
            }

        }
    }


    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未找到结果",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // 遍历所有poi，找到类型为公交线路的poi
        for (PoiInfo poi : poiResult.getAllPoi()) {
            if (poi.type == PoiInfo.POITYPE.BUS_LINE) {
                busLineId = poi.uid;
                Intent intent = new Intent(getActivity(), BusLineActivity.class);
                intent.putExtra("id", busLineId);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    interface OnItemInDlgClickListener {
        public void onItemClick(int position);
    }

    class MyTransitDlg extends Dialog {

        private List<? extends RouteLine> mtransitRouteLines;
        private ListView transitRouteList;
        private RouteLineAdapter mTransitAdapter;

        OnItemInDlgClickListener onItemInDlgClickListener;

        public MyTransitDlg(Context context, int theme) {
            super(context, theme);
        }

        public MyTransitDlg(Context context, List<? extends RouteLine> transitRouteLines, RouteLineAdapter.Type
                type) {
            this(context, 0);
            mtransitRouteLines = transitRouteLines;
            mTransitAdapter = new RouteLineAdapter(context, mtransitRouteLines, type);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        public void setOnDismissListener(OnDismissListener listener) {
            super.setOnDismissListener(listener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_transit_dialog);

            transitRouteList = (ListView) findViewById(R.id.transitList);
            transitRouteList.setAdapter(mTransitAdapter);

            transitRouteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemInDlgClickListener.onItemClick(position);
                    dismiss();
                    hasShownDialogue = false;
                }
            });
        }

        public void setOnItemInDlgClickLinster(OnItemInDlgClickListener itemListener) {
            onItemInDlgClickListener = itemListener;
        }

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            if (result.getRouteLines().size() > 1) {
                nowResultransit = result;
                if (!hasShownDialogue) {
                    MyTransitDlg myTransitDlg = new MyTransitDlg(getActivity(),
                            result.getRouteLines(),
                            RouteLineAdapter.Type.TRANSIT_ROUTE);
                    myTransitDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            hasShownDialogue = false;
                        }
                    });
                    myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                        public void onItemClick(int position) {

                            route = nowResultransit.getRouteLines().get(position);
                            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
                            mBaidumap.setOnMarkerClickListener(overlay);
                            routeOverlay = overlay;
                            overlay.setData(nowResultransit.getRouteLines().get(position));
                            overlay.addToMap();
                            overlay.zoomToSpan();
                        }

                    });
                    myTransitDlg.show();
                    hasShownDialogue = true;
                }
            } else if (result.getRouteLines().size() == 1) {
                // 直接显示
                route = result.getRouteLines().get(0);
                TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaidumap);
                mBaidumap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();

            } else {
                Log.d("route result", "结果数<0");
                return;
            }


        }
    }


    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }


    // 定制RouteOverly
    private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        mBaidumap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
        return false;
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    public void onRestart() {
        super.onStart();
        //mLocClient.start();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
        //mLocClient.start();
    }

    @Override
    public void onDestroy() {
        mSearch.destroy();
        mMapView.onDestroy();
        super.onDestroy();
    }

    public void MyToast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            mBaidumap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaidumap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//                start_edit.setText(location.getAddrStr());
                MyToast("当前所在位置：" + location.getAddrStr());
                localcity = location.getCity();
                String mm = "customer " + "location " + location.getLatitude() + " " + location.getLongitude() + "\n";


//                //判断GPS 是否打开
//                LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
//                // 判断GPS模块是否开启，如果没有则开启
//                if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
//                    Toast.makeText(getActivity(), "请打开GPS", Toast.LENGTH_SHORT).show();
//                    final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
//                    dialog.setTitle("请打开GPS连接");
//                    dialog.setMessage("为方便司机更容易接到您，请先打开GPS");
//                    dialog.setPositiveButton("设置", new android.content.DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            // 转到手机设置界面，用户设置GPS
//                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            Toast.makeText(getActivity(), "打开后直接点击返回键即可，若不打开返回下次将再次出现", Toast.LENGTH_LONG).show();
//                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
//                        }
//                    });
//                    dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            arg0.dismiss();
//                        }
//                    });
//                    dialog.show();
//                }
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}

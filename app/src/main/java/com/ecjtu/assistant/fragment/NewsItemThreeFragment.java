package com.ecjtu.assistant.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.activity.LifeInfoActivity;
import com.ecjtu.assistant.adapter.BaseRecycleViewHolderView;
import com.ecjtu.assistant.adapter.NewsRecyclerViewAdapter;
import com.ecjtu.assistant.db.BannerDBManager;
import com.ecjtu.assistant.db.BannerDb;
import com.ecjtu.assistant.db.DBManager;
import com.ecjtu.assistant.db.RecordDb;
import com.ecjtu.assistant.model.ScrollModel;
import com.ecjtu.assistant.utils.GlideImageLoader;
import com.ecjtu.assistant.utils.ReptileUtils;
import com.ecjtu.assistant.utils.ScreenUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LMR on 2018/5/5.
 */

public class NewsItemThreeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnBannerListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private View contentView;
    private Context context;
    private RecyclerView newsRecyclerView;
    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    private ProgressDialog progressDialog;

    private boolean isFirstCreateView = true;
    private int requestLinkNum = 0;
    private Banner banner;
    private int currentPage = 1;
    private List<RecordDb.Record> newsList = new ArrayList<>();
    private List<RecordDb.Record> allNewsList = new ArrayList<>();
    private List<BannerDb.Record> banerList = new ArrayList<>();
//    private List<Map<String, String>> bannerList;

    public static NewsItemThreeFragment newInstance(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("position", position);

        NewsItemThreeFragment newsItemThreeFragment = new NewsItemThreeFragment();
        newsItemThreeFragment.setArguments(bundle);

        return newsItemThreeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        contentView = inflater.inflate(R.layout.fragment_news_content, container, false);
        context = getActivity();
        initView();

//        dbManager = new DBManager(getActivity());
//        sqLiteDatabase = dbManager.openDatabase(getActivity());
//        allNewsList = dbManager.queryactivity(sqLiteDatabase, null, null, null);



//        BannerDBManager bannerDBManager = new BannerDBManager(getActivity());
//        SQLiteDatabase sqLiteDatabase = bannerDBManager.openDatabase(getActivity());
//        banerList = bannerDBManager.queryAll(sqLiteDatabase, null, null, null);
        getBanerList();

        //setDataForView();
        initData();
        return contentView;
    }

    private void initView() {

        //实例化DBHelper
        recordDb = RecordDb.getInstance();
        recordDb.open(getActivity());
        bannerDb = BannerDb.getInstance();
        bannerDb.open(getActivity());
        swipeRefreshLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipe_refresh_layout);

        newsRecyclerView = (RecyclerView) contentView.findViewById(R.id.fragment_news_recyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        newsRecyclerViewAdapter = new NewsRecyclerViewAdapter(context);
        newsRecyclerView.setAdapter(newsRecyclerViewAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.light_blue_600),
                getResources().getColor(R.color.green_300),
                getResources().getColor(R.color.orange_600));
        swipeRefreshLayout.setOnRefreshListener(this);

        //第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        requestLinkNum = 0;
        if (isFirstCreateView){
            showProgressDialog();
            isFirstCreateView = false;
        }

    }
    DBManager dbManager;
    SQLiteDatabase sqLiteDatabase;

    RecordDb recordDb;
    BannerDb bannerDb;

    @Override
    public void onDestroy() {
        super.onDestroy();
        recordDb.close();
        bannerDb.close();
    }

    public void saveData(List<Map<String, String>> newsList) {

        for (int i = 0; i < newsList.size(); i++) {
            Map<String, String> data = newsList.get(i);
            RecordDb.Record note = new RecordDb.Record();
            note.title = data.get("title");
            note.date = data.get("date");
            note.number = data.get("number");
            note.imgLink = data.get("imgLink");
            boolean insert = recordDb.insert(note);
        }
    }


    private void setDataForView( ) {

        //newsRecyclerViewAdapter.addDatas(newsList);
        newsRecyclerViewAdapter.setItemClickListener(new BaseRecycleViewHolderView.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (banerList.size() > 0) {
                    position = position - 1;
                }
                startActivity(new Intent(context, LifeInfoActivity.class)
//                        .putExtra("readNumber", newsList.get(position).number)
//                        .putExtra("newsLink", newsList.get(position).href));
                        .putExtra("newsLink", newsRecyclerViewAdapter.getDatas().get(position).href));
//                int n=Integer.parseInt(newsList.get(position).number);
//                newsList.get(position).number=(n+1)+"";
                newsRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        newsRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItem = manager.getItemCount();
                    if (lastVisibleItem >= (totalItem - 3)) {
                        Log.e("gaom lastVisibleItem=",lastVisibleItem+"");

                        currentPage++;
                        initData();
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        List<String> imageList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        for (int i = 0; i < banerList.size(); i++) {
            imageList.add(banerList.get(i).imgLink);
            titleList.add(banerList.get(i).title);
        }

        View header = LayoutInflater.from(mContext).inflate(R.layout.header, null);
        banner = (Banner) header.findViewById(R.id.banner);
        banner.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight(mContext) / 4));
        banner.setImages(imageList)
                .setBannerTitles(titleList)
                .setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(this)
                .setDelayTime(3000)
                .start();
        newsRecyclerViewAdapter.setHeaderView(header);
    }

    private String urlSuffix = "";
    private void initData() {
        urlSuffix = "71/list" + currentPage + ".htm";
        if(currentPage == 1){
            //第一页没有1这个数字
            urlSuffix = "71/list.htm";
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                //清空newslist
                newsList.clear();

                newsList = new ReptileUtils().getNewsList(urlSuffix);
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
        swipeRefreshLayout.setRefreshing(false);
//        int pageNum=4;
//        boolean b=false;
//        for (int i = (currentPage-1)*pageNum; i < ((currentPage-1)*pageNum)+pageNum; i++) {
//            if (i<allNewsList.size()) {
//                newsRecyclerViewAdapter.addData(allNewsList.get(i));
//            }else {
//                b=true;
//            }
//        }
//        if (b){
//            Toast.makeText(context, "没有更多数据了~", Toast.LENGTH_SHORT).show();
//            this.currentPage--;
//        }
    }

    private List<ScrollModel> scrollModelList = new ArrayList<ScrollModel>();
    private void getBanerList(){
        banerList.clear();
        scrollModelList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                scrollModelList = new ReptileUtils().getScrollModelList();
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void convertToBanerList(){

        for (ScrollModel scrollModel : scrollModelList) {
            BannerDb.Record banner = new BannerDb.Record();
            banner.title = scrollModel.getTitle();
            banner.imgLink = scrollModel.getSrc();
            banner.href = scrollModel.getUrl();
            banerList.add(banner);
        }
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
//                for (RecordDb.Record record : newsList) {//这个方法 加载第二页时程序闪退
//                    newsRecyclerViewAdapter.addData(record);
//                }
                //newsRecyclerViewAdapter.addDatas(newsList);
                for(int i = 0;i<newsList.size();i++){ //这个方法加载第二页时无响应
                    newsRecyclerViewAdapter.addData(newsList.get(i));
                }
                requestLinkNum++;
                if (requestLinkNum == 2){
                    closeProgressDialog();
                }
            }
            if (msg.what == 2){
                convertToBanerList();
                setDataForView();
                requestLinkNum++;
                if (requestLinkNum == 2){
                    closeProgressDialog();
                }
            }

        }
    };

    @Override
    public void onRefresh() {
//        currentPage = 1;
//        newsRecyclerViewAdapter.removeDatas();
//        initData();
        Toast.makeText(context, "已成功刷新新闻列表~", Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void OnBannerClick(int position) {
        startActivity(new Intent(context, LifeInfoActivity.class)
                .putExtra("readNumber", "6666")
                .putExtra("newsLink", banerList.get(position).href));
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if(progressDialog==null)
        {
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载....");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度条
     */
    private void closeProgressDialog()
    {
        if(progressDialog!=null)
            progressDialog.dismiss();
    }

}

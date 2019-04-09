package com.ecjtu.assistant.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.ecjtu.assistant.utils.GlideImageLoader;
import com.ecjtu.assistant.utils.ScreenUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 综合要闻
 */
public class NewsItemOneFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnBannerListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private View contentView;
    private Context context;
    private RecyclerView newsRecyclerView;
    private NewsRecyclerViewAdapter newsRecyclerViewAdapter;
    private Banner banner;

    private int currentPage = 1;
    private List<RecordDb.Record> newsList = new ArrayList<>();
    private List<RecordDb.Record> allNewsList = new ArrayList<>();
    private List<BannerDb.Record> banerList = new ArrayList<>();
//    private List<Map<String, String>> bannerList;

    public static NewsItemOneFragment newInstance(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("position", position);

        NewsItemOneFragment newsItemOneFragment = new NewsItemOneFragment();
        newsItemOneFragment.setArguments(bundle);

        return newsItemOneFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        contentView = inflater.inflate(R.layout.fragment_news_content, container, false);
        context = getActivity();
        initView();

        dbManager = new DBManager(getActivity());
        sqLiteDatabase = dbManager.openDatabase(getActivity());
        allNewsList = dbManager.queryAll(sqLiteDatabase, null, null, null);



        BannerDBManager bannerDBManager = new BannerDBManager(getActivity());
        SQLiteDatabase sqLiteDatabase = bannerDBManager.openDatabase(getActivity());
        banerList = bannerDBManager.queryAll(sqLiteDatabase, null, null, null);

        setDataForView() ;
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

        newsRecyclerViewAdapter.addDatas(newsList);
        newsRecyclerViewAdapter.setItemClickListener(new BaseRecycleViewHolderView.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (banerList.size() > 0) {
                    position = position - 1;
                }
                startActivity(new Intent(context, LifeInfoActivity.class)
                        .putExtra("readNumber", newsList.get(position).number)
                        .putExtra("newsLink", newsList.get(position).href));
                int n=Integer.parseInt(newsList.get(position).number);
                newsList.get(position).number=(n+1)+"";
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

    private void initData() {
        int pageNum=10;
        boolean b=false;
        for (int i = (currentPage-1)*pageNum; i < ((currentPage-1)*pageNum)+pageNum; i++) {
            if (i<allNewsList.size()) {
                newsRecyclerViewAdapter.addData(allNewsList.get(i));
            }else {
                b=true;
            }
        }

        if (b){
            Toast.makeText(context, "没有更多数据了~", Toast.LENGTH_SHORT).show();
            this.currentPage--;
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        newsRecyclerViewAdapter.removeDatas();
        initData();
        Toast.makeText(context, "已成功刷新新闻列表~", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnBannerClick(int position) {
        startActivity(new Intent(context, LifeInfoActivity.class)
                .putExtra("readNumber", "6666")
                .putExtra("newsLink", banerList.get(position).href));
    }
}
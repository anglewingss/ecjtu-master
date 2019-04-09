package com.ecjtu.assistant.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.adapter.NewsTabLayoutAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻
 */
public class NewsFragment extends BaseFragment {

    private View newsView;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private List<String> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mContext = getActivity();
        newsView = inflater.inflate(R.layout.fragment_news,container,false);
        initView();

        return newsView;

    }

    private void initView() {

        tabLayout = (TabLayout) newsView.findViewById(R.id.title_bar_TabLayout);
        viewPager = (ViewPager) newsView.findViewById(R.id.fragment_news_content_ViewPager);

        list.add("校园要闻");
        list.add("信息通告");
        list.add("学生活动");
        list.add("校园风光");

        NewsTabLayoutAdapter newsTabLayoutAdapter = new NewsTabLayoutAdapter(getActivity().getSupportFragmentManager(), list);
        viewPager.setAdapter(newsTabLayoutAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(newsTabLayoutAdapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }
}

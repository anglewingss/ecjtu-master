package com.ecjtu.assistant.fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.activity.BookSearchResultActivity;
import com.ecjtu.assistant.activity.MyBookActivity;
import com.ecjtu.assistant.activity.OverTimeBookActivity;
import com.ecjtu.assistant.db.MyBookDBManager;
import com.ecjtu.assistant.db.MyBookDb;

import java.util.ArrayList;
import java.util.List;

/**
 * 图书馆
 */
public class LibraryFragment extends BaseFragment {

    private View libraryView;
    private TextView num;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mContext = getActivity();
        libraryView = inflater.inflate(R.layout.fragment_library,container,false);
        initView();

        return libraryView;

    }
    private List<MyBookDb.Record> allNewsList = new ArrayList<>();
    SQLiteDatabase sqLiteDatabase;
    MyBookDBManager bannerDBManager;
    private void initView() {
        bannerDBManager = new MyBookDBManager(getActivity());
        sqLiteDatabase = bannerDBManager.openDatabase(getActivity());
        allNewsList = bannerDBManager.queryAll(sqLiteDatabase, null, null, null);

        num= (TextView) libraryView.findViewById(R.id.num);
        num.setText("已借书籍总数: "+allNewsList.size()+" 本");
        libraryView.findViewById(R.id.searchbook).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(context, BookSearchResultActivity.class);
//               intent.putExtra("keyWord", query);
               startActivity(intent);
           }
       });

       libraryView.findViewById(R.id.mybook).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(context, MyBookActivity.class);
//               intent.putExtra("keyWord", query);
               startActivity(intent);
           }
       });

       libraryView.findViewById(R.id.overtime).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent intent = new Intent(context, OverTimeBookActivity.class);
//               intent.putExtra("keyWord", query);
               startActivity(intent);
           }
       });


    }
}

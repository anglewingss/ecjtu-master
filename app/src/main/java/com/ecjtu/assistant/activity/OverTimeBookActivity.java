package com.ecjtu.assistant.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.adapter.OverTimeBooksAdapter;
import com.ecjtu.assistant.db.MyBookDBManager;
import com.ecjtu.assistant.db.MyBookDb;
import com.ecjtu.assistant.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class OverTimeBookActivity extends BaseActivity {

    private RecyclerView booksItemRecyclerView;
    private OverTimeBooksAdapter booksRecyclerViewAdapter;
    private List<MyBookDb.Record> allNewsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book);

        context = this;
        setTitle("逾期");

        //实例化DBHelper
//        bookLibDb = MyBookDb.getInstance();
//        bookLibDb.open(this);

//        setData();

        initView();
        setDataForView();
        MyBookDBManager myBookDBManager = new MyBookDBManager(this);
        SQLiteDatabase sqLiteDatabase = myBookDBManager.openDatabase(this);
        //select * from tb where addTime > '2011-12-11'
        allNewsList = myBookDBManager.queryAll(sqLiteDatabase, null, null, null);

        allNewsList    = myBookDBManager.queryAll (sqLiteDatabase, null,   " back_time < ? ",
                new String[] {TimeUtils.getCurDate("yyyy-MM-dd")});
        initData();

    }


    private void initView() {
        booksItemRecyclerView = (RecyclerView) findViewById(R.id.book_item_recyclerView);
        booksItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        booksRecyclerViewAdapter = new OverTimeBooksAdapter();
        booksItemRecyclerView.setAdapter(booksRecyclerViewAdapter);
    }

    private void initData() {
//        allNewsList.addAll(bookLibDb.query());
        booksRecyclerViewAdapter.addDatas(allNewsList);


    }

    private void setDataForView() {

        booksRecyclerViewAdapter.addDatas(allNewsList);
    }


}

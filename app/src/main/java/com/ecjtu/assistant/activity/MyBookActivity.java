package com.ecjtu.assistant.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.adapter.MyBooksAdapter;
import com.ecjtu.assistant.db.MyBookDBManager;
import com.ecjtu.assistant.db.MyBookDb;

import java.util.ArrayList;
import java.util.List;

public class MyBookActivity extends BaseActivity {

    private RecyclerView booksItemRecyclerView;
    private MyBooksAdapter booksRecyclerViewAdapter;
    private List<MyBookDb.Record> allNewsList = new ArrayList<>();
    SQLiteDatabase sqLiteDatabase;
    MyBookDBManager bannerDBManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book);

        context = this;
        setTitle("我的书籍");



        initView();
        setDataForView();


    }



    private void initView() {
        booksItemRecyclerView = (RecyclerView) findViewById(R.id.book_item_recyclerView);
        booksItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        booksRecyclerViewAdapter = new MyBooksAdapter(this);
        booksItemRecyclerView.setAdapter(booksRecyclerViewAdapter);
    }

    public void xujie(MyBookDb.Record data) {
        bannerDBManager.update(sqLiteDatabase, " title=? ",
                new String[]{data.title}, data);
        setDataForView();
    }

    private void setDataForView() {

        bannerDBManager = new MyBookDBManager(this);
        sqLiteDatabase = bannerDBManager.openDatabase(this);
        allNewsList = bannerDBManager.queryAll(sqLiteDatabase, null, null, null);



        booksRecyclerViewAdapter.addDatas(allNewsList);

    }


}

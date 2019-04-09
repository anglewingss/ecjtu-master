package com.ecjtu.assistant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.adapter.BaseRecycleViewHolderView;
import com.ecjtu.assistant.adapter.BooksRecyclerViewAdapter;
import com.ecjtu.assistant.db.BookLibDBManager;
import com.ecjtu.assistant.db.BookLibDb;
import com.ecjtu.assistant.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class BookSearchResultActivity extends BaseActivity {

    private TextView errorTextView;
    //    private ProgressBar progressBar;
    private RecyclerView booksItemRecyclerView;
    private BooksRecyclerViewAdapter booksRecyclerViewAdapter;

    //    private String keyWord = "";
    private int currentPage = 1;
    //    private List<Map<String, String>> booksList = new ArrayList<>();
    public List<BookLibDb.Record> allNewsList = new ArrayList<>();

    BookLibDBManager dbManager;
    SQLiteDatabase sqLiteDatabase;
    SearchView searchView;

    BookLibDb bookLibDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search_result);

        //实例化DBHelper
        bookLibDb = BookLibDb.getInstance();
        bookLibDb.open(this);

        dbManager = new BookLibDBManager(this);
        sqLiteDatabase = dbManager.openDatabase(this);
        allNewsList = dbManager.queryAll(sqLiteDatabase, null, null, null);

        Log.e("gaom allNewsList", allNewsList.size() + "");

        context = this;

        initView();
        initData();

        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setQuery("杂货店",true);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                closeKeybord(BookSearchResultActivity.this);
                booksRecyclerViewAdapter.getDatas().clear();

                List<BookLibDb.Record> allNewsList    = dbManager.query (sqLiteDatabase,    " bookName LIKE ? ",
                 new String[] { "%" + query + "%" });
                if (allNewsList.size() == 0) {
                    errorTextView.setVisibility(View.VISIBLE);
                } else {
                    errorTextView.setVisibility(View.GONE);
                    booksRecyclerViewAdapter.addDatas(allNewsList);
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        closeKeybord(BookSearchResultActivity.this);

    }

    public static void closeKeybord(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(),
                    0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bookLibDb.close();
    }

    private void initView() {


        errorTextView = (TextView) findViewById(R.id.book_error_textView);
        booksItemRecyclerView = (RecyclerView) findViewById(R.id.book_item_recyclerView);
        booksItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        booksRecyclerViewAdapter = new BooksRecyclerViewAdapter();
        booksItemRecyclerView.setAdapter(booksRecyclerViewAdapter);
        booksRecyclerViewAdapter.setItemClickListener(new BaseRecycleViewHolderView.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<BookLibDb.Record> list=booksRecyclerViewAdapter.getDatas();

                String bookLink = list.get(position).bookLink;

                if (StringUtils.isFine(bookLink)) {
                    Intent intent = new Intent(BookSearchResultActivity.this, BookDetailActivity.class);
                    intent.putExtra("bookLink", bookLink);
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {

        booksRecyclerViewAdapter.addDatas(allNewsList);


    }
    private void setDataForView() {
        booksRecyclerViewAdapter.addDatas(allNewsList);
        booksRecyclerViewAdapter.setItemClickListener(new BaseRecycleViewHolderView.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String bookLink = allNewsList.get(position).bookLink;

                if (StringUtils.isFine(bookLink)) {
                    Intent intent = new Intent(context, BookDetailActivity.class);
                    intent.putExtra("bookLink", bookLink);
                    startActivity(intent);
                }
            }
        });



    }


}

package com.ecjtu.assistant.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.adapter.MyCourseAdapter;
import com.ecjtu.assistant.app.MyApplication;
import com.ecjtu.assistant.db.CourseDb;

import java.util.ArrayList;
import java.util.List;

public class MyCourseActivity extends BaseActivity {

    private RecyclerView booksItemRecyclerView;
    private MyCourseAdapter booksRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book);

        context = this;
        setTitle("已选课程");

//        实例化DBHelper
        bookLibDb = CourseDb.getInstance();
        bookLibDb.open(this);

        initView();
        setDataForView();


    }


    CourseDb bookLibDb;


    private void initView() {
        booksItemRecyclerView = (RecyclerView) findViewById(R.id.book_item_recyclerView);
        booksItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        booksRecyclerViewAdapter = new MyCourseAdapter(this);
        booksItemRecyclerView.setAdapter(booksRecyclerViewAdapter);
    }


    private void setDataForView() {
        List<CourseDb.Record> allNewsList = new ArrayList<>();
        List<CourseDb.UserCourse> userCourses = bookLibDb.queryUserCourse(" userId =  " + MyApplication.getInstance().student.key);
        for (int i = 0; i < userCourses.size(); i++) {
            List<CourseDb.Record> query = bookLibDb.query(" _id =  " + userCourses.get(i).courseId);
            allNewsList.addAll(query);
        }
        booksRecyclerViewAdapter.getDatas().clear();
        booksRecyclerViewAdapter.addDatas(allNewsList);
    }


    public void unSelect(CourseDb.Record data) {

        boolean b = bookLibDb.deleteUserCourse(" courseId = ? and userId = ?",
                new String[] {data.key+"",MyApplication.getInstance().student.key+""});
        if (b) {
            showToast("退选成功!");
            setDataForView();
        }
    }


}

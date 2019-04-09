package com.ecjtu.assistant.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.adapter.CourseAdapter;
import com.ecjtu.assistant.app.MyApplication;
import com.ecjtu.assistant.db.CourseDBManager;
import com.ecjtu.assistant.db.CourseDb;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends BaseActivity {

    private RecyclerView booksItemRecyclerView;
    private CourseAdapter booksRecyclerViewAdapter;
    private int selectSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book);

        context = this;
        setTitle("可选课程");

        CourseDBManager courseDBManager = new CourseDBManager(this);
         courseDBManager.initDBManager(getPackageName());


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
        booksRecyclerViewAdapter = new CourseAdapter(this);
        booksItemRecyclerView.setAdapter(booksRecyclerViewAdapter);
    }

    private void setDataForView() {
        List<CourseDb.Record> allNewsList = new ArrayList<>();
        allNewsList = bookLibDb.query();
        List<CourseDb.Record> query2 = new ArrayList<>();
        List<CourseDb.UserCourse> userCourses = bookLibDb.queryUserCourse(" userId =  " + MyApplication.getInstance().student.key);
        for (int i = 0; i < userCourses.size(); i++) {
            List<CourseDb.Record> query = bookLibDb.query(" _id =  " + userCourses.get(i).courseId);
            query2.addAll(query);
            if (query.size() > 0) {
                for (int j = 0; j < allNewsList.size(); j++) {
                    if (allNewsList.get(j).key == query.get(0).key) {
                        allNewsList.remove(j);
                        break;
                    }
                }
            }
        }

        selectSize = query2.size();

        Log.e("gaom selectSize=", "" + selectSize);
        booksRecyclerViewAdapter.getDatas().clear();
        booksRecyclerViewAdapter.addDatas(allNewsList);

    }


    public void select(CourseDb.Record data) {
        if (selectSize > 2) {
            showToast("已选课程不能超过3个!");
            return;
        }
        CourseDb.UserCourse userCourse = new CourseDb.UserCourse();
        userCourse.courseId = data.key + "";
        userCourse.userId = MyApplication.getInstance().student.key + "";
        boolean b = bookLibDb.insertUserCourse(userCourse);
        if (b) {
            showToast("选课成功!");
            setDataForView();
        }
    }


}

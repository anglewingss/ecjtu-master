package com.ecjtu.assistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.activity.CourseActivity;
import com.ecjtu.assistant.activity.MyCourseActivity;


/**
 * 图书馆
 */
public class CourseFragment extends BaseFragment {

    private View libraryView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mContext = getActivity();
        libraryView = inflater.inflate(R.layout.fragment_course,container,false);
        initView();

        return libraryView;

    }
    private void initView() {

        //可选课程
        libraryView.findViewById(R.id.mybook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,  CourseActivity.class);
//               intent.putExtra("keyWord", query);
                startActivity(intent);
            }
        });
        //已选课程
        libraryView.findViewById(R.id.searchbook).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(context, MyCourseActivity.class);
//               intent.putExtra("keyWord", query);
               startActivity(intent);
           }
       });




    }
}

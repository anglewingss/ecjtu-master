package com.ecjtu.assistant.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.ecjtu.assistant.R;
import com.ecjtu.assistant.activity.EditInfoActivity;
import com.ecjtu.assistant.activity.LoginActivity;
import com.ecjtu.assistant.app.MyApplication;
import com.ecjtu.assistant.db.StudentDb;

public class MeFragment extends Fragment {
    TextView head_title;
    TextView name;
    TextView school;
    ImageView imageView;
    public StudentDb.Record student;

    @Override
    public void onResume() {
        super.onResume();
        student = MyApplication.getInstance().student;

        name.setText(student.name);
        school.setText(student.school);
        try {
            Bitmap bit = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(Uri.parse(MyApplication.getInstance().student.headerUrl)));
            imageView.setImageBitmap(bit);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fargment_me, null);


        head_title = (TextView) view.findViewById(R.id.head_title);
        name = (TextView) view.findViewById(R.id.name);
        school = (TextView) view.findViewById(R.id.school);
        imageView = (ImageView) view.findViewById(R.id.headerimg);

        head_title.setText("个人中心");


        view.findViewById(R.id.ll_info).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), EditInfoActivity.class));

            }
        });

        view.findViewById(R.id.image_sure_tv).setVisibility(View.VISIBLE);
        TextView rightTv = (TextView) view.findViewById(R.id.image_sure_tv);
        rightTv.setText("退出登录");
        rightTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        view.findViewById(R.id.head_back).setVisibility(View.GONE);


        return view;
    }


}
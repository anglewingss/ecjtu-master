package com.ecjtu.assistant.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.ecjtu.assistant.R;
import com.ecjtu.assistant.activity.EditInfoActivity;
import com.ecjtu.assistant.activity.LoginActivity;
import com.ecjtu.assistant.activity.WebActivity;
import com.ecjtu.assistant.app.MyApplication;
import com.ecjtu.assistant.db.StudentDb;
import com.ecjtu.assistant.utils.NetWorkUtils;
import com.ecjtu.assistant.utils.ReptileUtils;
import com.ecjtu.assistant.utils.ToastUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

        Button openWebPage = (Button) view.findViewById(R.id.openWebPage);
//        openWebPage.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), WebActivity.class));
//                getActivity().finish();
//            }
//        });

        Button connect = (Button) view.findViewById(R.id.connect);

        connect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {

                    private HttpURLConnection conn;
                    private String strurl = NetWorkUtils.url + "/request/test";
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(strurl);
                            conn = (HttpURLConnection) url.openConnection();
                            conn.connect();
                            conn.setRequestMethod("GET");//设置请求方式为GET
                            conn.setConnectTimeout(6 * 1000);//设置连接超时的时间
                            conn.setReadTimeout(8000);//设置读取超时的毫秒数
                            //conn.setDoInput(true); //允许输入流，即允许下载
                            //conn.setDoOutput(true); //允许输出流，即允许上传

                            //if (conn.getResponseCode() != 200)//判断是否请求成功
                                //throw new RuntimeException("运行异常");
                            //获取服务器返回的输入流
                            InputStream is = conn.getInputStream();
                            String str = convertStreamToString(is);
                            Looper.prepare();
                            Toast.makeText(getActivity(), "Http"+str, Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            System.out.println("HttpUrlConnection方式"+str);


                        } catch (Exception e) {
                            System.out.println("utils异常");
                            e.printStackTrace();
                        }
                        //关闭http链接
                        conn.disconnect();
                    }
                }).start();
            }
        });

        Button reptile = (Button)view.findViewById(R.id.reptile);
        reptile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new ReptileUtils().getScrollModelList();
                    }
                }).start();
            }
        });
        return view;
    }

    public static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the
         * BufferedReader.readLine() method. We iterate until the BufferedReader
         * return null which means there's no more data to read. Each line will
         * appended to a StringBuilder and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
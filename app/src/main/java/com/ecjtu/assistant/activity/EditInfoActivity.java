package com.ecjtu.assistant.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecjtu.assistant.R;
import com.ecjtu.assistant.app.MyApplication;
import com.ecjtu.assistant.db.StudentDb;

public class EditInfoActivity extends BaseActivity {

    StudentDb recordDb;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @Override
    public void onDestroy() {
        super.onDestroy();
        recordDb.close();
    }

    TextView head_title;
    EditText name;
    EditText school;
    ImageView imageView;
    public StudentDb.Record student;

    private void setTitleBar() {
        TextView titleTv = (TextView) findViewById(R.id.head_title);
        titleTv.setText("编辑信息");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editinfo);
        setTitleBar();

        //实例化DBHelper
        recordDb = StudentDb.getInstance();
        recordDb.open(this);

        student = MyApplication.getInstance().student;


        head_title = (TextView) findViewById(R.id.head_title);
        name = (EditText) findViewById(R.id.name);
        school = (EditText) findViewById(R.id.school);
        imageView = (ImageView) findViewById(R.id.header);

        name.setText(student.name);
        school.setText(student.school);
        uriStr=student.headerUrl;
        try {
            if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
                // do your stuff..
                Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(MyApplication.getInstance().student.headerUrl)));
                imageView.setImageBitmap(bit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//选照片
                choosePhoto();
            }
        });
         findViewById(R.id.image_sure_tv).setVisibility(View.VISIBLE);
        TextView rightTv = (TextView)  findViewById(R.id.image_sure_tv);
        rightTv.setText("保存");
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(name.getText().toString())) {
                    showToast("请输入名称!");
                    return;
                }
                if (TextUtils.isEmpty(school.getText().toString())) {
                    showToast("请输入学院!");
                    return;
                }
                if (TextUtils.isEmpty(uriStr)) {
                    showToast("请选择图片!");
                    return;
                }

                save();
            }
        });
    }


    private void save() {

        student.name = name.getText().toString();
        student.school = school.getText().toString();
        student.headerUrl = uriStr;
        boolean insert = recordDb.update(student, " _id=? ",
                new String[]{student.key+""});
        if (insert) {
            MyApplication.getInstance().student=student;
            showToast("保存成功!");
            finish();
        } else {
            showToast("保存失败!");
        }
    }


    final static int REQUEST_CODE_PICK_IMAGE = 222;

    void choosePhoto() {
        /**
         * 打开选择图片的界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

    }

    String uriStr;

    public void onActivityResult(int req, int res, Intent data) {
        switch (req) {
            /**
             * 从相册中选取图片的请求标志
             */

            case REQUEST_CODE_PICK_IMAGE:
                if (res == RESULT_OK) {
                    try {
                        /**
                         * 该uri是上一个Activity返回的
                         */
                        Uri uri = data.getData();
                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
//                        MyApplication.getInstance().bit = bit;
                        uriStr = uri.toString();

//                        saveImage();
                        imageView.setImageBitmap(bit);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("tag", e.getMessage());
//                        Toast.makeText(this, "程序崩溃", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("liang", "失败");
                }

                break;

            default:
                break;
        }
    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialog("External storage", context,
                                    Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat.requestPermissions((Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context, final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
}

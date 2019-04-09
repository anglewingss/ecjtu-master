package com.ecjtu.assistant.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by KingYang on 16/8/3.
 * E-Mail: admin@kingyang.cn
 */
public class DialogUtil {


    public static void show2Btn(Context context, CharSequence msg, CharSequence btn1, DialogInterface.OnClickListener listener1) {
        show2Btn(context, "提示", msg, btn1, listener1, "取消", null);
    }

    public static AlertDialog show2Btn(Context context, CharSequence title, CharSequence msg, CharSequence btn1, DialogInterface.OnClickListener listener1,
                                       CharSequence btn2, DialogInterface.OnClickListener listener2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(btn1, listener1)
                .setNegativeButton(btn2, listener2);
        AlertDialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static void show1Btn(Context context, CharSequence msg) {
        show1Btn(context, msg, null);
    }

    public static void show1Btn(Context context, CharSequence msg, DialogInterface.OnClickListener listener) {
        show1Btn(context, "提示", msg, "确定", listener);
    }

    public static void show1Btn(Context context, CharSequence title, CharSequence msg, CharSequence btn1, DialogInterface.OnClickListener listener1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(btn1, listener1);
        builder.show().setCanceledOnTouchOutside(false);
    }

    public static AlertDialog show3Btn(Context context, CharSequence msg, CharSequence btn1, DialogInterface.OnClickListener listener1,
                                       CharSequence btn2, DialogInterface.OnClickListener listener2) {
        return show3Btn(context, "提示", msg, btn1, listener1, btn2, listener2, "取消", null);
    }

    public static AlertDialog show3Btn(Context context, CharSequence title, CharSequence msg, CharSequence btn1, DialogInterface.OnClickListener listener1,
                                       CharSequence btn2, DialogInterface.OnClickListener listener2, CharSequence btn3, DialogInterface.OnClickListener listener3) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(btn1, listener1)
                .setNegativeButton(btn2, listener2)
                .setNeutralButton(btn3, listener3);
        AlertDialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }


    public static ProgressDialog progress(Context context, CharSequence msg) {
        return progress(context, "", msg);
    }

    public static ProgressDialog progress(Context context, CharSequence title, CharSequence msg) {
        ProgressDialog dialog = ProgressDialog.show(context, title, msg, true, true);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public static AlertDialog showEdit(Context context, CharSequence title, View view, CharSequence btn1, DialogInterface.OnClickListener listener1) {
        return showEdit(context, title, view, btn1, listener1, "取消", null);
    }

    public static AlertDialog showEdit(Context context, CharSequence title, View view, CharSequence btn1, DialogInterface.OnClickListener listener1,
                                       CharSequence btn2, DialogInterface.OnClickListener listener2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(btn1, listener1)
                .setNegativeButton(btn2, listener2);
        AlertDialog dialog = builder.show();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

//
//    public static AlertDialog show2Edit(Context context, IDel iDel) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View v = LayoutInflater.from(context).inflate(R.layout.edit_link_layout, null);
//        TextInputLayout mInputAccount;
//        TextInputLayout mInputPassword;
//        EditText mEditAccount;
//        EditText mEditPassword;
//        mInputAccount = (TextInputLayout) v.findViewById(R.id.link);
//        mInputPassword = (TextInputLayout) v.findViewById(R.id.title);
//
//        mEditAccount = (EditText) mInputAccount.getEditText();
//        mEditPassword = (EditText) mInputPassword.getEditText();
//
//        builder.setView(v)
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        iDel.del(mEditAccount.getText().toString(), mEditPassword.getText().toString());
//                    }
//                })
//                .setNegativeButton("取消", null);
//        AlertDialog dialog = builder.show();
//
//        dialog.setCanceledOnTouchOutside(false);
//
//
//        return dialog;
//    }
//
//    public static AlertDialog showSelectEditor(Context context, IDel2 iDel) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View v = LayoutInflater.from(context).inflate(R.layout.selector_layout, null);
//
//        v.findViewById(R.id.rich).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                iDel.selectItem(1);
//            }
//        });
//        v.findViewById(R.id.siu).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                iDel.selectItem(0);
//            }
//        });
//        builder.setView(v);
//        AlertDialog dialog = builder.show();
//        dialog.setCanceledOnTouchOutside(false);
//        return dialog;
//    }

    private IDel iDel;

    public interface IDel {
        void del(String link, String title);
    }

    private IDel2 iDel2;

    public interface IDel2 {
        void selectItem(int item);
    }


}

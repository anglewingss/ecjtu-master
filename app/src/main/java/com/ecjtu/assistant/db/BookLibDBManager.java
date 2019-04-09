package com.ecjtu.assistant.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BookLibDBManager {
    private String DB_NAME = "booklib_db.db";
    private Context mContext;

    public BookLibDBManager(Context mContext) {
        this.mContext = mContext;
    }
    //把assets目录下的db文件复制到dbpath下
    public SQLiteDatabase initDBManager(String packName) {
        String dbPath = "/data/data/" + packName
                + "/databases/" + DB_NAME;
        if (!new File(dbPath).exists()) {
            try {
                FileOutputStream out = new FileOutputStream(dbPath);
                InputStream in = mContext.getAssets().open("booklib_db.db");
                byte[] buffer = new byte[1024];
                int readBytes = 0;
                while ((readBytes = in.read(buffer)) != -1)
                    out.write(buffer, 0, readBytes);
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return SQLiteDatabase.openOrCreateDatabase(dbPath, null);
    }

    //查询
    public List<BookLibDb.Record> queryAll(SQLiteDatabase sqliteDB, String[] columns, String selection, String[] selectionArgs) {
        List<BookLibDb.Record> list=new ArrayList<>();
        BookLibDb.Record note = null;
        try {
            String table = "booklib";
            Cursor cursor = sqliteDB.query(table, columns, selection, selectionArgs, null, null, null);

            cursor.moveToFirst();
            do {
                String title = cursor.getString(cursor.getColumnIndex("bookInfo2"));
                String date = cursor.getString(cursor.getColumnIndex("bookInfo"));
                String number = cursor.getString(cursor.getColumnIndex("bookInfo1"));
                String imgLink = cursor.getString(cursor.getColumnIndex("bookLink"));
                String href = cursor.getString(cursor.getColumnIndex("bookName"));

                note = new BookLibDb.Record();
                note.bookInfo2 =  title;
                note.bookInfo =  date;
                note.bookInfo1 =  number;
                note.bookLink =  imgLink;
                note.bookName =  href;
                list.add(note);
            } while (cursor.moveToNext());

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


//查询
    public List<BookLibDb.Record> query(SQLiteDatabase sqliteDB,   String selection, String[] selectionArgs) {
        List<BookLibDb.Record> list=new ArrayList<>();
        BookLibDb.Record note = null;
        try {
            String table = "booklib";
            Cursor cursor = sqliteDB.query(table, null, selection, selectionArgs, null, null, null);

            cursor.moveToFirst();
            do {
                String title = cursor.getString(cursor.getColumnIndex("bookInfo2"));
                String date = cursor.getString(cursor.getColumnIndex("bookInfo"));
                String number = cursor.getString(cursor.getColumnIndex("bookInfo1"));
                String imgLink = cursor.getString(cursor.getColumnIndex("bookLink"));
                String href = cursor.getString(cursor.getColumnIndex("bookName"));

                note = new BookLibDb.Record();
                note.bookInfo2 =  title;
                note.bookInfo =  date;
                note.bookInfo1 =  number;
                note.bookLink =  imgLink;
                note.bookName =  href;
                list.add(note);
            } while (cursor.moveToNext());

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }



    //数据库存储路径
    String filePath = "data/data/com.fedming.gdoulife/booklib_db.db";
    //数据库存放的文件夹 data/data/com.main.jh 下面
    String pathStr = "data/data/com.fedming.gdoulife";

//    SQLiteDatabase database;
    public  SQLiteDatabase openDatabase(Context context){
        System.out.println("filePath:"+filePath);
        File jhPath=new File(filePath);
        //查看数据库文件是否存在
        if(jhPath.exists()){
            Log.i("test", "存在数据库");
            //存在则直接返回打开的数据库
            return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
        }else{
            //不存在先创建文件夹
            File path=new File(pathStr);
            Log.i("test", "pathStr="+path);
            if (path.mkdir()){
                Log.i("test", "创建成功");
            }else{
                Log.i("test", "创建失败");
            };
            try {
                //得到资源
                AssetManager am= context.getAssets();
                //得到数据库的输入流
                InputStream is=am.open("booklib_db.db");
                Log.i("test", is+"");
                //用输出流写到SDcard上面
                FileOutputStream fos=new FileOutputStream(jhPath);
                Log.i("test", "fos="+fos);
                Log.i("test", "jhPath="+jhPath);
                //创建byte数组  用于1KB写一次
                byte[] buffer=new byte[1024];
                int count = 0;
                while((count = is.read(buffer))>0){
                    Log.i("test", "得到");
                    fos.write(buffer,0,count);
                }
                //最后关闭就可以了
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            //如果没有这个数据库  我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了
            return openDatabase(context);
        }
    }

}
package com.ecjtu.assistant.db;

import android.content.ContentValues;
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

public class MyBookDBManager {//mybook_db.db
    private String DB_NAME = "mybook_db.db";
    private Context mContext;

    public MyBookDBManager(Context mContext) {
        this.mContext = mContext;
    }
    //把assets目录下的db文件复制到dbpath下
    public SQLiteDatabase initDBManager(String packName) {
        String dbPath = "/data/data/" + packName
                + "/databases/" + DB_NAME;
        if (!new File(dbPath).exists()) {
            try {
                FileOutputStream out = new FileOutputStream(dbPath);
                InputStream in = mContext.getAssets().open("mybook_db.db");
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
    public List<MyBookDb.Record> queryAll(SQLiteDatabase sqliteDB, String[] columns, String selection, String[] selectionArgs) {
        List<MyBookDb.Record> list=new ArrayList<>();
        MyBookDb.Record note = null;
        try {
            String table = "mybook";
            Cursor cursor = sqliteDB.query(table, columns, selection, selectionArgs, null, null, null);
            cursor.moveToFirst();
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String remainday = cursor.getString(cursor.getColumnIndex("remain_day"));
                String num = cursor.getString(cursor.getColumnIndex("num"));
                String backtime = cursor.getString(cursor.getColumnIndex("back_time"));
                String borrowtime = cursor.getString(cursor.getColumnIndex("borrow_time"));
/*
        public String address;
        public String remainday;
        public String num;
        public String backtime;
        public String borrowtime;
        public String title;*/
                note = new MyBookDb.Record();
                note.title =  title;
                note.address =  address;
                note.remainday =  remainday;
                note.num =  num;
                note.backtime =  backtime;
                note.borrowtime =  borrowtime;
                list.add(note);
            } while (cursor.moveToNext());

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
  //更新
    public void update(SQLiteDatabase sqliteDB,   String selection, String[] selectionArgs,MyBookDb.Record record) {
        ContentValues values = new ContentValues();
        values.put(MyBookDb.DB_TABLE_COLUMN_address, record.address);
        values.put(MyBookDb.DB_TABLE_COLUMN_title, record.title);
        values.put(MyBookDb.DB_TABLE_COLUMN_remain_day, record.remainday);
        values.put(MyBookDb.DB_TABLE_COLUMN_DATE, record.date);
        values.put(MyBookDb.DB_TABLE_COLUMN_num, record.num);
        values.put(MyBookDb.DB_TABLE_COLUMN_borrow_time, record.borrowtime);
        values.put(MyBookDb.DB_TABLE_COLUMN_back_time, record.backtime);
         sqliteDB.update("mybook",  values,selection,selectionArgs);

    }



    //数据库存储路径
    String filePath = "data/data/com.fedming.gdoulife/mybook_db.db";
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
                InputStream is=am.open("mybook_db.db");
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